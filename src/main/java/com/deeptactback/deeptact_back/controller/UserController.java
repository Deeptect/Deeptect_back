package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.dto.ChgPasswordReqDto;
import com.deeptactback.deeptact_back.dto.UserShowRespDto;
import com.deeptactback.deeptact_back.dto.UserLoginRespDto;
import com.deeptactback.deeptact_back.dto.UserReqDto;
import com.deeptactback.deeptact_back.service.CloudflareR2Service;
import com.deeptactback.deeptact_back.service.EmailService;
import com.deeptactback.deeptact_back.service.UserService;
import com.deeptactback.deeptact_back.vo.LogListRespVo;
import com.deeptactback.deeptact_back.vo.UserShowResponseVo;
import com.deeptactback.deeptact_back.vo.UserLoginResponseVo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final CloudflareR2Service cloudflareR2Service;

    // 회원가입 API
    // 입력값 : userRequestDto
    // 반환값 : code, message
    @PostMapping("/register")
    public CMResponse<Void> registerUser(@RequestBody UserReqDto userReqDto) {
        try {
            userService.RegisterUser(userReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인 API
    // 입력값 : username, email, password, password_confirmation
    // 반환값 : code, message, Access Token, Refresh Token
    @PostMapping("/login")
    public CMResponse<UserLoginResponseVo> loginUser(@RequestBody UserReqDto userReqDto) {
        try {
            UserLoginRespDto userLoginRespDto = userService.LoginUser(userReqDto);
            UserLoginResponseVo userLoginResponseVo = UserLoginResponseVo.dtoToVo(userLoginRespDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, userLoginResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 조회 API
    // 입력값 : Token
    // 출력값 : code, message, showUserResponseVo
    @GetMapping("/show")
    public CMResponse<UserShowResponseVo> showUser() {
        try{
            UserShowRespDto userShowRespDto = userService.ShowUser();
            UserShowResponseVo userShowResponseVo = UserShowResponseVo.dtoToVo(userShowRespDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, userShowResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 정보 수정 API
    // 입력값 : Token, userRequestDto
    // 출력값 : code, message, null
    @PatchMapping("/update")
    public CMResponse<Void> updateUser(@RequestBody UserReqDto userReqDto) {
        try{
            userService.updateUser(userReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 정보 삭제 API
    // 입력값 : password
    // 출력값 : code, message, null
    @DeleteMapping("/delete")
    public CMResponse<Void> deleteUser(@RequestBody UserReqDto userReqDto) {
        try {
            userService.deleteUser(userReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 비밀번호 변경 API
    // 입력값 : currentPassword, password, confirmPassword
    // 출력값 : code, message, null
    @PatchMapping("/password/change")
    public CMResponse<Void> chgPassword(@RequestBody ChgPasswordReqDto chgPasswordReqDto) {
        try{
            userService.chgPassword(chgPasswordReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/logs")
    public CMResponse<List<LogListRespVo>> getLogsByUser() {
        List<LogListRespVo> logs = cloudflareR2Service.getUserLogs();
        return CMResponse.success(BaseResponseStatus.SUCCESS, logs);
    }
}
