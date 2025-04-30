package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.OauthRespDto;
import com.deeptactback.deeptact_back.dto.oauth.GoogleInfo;
import com.deeptactback.deeptact_back.dto.oauth.GoogleToken;
import com.deeptactback.deeptact_back.dto.oauth.KakaoInfo;
import com.deeptactback.deeptact_back.dto.oauth.KakaoToken;
import com.deeptactback.deeptact_back.dto.oauth.NaverInfo;
import com.deeptactback.deeptact_back.dto.oauth.NaverToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;

public interface OauthService {
    String createGoogleOauthUrl();
    OauthRespDto getGoogleUserInfo(String code) throws URISyntaxException, JsonProcessingException;
    GoogleInfo getGoogleInfo(GoogleToken googleToken) throws URISyntaxException, JsonProcessingException;
    GoogleToken getGoogleToken(String code) throws URISyntaxException, JsonProcessingException;

    String createNaverOauthUrl();
    OauthRespDto getNaverUserInfo(String code) throws URISyntaxException, JsonProcessingException;
    NaverToken getNaverToken(String code)throws URISyntaxException, JsonProcessingException;
    NaverInfo getNaverInfo(NaverToken naverToken)throws URISyntaxException, JsonProcessingException;
    String createKakaoOauthUrl();
    OauthRespDto getKakaoUserInfo(String code) throws URISyntaxException, JsonProcessingException;
    KakaoInfo getKakaoInfo(KakaoToken kakaoToken) throws URISyntaxException, JsonProcessingException;
    KakaoToken getKakaoToken(String code) throws URISyntaxException, JsonProcessingException;
}
