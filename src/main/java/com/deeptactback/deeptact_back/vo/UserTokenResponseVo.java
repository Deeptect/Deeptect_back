package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.dto.UserTokenRespDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserTokenResponseVo {

    private String accessToken;
    private String refreshToken;

    @Builder
    public UserTokenResponseVo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public static UserTokenResponseVo dtoToVo(UserTokenRespDto userTokenRespDto) {
        return UserTokenResponseVo.builder()
            .accessToken(userTokenRespDto.getAccessToken())
            .refreshToken(userTokenRespDto.getRefreshToken())
            .build();
    }
}

