package com.deeptactback.deeptact_back.dto.oauth;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class KakaoToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String idToken;
    private Long expiresIn;
    private Long refreshTokenExpiresIn;
    private String scope;

    public static KakaoToken entityToDto(String accessToken, String refreshToken, String tokenType,
        Long expiresIn, String idToken, Long refreshTokenExpiresIn, String scope) {
        return KakaoToken.builder()
            .accessToken(accessToken)
            .tokenType(tokenType)
            .refreshToken(refreshToken)
            .idToken(idToken)
            .expiresIn(expiresIn)
            .refreshTokenExpiresIn(refreshTokenExpiresIn)
            .scope(scope)
            .build();
    }
}
