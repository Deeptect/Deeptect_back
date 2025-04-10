package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.dto.UserLoginResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponseVo {
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String role;
    private String provider;

    @Builder
    public UserLoginResponseVo(String accessToken, String refreshToken, String nickname, String email, String profileImageUrl, String role, String provider) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.provider = provider;
    }

    public static UserLoginResponseVo dtoToVo(UserLoginResponseDto userLoginResponseDto) {
        return UserLoginResponseVo.builder()
            .accessToken(userLoginResponseDto.getAccessToken())
            .refreshToken(userLoginResponseDto.getRefreshToken())
            .nickname(userLoginResponseDto.getNickname())
            .email(userLoginResponseDto.getEmail())
            .profileImageUrl(userLoginResponseDto.getProfileImageUrl())
            .role(userLoginResponseDto.getRole())
            .provider(userLoginResponseDto.getProvider())
            .build();
    }
}
