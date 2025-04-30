package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.dto.UserShowRespDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShowResponseVo {
    private String nickname;
    private String email;
    private String profileImageUrl ;
    private boolean isVerified;

    @Builder
    public UserShowResponseVo(String nickname, String email, String profileImageUrl, boolean isVerified) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.isVerified = isVerified;
    }

    public static UserShowResponseVo dtoToVo(
        UserShowRespDto userShowRespDto) {
        return UserShowResponseVo.builder()
            .nickname(userShowRespDto.getNickname())
            .email(userShowRespDto.getEmail())
            .isVerified(userShowRespDto.isVerified())
            .build();
    }
}
