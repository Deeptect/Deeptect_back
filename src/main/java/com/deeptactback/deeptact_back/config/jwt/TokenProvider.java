package com.deeptactback.deeptact_back.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    // 비밀키 생성 (실제 운영환경에서는 설정 파일에서 관리하는 것을 권장)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final SecretKey signingKey;

    // Access Token과 Refresh Token의 유효기간을 각각 설정
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    // application.properties에서 값을 가져옴
    public TokenProvider(@Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access-token-expiration-time}") long accessTokenExpirationTime,
        @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime) {
        secretKey = secretKey.replaceAll("\\s+", "");
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String createAccessToken(String uuid) {
        return createToken(uuid, accessTokenExpirationTime);
    }

    public String createRefreshToken(String uuid) {
        return createToken(uuid, refreshTokenExpirationTime);
    }

    private String createToken(String uuid, long expirationTime) {
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTime);
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(uuid)
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    // 토큰 유효시간 검증 메서드
    public boolean validateAccessToken(String token) {
        return validateToken(token);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }

    private boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUuidFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    // JWT 디코딩 메서드
    public Claims decodeToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody(); // 클레임 반환
    }

    // 토큰에서 값 추출
    public String getSubject(String token) {
        String subject = Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

        try {
            return subject;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid subject format: " + subject, e);
        }
    }
}
