package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserReqDto {

    private int user_id;
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String token;

    @Builder
    public UserReqDto(int user_id, String email, String password, String confirmPassword, String nickname, String token) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.nickname = nickname;
        this.token = token;
    }

}
