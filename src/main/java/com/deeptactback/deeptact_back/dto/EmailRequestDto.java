package com.deeptactback.deeptact_back.dto;

import lombok.Data;

@Data
public class EmailRequestDto {
    private String mail;
    private String verifyCode;
}
