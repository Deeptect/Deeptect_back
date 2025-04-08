package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public static UserTokenResponseDto entityToDto(String accessToken, String refreshToken) {
        return UserTokenResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
