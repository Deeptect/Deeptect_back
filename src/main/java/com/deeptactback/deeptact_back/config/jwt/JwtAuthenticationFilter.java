package com.deeptactback.deeptact_back.config.jwt;

import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.dto.UserTokenResponseDto;
import com.deeptactback.deeptact_back.repository.UserRepository;
import com.deeptactback.deeptact_back.vo.UserTokenResponseVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthorizationExtractor authExtractor;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, UserRepository userRepository,
        AuthorizationExtractor authExtractor) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authExtractor = authExtractor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.info(">>> JwtAuthenticationFilter 호출: {}", request.getRequestURI());

        if (shouldSkipFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 JWT 토큰 추출
        String accessToken = authExtractor.extract(request, "Bearer").replaceAll("\\s+", "");
        String refreshToken = authExtractor.extractRefreshToken(request);
        // accessToken 유효시간 검증 성공 시
        if (StringUtils.hasText(accessToken) && tokenProvider.validateAccessToken(accessToken)) {
            String uuid = tokenProvider.getSubject(accessToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(uuid, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("uuid", uuid);
            filterChain.doFilter(request, response);
        } else {
            BaseResponseStatus status;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.setContentType("application/json;charset=utf-8");

            // 리프레시 토큰이 있고 유효한 경우
            if (StringUtils.hasText(refreshToken) && tokenProvider.validateRefreshToken(refreshToken)) {
                String uuid = tokenProvider.getSubject(refreshToken);
                User user = userRepository.findByUuid(uuid);

                if (user != null && user.getRefreshToken().equals(refreshToken)) {
                    String newAccessToken = tokenProvider.createAccessToken(user.getUuid());
                    String newRefreshToken = tokenProvider.createRefreshToken(user.getUuid());

                    User updatedUser = User.builder()
                        .user_id(user.getUser_id())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .uuid(user.getUuid())
                        .refreshToken(newRefreshToken)
                        .build();

                    userRepository.save(updatedUser);

                    UserTokenResponseDto userTokenResponseDto = UserTokenResponseDto.entityToDto(newAccessToken,
                        newRefreshToken);
                    UserTokenResponseVo userTokenResponseVo = UserTokenResponseVo.dtoToVo(userTokenResponseDto);

                    status = BaseResponseStatus.ACCESS_TOKEN_RETURNED_SUCCESS;
                    // CMResponse cmResponse = CMResponse.success(status.getCode(), status,
                    // userTokenResponseVo);
                    writeResponse(response, null);
                    return;
                }
            }

            // 리프레시 토큰이 유효하지 않거나 사용자를 찾을 수 없는 경우
            status = BaseResponseStatus.TOKEN_EXPIRED;
            CMResponse cmResponse = CMResponse.fail(status);
            writeResponse(response, cmResponse);
        }
    }

    private boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/oauth/") ||
            path.startsWith("/api/v1/email/") ||
            path.equals("/api/v1/user/login") ||
            path.equals("/api/v1/user/register");
    }

    private void writeResponse(HttpServletResponse response, CMResponse cmResponse) {
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(cmResponse));
        } catch (IOException e) {
            log.error("응답 작성 중 오류 발생", e);
        }
    }

}
