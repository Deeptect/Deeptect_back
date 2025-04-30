package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.common.Role;
import com.deeptactback.deeptact_back.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRespDto {
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String profileImageUrl;
    private Role role;
    private String provider;

    public static UserLoginRespDto entityToDto(User user, String accessToken, String refreshToken, String provider) {
        return UserLoginRespDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .role(user.getRole())
            .provider(provider)
            .build();
    }
}
