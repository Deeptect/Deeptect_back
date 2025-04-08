package com.deeptactback.deeptact_back.dto.oauth;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
public class KakaoAccount {
    private Profile profile;
    private Boolean profileNicknameNeedsAgreement;
    private Boolean hasEmail;
    private Boolean emailNeedsAgreement;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;
    private String email;

    @Data
    @ToString
    public static class Profile {
        private String nickname;
        private Boolean isDefaultNickname;

        @Builder
        public Profile(String nickname, Boolean isDefaultNickname) {
            this.nickname = nickname;
            this.isDefaultNickname = isDefaultNickname;
        }
    }

    @Builder
    public KakaoAccount(
        Profile profile,
        Boolean profileNicknameNeedsAgreement,
        Boolean hasEmail,
        Boolean emailNeedsAgreement,
        Boolean isEmailValid,
        Boolean isEmailVerified,
        String email
    ) {
        this.profile = profile;
        this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
        this.hasEmail = hasEmail;
        this.emailNeedsAgreement = emailNeedsAgreement;
        this.isEmailValid = isEmailValid;
        this.isEmailVerified = isEmailVerified;
        this.email = email;
    }
}
