package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.common.Role;
import com.deeptactback.deeptact_back.dto.UserLoginRespDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponseVo {
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String profileImageUrl;
    private Role role;
    private String provider;

    @Builder
    public UserLoginResponseVo(String accessToken, String refreshToken, String nickname, String profileImageUrl, Role role, String provider) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.provider = provider;
    }

    public static UserLoginResponseVo dtoToVo(UserLoginRespDto userLoginRespDto) {
        return UserLoginResponseVo.builder()
            .accessToken(userLoginRespDto.getAccessToken())
            .refreshToken(userLoginRespDto.getRefreshToken())
            .nickname(userLoginRespDto.getNickname())
            .profileImageUrl(userLoginRespDto.getProfileImageUrl())
            .role(userLoginRespDto.getRole())
            .provider(userLoginRespDto.getProvider())
            .build();
    }
}
