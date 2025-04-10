package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.ChgPasswordRequestDto;
import com.deeptactback.deeptact_back.dto.ShowUserResponseDto;
import com.deeptactback.deeptact_back.dto.UserLoginResponseDto;
import com.deeptactback.deeptact_back.dto.UserRequestDto;
import com.deeptactback.deeptact_back.dto.UserTokenResponseDto;

public interface UserService {
    void RegisterUser(UserRequestDto userRequestDto);
    UserLoginResponseDto LoginUser(UserRequestDto userRequestDto);
    ShowUserResponseDto ShowUser();
    void updateUser(UserRequestDto userRequestDto);
    void deleteUser(UserRequestDto userRequestDto);
    void chgPassword(ChgPasswordRequestDto chgPasswordRequestDto);
    UserTokenResponseDto rotateToken();
}
