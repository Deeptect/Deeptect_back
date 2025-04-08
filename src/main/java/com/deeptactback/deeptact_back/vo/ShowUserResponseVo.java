package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.dto.ShowUserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowUserResponseVo {
    private String nickname;
    private String email;
    boolean isVerified;

    @Builder
    public ShowUserResponseVo(String username, String email, boolean isVerified) {
        this.nickname = nickname;
        this.email = email;
        this.isVerified = isVerified;
    }

    public static ShowUserResponseVo dtoToVo(
        ShowUserResponseDto showUserResponseDto) {
        return ShowUserResponseVo.builder()
            .nickname(showUserResponseDto.getNickname())
            .email(showUserResponseDto.getEmail())
            .isVerified(showUserResponseDto.isVerified())
            .build();
    }
}
