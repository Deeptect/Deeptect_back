package com.deeptactback.deeptact_back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String refreshToken;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @Column(nullable = false)
    private boolean role;

    private String profileImageUrl;


    @Builder
    public User(int user_id,String uuid, String nickname, String email, String password, String refreshToken, boolean isEmailVerified, boolean role, String profileImageUrl) {
        this.user_id = user_id;
        this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
        this.isEmailVerified = isEmailVerified;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }
}
