package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShowRespDto {
    private String nickname;
    private String email;
    private String profileImageUrl;
    private boolean isVerified;

    public static UserShowRespDto entityToDto(User user){
        return UserShowRespDto.builder()
            .nickname(user.getNickname())
            .email(user.getEmail())
            .profileImageUrl(user.getProfileImageUrl())
            .isVerified(user.isEmailVerified())
            .build();
    }
}
