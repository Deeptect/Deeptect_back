package com.deeptactback.deeptact_back.config;

import com.deeptactback.deeptact_back.config.jwt.AuthorizationExtractor;
import com.deeptactback.deeptact_back.config.jwt.JwtAuthenticationFilter;
import com.deeptactback.deeptact_back.config.jwt.TokenProvider;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TokenProvider tokenProvider;
    private final AuthorizationExtractor authExtractor;

    public SecurityConfig(TokenProvider tokenProvider,
        AuthorizationExtractor authExtractor) {
        this.tokenProvider = tokenProvider;
        this.authExtractor = authExtractor;
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider, authExtractor);
    }

    @Bean // -> 암호화
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/api/v1/email/*",
                    "/api/v1/oauth/*",
                    "/api/v1/user/register",
                    "/api/v1/user/login",
                    "/api/v1/video/fetch-shorts",
                    "/api/v1/video/videos", // 수정필요
                    "/api/v1/video/analysis"
                )
                .permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정 추가

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5500");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
