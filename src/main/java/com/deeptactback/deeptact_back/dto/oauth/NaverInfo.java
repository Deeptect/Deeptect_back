package com.deeptactback.deeptact_back.dto.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class NaverInfo {
    private String id;
    private String nickname;
    private String profile_image;
    private String age;
    private String gender;
    private String email;
    private String mobile;
    private String mobile_e164;
    private String name;
    private String birthday;
    private String birthyear;

    @Builder
    public static NaverInfo entityToDto(Map<String, Object> jsonResponse) {

        return NaverInfo.builder()
            .id((String) jsonResponse.get("id"))
            .nickname((String) jsonResponse.get("nickname"))
            .profile_image((String) jsonResponse.get("profile_image"))
            .age((String) jsonResponse.get("age"))
            .gender((String) jsonResponse.get("gender"))
            .email((String) jsonResponse.get("email"))
            .mobile((String) jsonResponse.get("mobile"))
            .mobile_e164((String) jsonResponse.get("mobile_e164"))
            .name((String) jsonResponse.get("name"))
            .birthday((String) jsonResponse.get("birthday"))
            .birthyear((String) jsonResponse.get("birthyear"))
            .build();
    }
}
