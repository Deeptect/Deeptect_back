package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.config.jwt.TokenProvider;
import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.dto.ChgPasswordReqDto;
import com.deeptactback.deeptact_back.dto.UserShowRespDto;
import com.deeptactback.deeptact_back.dto.UserLoginRespDto;
import com.deeptactback.deeptact_back.dto.UserReqDto;
import com.deeptactback.deeptact_back.dto.UserTokenRespDto;
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
    public void RegisterUser(UserReqDto userReqDto) {

        // 400 : 데이터 누락
        if (userReqDto == null || userReqDto.getPassword() == null
            || userReqDto.getEmail() == null || userReqDto.getConfirmPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 2601 : 비밀번호 형식 불일치
        if (!(userReqDto.getPassword()).matches(
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]).{8,20}$")) {
            throw new BaseException(BaseResponseStatus.PASSWORD_FORMAT_INVALID);
        }

        // 2602 : 비밀번호 불일치
        if (!userReqDto.getPassword().equals(userReqDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        // 2100 : 중복된 이메일
        if (userRepository.existsByEmail(userReqDto.getEmail())) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
        }

        User user = User.builder()
            .uuid(null)
            .email(userReqDto.getEmail())
            .password(bCryptPasswordEncoder.encode(userReqDto.getPassword()))
            .nickname(userReqDto.getNickname())
            .profileImageUrl(null)
            .isEmailVerified(true)
            .role(null)
            .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserLoginRespDto LoginUser(UserReqDto userReqDto) {

        // 400 : 데이터 누락
        if (userReqDto.getEmail() == null || userReqDto.getPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 회원 존재 X 2106
        User user = userRepository.findByEmail(userReqDto.getEmail());
        if (user == null || !bCryptPasswordEncoder.matches(userReqDto.getPassword(),
            user.getPassword())) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        String accessToken = tokenProvider.createAccessToken(user.getUuid());
        String refreshToken = tokenProvider.createRefreshToken(user.getUuid());

        User updateUser = User.builder()
            .user_id(user.getUserId())
            .uuid(user.getUuid())
            .email(user.getEmail())
            .password(user.getPassword())
            .nickname(user.getNickname())
            .role(user.getRole())
            .refreshToken(refreshToken)
            .build();

        userRepository.save(updateUser);

        UserLoginRespDto userLoginRespDto = UserLoginRespDto.entityToDto(user, accessToken, refreshToken, "provider");

        return userLoginRespDto;
    }

    @Override
    public UserShowRespDto ShowUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        UserShowRespDto userShowRespDto = UserShowRespDto.entityToDto(user);
        return userShowRespDto;
    }

    @Override
    @Transactional
    public void updateUser(UserReqDto userReqDto) {

        // 400 : 데이터 누락
        if (userReqDto == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        User updatedUser = User.builder()
            .user_id(user.getUserId())
            .nickname(userReqDto.getNickname() != null ? userReqDto.getNickname()
                : user.getNickname())
            .email(userReqDto.getEmail() != null ? userReqDto.getEmail() : user.getEmail())
            .profileImageUrl(user.getProfileImageUrl() != null ? user.getProfileImageUrl() : null)
            .password(user.getPassword())
            .uuid(user.getUuid())
            .role(user.getRole())
            .refreshToken(user.getRefreshToken())
            .build();

        userRepository.save(updatedUser);

    }

    @Override
    @Transactional
    public void deleteUser(UserReqDto userReqDto) {
        // 400 : 데이터 누락
        if (userReqDto == null) {
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
        if (!bCryptPasswordEncoder.matches(userReqDto.getPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void chgPassword(ChgPasswordReqDto chgPasswordReqDto) {
        // 400 : 데이터 누락
        if (chgPasswordReqDto == null || chgPasswordReqDto.getCurrentPassword() == null ||
            chgPasswordReqDto.getNewPassword() == null ||
            chgPasswordReqDto.getEmail() == null ||
            chgPasswordReqDto.getConfirmPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 사용자 조회
        User user = userRepository.findByEmail(chgPasswordReqDto.getEmail());
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        // 현재 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(chgPasswordReqDto.getCurrentPassword(),
            user.getPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_CURRENT_PASSWORD);
        }

        // 2601 : 비밀번호 형식 불일치
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(chgPasswordReqDto.getNewPassword()).matches(regex)) {
            throw new BaseException(BaseResponseStatus.PASSWORD_FORMAT_INVALID);
        }

        // 2602 : 비밀번호 불일치
        if (!chgPasswordReqDto.getNewPassword()
            .equals(chgPasswordReqDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        user = User.builder()
            .user_id(user.getUserId())
            .nickname(user.getNickname())
            .password(bCryptPasswordEncoder.encode(chgPasswordReqDto.getNewPassword()))
            .email(user.getEmail())
            .uuid(user.getUuid())
            .role(user.getRole())
            .isEmailVerified(user.isEmailVerified())
            .build();

        userRepository.save(user);
    }

    public UserTokenRespDto rotateToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        String newAccessToken = tokenProvider.createAccessToken(user.getUuid());
        String newRefreshToken = tokenProvider.createRefreshToken(user.getUuid());

        User updatedUser = User.builder()
            .user_id(user.getUserId())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .role(user.getRole())
            .refreshToken(newRefreshToken)
            .build();

        userRepository.save(updatedUser);

        UserTokenRespDto userTokenRespDto = UserTokenRespDto.entityToDto(
            newAccessToken,
            newRefreshToken);
        return userTokenRespDto;
    }

}
