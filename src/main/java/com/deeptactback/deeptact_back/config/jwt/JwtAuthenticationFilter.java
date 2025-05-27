package com.deeptactback.deeptact_back.config.jwt;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final AuthorizationExtractor authExtractor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.info(">>> JwtAuthenticationFilter 호출: {}", request.getRequestURI());

        String path = request.getRequestURI();

        if (shouldSkipFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = path.equals("/api/v1/token/rotate")
            ? authExtractor.extractRefreshToken(request)
            : authExtractor.extract(request, "Bearer").replaceAll("\\s+", "");

        String tokenType = path.equals("/api/v1/token/rotate") ? "refresh" : "access";

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token, tokenType)) {
            String uuid = tokenProvider.getSubject(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(uuid, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("uuid", uuid);
            filterChain.doFilter(request, response);
        } else {
            BaseException.sendErrorResponse(response, BaseResponseStatus.TOKEN_EXPIRED);
        }

    }

    private boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/oauth/") ||
            path.startsWith("/api/v1/email/") ||
            path.equals("/api/v1/user/login") ||
            path.equals("/api/v1/user/register") ||
            path.equals("/api/v1/video/fetch-shorts") ||
            path.equals("/api/v1/video/videos") ||
            path.equals("/api/v1/video/analysis");
    }

}
