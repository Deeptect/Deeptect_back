package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowUserResponseDto {
    private String nickname;
    private String email;
    private boolean isVerified;

    public static ShowUserResponseDto entityToDto(User user){
        return ShowUserResponseDto.builder()
            .nickname(user.getNickname())
            .email(user.getEmail())
            .isVerified(user.isEmailVerified())
            .build();
    }
}
