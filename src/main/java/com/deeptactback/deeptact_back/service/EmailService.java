package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.ResetPasswordReqDto;
import jakarta.mail.internet.MimeMessage;

public interface EmailService {

    // 인증코드 생성
    String createCode();

    // 이메일 내용 초기화
    String setEmail(String email, String code);

    // 이메일 폼 생성
    MimeMessage createEmailForm(String email);

    void sendEmail(String string) ;

    void verifyCode(String email, String code);

    void resetPassword(ResetPasswordReqDto resetPasswordReqDto);
}
