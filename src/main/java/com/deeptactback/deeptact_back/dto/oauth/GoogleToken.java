package com.deeptactback.deeptact_back.dto.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class GoogleToken {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private String idToken;
    private String scope;

    public static GoogleToken entityToDto(Map<String, Object> jsonResponse) {
        return GoogleToken.builder()
            .accessToken((String) jsonResponse.get("access_token"))
            .refreshToken((String) jsonResponse.get("refresh_token"))
            .tokenType((String) jsonResponse.get("token_type"))
            //.expiresIn((Long) jsonResponse.get("expires_in"))
            .idToken((String) jsonResponse.get("id_token"))
            .build();
    }
}
