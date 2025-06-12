package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.dto.UserTokenRespDto;
import com.deeptactback.deeptact_back.service.UserService;
import com.deeptactback.deeptact_back.vo.UserTokenResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/token")
public class TokenController {

    private final UserService userService;

    @GetMapping("/rotate")
    public CMResponse<UserTokenResponseVo> rotateToken() {
        try{
            UserTokenRespDto userTokenRespDto = userService.rotateToken();
            UserTokenResponseVo userTokenResponseVo = UserTokenResponseVo.dtoToVo(userTokenRespDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, userTokenResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
