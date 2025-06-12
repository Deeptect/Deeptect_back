package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.dto.EmailReqDto;
import com.deeptactback.deeptact_back.dto.ResetPasswordReqDto;
import com.deeptactback.deeptact_back.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/email")
public class EmailController {
    private final EmailService emailService;

    // 이메일 인증코드 전송
    @PostMapping("/authcode")
    public CMResponse<Void> sendAuthEmail(@RequestBody EmailReqDto emailReqDto) {
        try {
            emailService.sendEmail(emailReqDto.getMail());
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify")
    public CMResponse<Void> verifyCode(@RequestBody EmailReqDto emailReqDto) {
        try {
            emailService.verifyCode(emailReqDto.getMail(), emailReqDto.getVerifyCode());
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 비밀번호 재설정 API
    // 입력값 :
    // 출력값 :
    @PostMapping("/password/reset")
    public CMResponse<Void> resetPassword(@RequestBody ResetPasswordReqDto resetPasswordReqDto) {
        try {
            emailService.resetPassword(resetPasswordReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
