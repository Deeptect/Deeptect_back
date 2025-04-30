package com.deeptactback.deeptact_back.dto;

import lombok.Data;

@Data
public class ChgPasswordReqDto {

    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
