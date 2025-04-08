package com.deeptactback.deeptact_back.dto.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class KakaoInfo {
    private Long id;
    private String connectedAt;
    private String nickname;
    private Boolean profileNicknameNeedsAgreement;
    private Boolean isDefaultNickname;
    private Boolean hasEmail;
    private Boolean emailNeedsAgreement;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;
    private String email;

    @Builder
    public static KakaoInfo entityToDto(Map<String, Object> jsonResponse, Map<String, Object> kakaoAccount) {
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return KakaoInfo.builder()
            .id((Long) jsonResponse.get("id"))
            .connectedAt((String) jsonResponse.get("connected_at"))
            .nickname((String) profile.get("nickname"))
            .profileNicknameNeedsAgreement((Boolean) kakaoAccount.get("profile_nickname_needs_agreement"))
            .isDefaultNickname((Boolean) profile.get("is_default_nickname"))
            .hasEmail((Boolean) kakaoAccount.get("has_email"))
            .emailNeedsAgreement((Boolean) kakaoAccount.get("email_needs_agreement"))
            .isEmailValid((Boolean) kakaoAccount.get("is_email_valid"))
            .isEmailVerified((Boolean) kakaoAccount.get("is_email_verified"))
            .email((String) kakaoAccount.get("email"))
            .build();
    }
}
