package com.deeptactback.deeptact_back.dto.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class NaverToken {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    //private Long expiresIn;

    public static NaverToken entityToDto(Map<String, Object> jsonResponse) {
        return NaverToken.builder()
            .accessToken((String) jsonResponse.get("access_token"))
            .refreshToken((String) jsonResponse.get("refresh_token"))
            .tokenType((String) jsonResponse.get("token_type"))
            //.expiresIn((Long) jsonResponse.get("expires_in"))
            .build();
    }
}
