package com.deeptactback.deeptact_back.dto;

import lombok.Data;

@Data
public class ResetPasswordReqDto {

    private String mail;
    private String code;
    private String password;
    private String confirmPassword;
}
