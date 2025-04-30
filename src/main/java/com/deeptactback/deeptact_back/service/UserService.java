package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.ChgPasswordReqDto;
import com.deeptactback.deeptact_back.dto.UserShowRespDto;
import com.deeptactback.deeptact_back.dto.UserLoginRespDto;
import com.deeptactback.deeptact_back.dto.UserReqDto;
import com.deeptactback.deeptact_back.dto.UserTokenRespDto;

public interface UserService {
    void RegisterUser(UserReqDto userReqDto);
    UserLoginRespDto LoginUser(UserReqDto userReqDto);
    UserShowRespDto ShowUser();
    void updateUser(UserReqDto userReqDto);
    void deleteUser(UserReqDto userReqDto);
    void chgPassword(ChgPasswordReqDto chgPasswordReqDto);
    UserTokenRespDto rotateToken();
}
