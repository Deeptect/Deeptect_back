package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.dto.oauth.GoogleInfo;
import com.deeptactback.deeptact_back.dto.oauth.GoogleToken;
import com.deeptactback.deeptact_back.dto.oauth.KakaoInfo;
import com.deeptactback.deeptact_back.dto.oauth.KakaoToken;
import com.deeptactback.deeptact_back.dto.oauth.NaverInfo;
import com.deeptactback.deeptact_back.dto.oauth.NaverToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OauthResponseDto {
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    public static OauthResponseDto entityToDto(KakaoInfo kakaoInfo, KakaoToken kakaoToken) {
        return OauthResponseDto.builder()
            .email(kakaoInfo.getEmail())
            .nickname(kakaoInfo.getNickname())
            .accessToken(kakaoToken.getAccessToken())
            .refreshToken(kakaoToken.getRefreshToken())
            .build();
    }

    public static OauthResponseDto entityToDto(NaverInfo naverInfo, NaverToken naverToken) {
        return OauthResponseDto.builder()
            .email(naverInfo.getEmail())
            .nickname(naverInfo.getName())
            .accessToken(naverToken.getAccessToken())
            .refreshToken(naverToken.getRefreshToken())
            .build();
    }

    public static OauthResponseDto entityToDto(GoogleInfo googleInfo, GoogleToken googleToken) {
        return OauthResponseDto.builder()
            .email(googleInfo.getEmail())
            .nickname(googleInfo.getName())
            .accessToken(googleToken.getAccessToken())
            .refreshToken(googleToken.getRefreshToken())
            .build();
    }
}
