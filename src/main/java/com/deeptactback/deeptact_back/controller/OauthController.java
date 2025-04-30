package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.dto.OauthReqDto;
import com.deeptactback.deeptact_back.dto.OauthRespDto;
import com.deeptactback.deeptact_back.service.OauthService;
import com.deeptactback.deeptact_back.vo.OauthResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/{provider}")
    public CMResponse<String> getOauthLoginUrl(@PathVariable String provider) {
        try {
            String url;
            switch (provider.toLowerCase()) {
                case "kakao":
                    url = oauthService.createKakaoOauthUrl();
                    break;
                case "google":
                    url = oauthService.createGoogleOauthUrl();
                    break;
                case "naver":
                    url = oauthService.createNaverOauthUrl();
                    break;
                default:
                    throw new BaseException(BaseResponseStatus.INVALID_PROVIDER);
            }
            return CMResponse.success(BaseResponseStatus.SUCCESS, url);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{provider}")
    public CMResponse<OauthResponseVo> getOauthAccount(@PathVariable String provider,
        @RequestBody OauthReqDto oauthReqDto) {
        try {
            OauthRespDto oauthResponsedto;
            switch (provider.toLowerCase()) {
                case "kakao":
                    oauthResponsedto = oauthService.getKakaoUserInfo(oauthReqDto.getCode());
                    break;
                case "google":
                    oauthResponsedto = oauthService.getGoogleUserInfo(oauthReqDto.getCode());
                    break;
                case "naver":
                    oauthResponsedto = oauthService.getNaverUserInfo(oauthReqDto.getCode());
                    break;
                default:
                    throw new BaseException(BaseResponseStatus.INVALID_PROVIDER);
            }
            OauthResponseVo oauthResponseVo = OauthResponseVo.dtoToVo(oauthResponsedto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, oauthResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
