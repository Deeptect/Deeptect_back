package com.deeptactback.deeptact_back.dto.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class GoogleInfo {
    private String sub;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
    private boolean email_verified;

    @Builder
    public static GoogleInfo entityToDto(Map<String, Object> jsonResponse) {

        return GoogleInfo.builder()
            .sub((String) jsonResponse.get("sub"))
            .name((String) jsonResponse.get("name"))
            .given_name((String) jsonResponse.get("given_name"))
            .family_name((String) jsonResponse.get("family_name"))
            .picture((String) jsonResponse.get("picture"))
            .email((String) jsonResponse.get("email"))
            .email_verified((boolean) jsonResponse.get("email_verified"))
            .build();
    }
}
