package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    private int user_id;
    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;
    private String token; // JWT 토큰

    @Builder
    public UserRequestDto(int user_id, String nickname, String email, String password,String confirmPassword , String token) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.token = token;
    }

}
