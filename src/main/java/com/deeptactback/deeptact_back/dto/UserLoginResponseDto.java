package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String email;

    public static UserLoginResponseDto entityToDto(String accessToken, String refreshToken, String nickname, String email) {
        return UserLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .nickname(nickname)
            .email(email)
            .build();
    }
}
