package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.config.redis.RedisUtil;
import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.dto.ResetPasswordReqDto;
import com.deeptactback.deeptact_back.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "lysfox01@gmail.com";
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String createCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String code = random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        return code;
    }

    @Override
    public String setEmail(String email, String code) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        Context context = new Context();
        context.setVariable("code", code); // 인증 코드를 템플릿에 전달
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // 템플릿 처리
        String emailContent = templateEngine.process("codemail", context);

        return emailContent;
    }

    @Override
    public MimeMessage createEmailForm(String email) {
        // 인증 코드 생성
        String authCode = createCode();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.addRecipients(RecipientType.TO, email);
            message.setSubject("컴히얼 인증코드 안내 메일");
            message.setFrom(senderEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        // 인증 코드로 이메일 내용 설정
        String emailContent = setEmail(email, authCode);

        try {
            message.setContent(emailContent, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        redisUtil.setDataExpire(email, authCode, 60 * 30L);

        return message;
    }

    @Override
    public void sendEmail(String email) {
        if(email == null){
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        if (redisUtil.existData(email)) {
            redisUtil.deleteData(email);
        }
        // 이메일 폼 생성
        MimeMessage emailForm = createEmailForm(email);
        // 이메일 발송
        try {
            javaMailSender.send(emailForm);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAIL_MAIL_SEND);
        }
    }

    @Override
    public void verifyCode(String email, String code){
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_EMAIL_CODE);
        }

        if (!codeFoundByEmail.equals(code)) {
            throw new BaseException(BaseResponseStatus.INVALID_VERIFICATION_CODE);
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        user = User.builder()
            .user_id(user.getUserId())
            .nickname(user.getNickname())
            .password(user.getPassword())
            .email(user.getEmail())
            .uuid(user.getUuid())
            .isEmailVerified(true)
            .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordReqDto resetPasswordReqDto) {
        verifyCode(resetPasswordReqDto.getMail(), resetPasswordReqDto.getCode());

        if (!resetPasswordReqDto.getPassword().equals(resetPasswordReqDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        User user = userRepository.findByEmail(resetPasswordReqDto.getMail());
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        // 비밀번호 해싱
        String hashedPassword = bCryptPasswordEncoder.encode(resetPasswordReqDto.getPassword());

        user = User.builder()
            .user_id(user.getUserId())
            .nickname(user.getNickname())
            .password(hashedPassword)
            .email(user.getEmail())
            .uuid(user.getUuid())
            .isEmailVerified(user.isEmailVerified())
            .build();

        userRepository.save(user);

        redisUtil.deleteData(resetPasswordReqDto.getMail());
    }

}
