package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.config.jwt.TokenProvider;
import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.dto.ChgPasswordRequestDto;
import com.deeptactback.deeptact_back.dto.ShowUserResponseDto;
import com.deeptactback.deeptact_back.dto.UserLoginResponseDto;
import com.deeptactback.deeptact_back.dto.UserRequestDto;
import com.deeptactback.deeptact_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public void RegisterUser(UserRequestDto userRequestDto) {

        // 400 : 데이터 누락
        if (userRequestDto == null || userRequestDto.getPassword() == null || userRequestDto.getEmail() == null || userRequestDto.getConfirmPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 2601 : 비밀번호 형식 불일치
        if (!(userRequestDto.getPassword()).matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$")) {
            throw new BaseException(BaseResponseStatus.PASSWORD_FORMAT_INVALID);
        }

        // 2602 : 비밀번호 불일치
        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        // 2100 : 중복된 이메일
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
        }

        User user = User.builder()
            .nickname(userRequestDto.getNickname())
            .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail())
            .uuid(null)
            .isEmailVerified(false)
            .role(false)
            .profileImageUrl(null)
            .build();


        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserLoginResponseDto LoginUser(UserRequestDto userRequestDto) {

        // 400 : 데이터 누락
        if(userRequestDto.getEmail()==null || userRequestDto.getPassword()==null){
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 회원 존재 X 2106
        User user = userRepository.findByEmail(userRequestDto.getEmail());
        if(user==null || !bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())){
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        String accessToken = tokenProvider.createAccessToken(user.getUuid());
        String refreshToken = tokenProvider.createRefreshToken(user.getUuid());

        // refreshToken 추가
        User updateUser = User.builder()
            .user_id(user.getUser_id())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .refreshToken(refreshToken)
            .build();

        userRepository.save(updateUser);

        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .nickname(user.getNickname())
            .email(user.getEmail())
            .build();

        return userLoginResponseDto;
    }

    @Override
    public ShowUserResponseDto ShowUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal(); // uuid를 가져옵니다.

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        ShowUserResponseDto showUserResponseDto = ShowUserResponseDto.entityToDto(user);
        return showUserResponseDto;
    }

    @Override
    @Transactional
    public void updateUser(UserRequestDto userRequestDto) {

        // 400 : 데이터 누락
        if(userRequestDto==null){
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal(); // uuid를 가져옵니다.

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        User updatedUser = User.builder()
            .user_id(user.getUser_id())
            .nickname(userRequestDto.getNickname() != null ? userRequestDto.getNickname() : user.getNickname())
            .email(userRequestDto.getEmail() != null ? userRequestDto.getEmail() : user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .refreshToken(user.getRefreshToken())
            .build();

        userRepository.save(updatedUser);

    }

    @Override
    @Transactional
    public void deleteUser(UserRequestDto userRequestDto) {
        // 400 : 데이터 누락
        if(userRequestDto==null){
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        // 2106
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        // 2202
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void chgPassword(ChgPasswordRequestDto chgPasswordRequestDto) {
        // 400 : 데이터 누락
        if (chgPasswordRequestDto == null || chgPasswordRequestDto.getCurrentPassword() == null ||
            chgPasswordRequestDto.getNewPassword() == null ||
            chgPasswordRequestDto.getEmail() == null ||
            chgPasswordRequestDto.getConfirmPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 사용자 조회
        User user = userRepository.findByEmail(chgPasswordRequestDto.getEmail());
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        // 현재 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(chgPasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_CURRENT_PASSWORD);
        }

        // 2601 : 비밀번호 형식 불일치
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(chgPasswordRequestDto.getNewPassword()).matches(regex)) {
            throw new BaseException(BaseResponseStatus.PASSWORD_FORMAT_INVALID);
        }

        // 2602 : 비밀번호 불일치
        if (!chgPasswordRequestDto.getNewPassword().equals(chgPasswordRequestDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        user = User.builder()
            .user_id(user.getUser_id())
            .nickname(user.getNickname())
            .password(bCryptPasswordEncoder.encode(chgPasswordRequestDto.getNewPassword()))
            .email(user.getEmail())
            .uuid(user.getUuid())
            .isEmailVerified(user.isEmailVerified())
            .build();

        userRepository.save(user);
    }

}
