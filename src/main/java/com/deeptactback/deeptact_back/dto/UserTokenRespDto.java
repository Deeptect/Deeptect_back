package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenRespDto {

    private String accessToken;
    private String refreshToken;

    public static UserTokenRespDto entityToDto(String accessToken, String refreshToken) {
        return UserTokenRespDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
