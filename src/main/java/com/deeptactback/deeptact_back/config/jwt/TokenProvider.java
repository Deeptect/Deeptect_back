package com.deeptactback.deeptact_back.config.jwt;

import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.repository.UserRepository;
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
    private final UserRepository userRepository;

    // 비밀키 생성 (실제 운영환경에서는 설정 파일에서 관리하는 것을 권장)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final SecretKey signingKey;

    // Access Token과 Refresh Token의 유효기간을 각각 설정
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    // application.properties에서 값을 가져옴
    public TokenProvider(UserRepository userRepository, @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access-token-expiration-time}") long accessTokenExpirationTime,
        @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime) {
        secretKey = secretKey.replaceAll("\\s+", "");
        this.userRepository = userRepository;
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

    public boolean validateToken(String token, String tokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

            if (!claims.getExpiration().after(new Date())) {
                return false;
            }

            if (tokenType.equals("refresh")) {
                String uuid = claims.getSubject();
                User user = userRepository.findByUuid(uuid);
                if (user == null || !user.getRefreshToken().equals(token)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        String subject = Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

        return subject;
    }
}
