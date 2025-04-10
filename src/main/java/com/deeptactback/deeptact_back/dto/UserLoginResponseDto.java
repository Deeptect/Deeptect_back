package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String role;
    private String provider;

    public static UserLoginResponseDto entityToDto(User user, String accessToken, String refreshToken, String provider) {
        return UserLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .nickname(user.getNickname())
            .email(user.getEmail())
            .profileImageUrl(user.getProfileImageUrl())
            .role(user.getRole())
            .provider(provider)
            .build();
    }
}
