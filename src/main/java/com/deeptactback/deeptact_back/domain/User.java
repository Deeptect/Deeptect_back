package com.deeptactback.deeptact_back.domain;

import com.deeptactback.deeptact_back.common.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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
    private int userId;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String refreshToken;

    private String profileImageUrl;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    @Builder
    public User(int user_id, String uuid, String email, String password, String nickname,
        String refreshToken, String profileImageUrl, boolean isEmailVerified, Role role, LocalDateTime createdAt) {
        this.userId = user_id;
        this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
        this.profileImageUrl = profileImageUrl;
        this.isEmailVerified = isEmailVerified;
        this.role = role != null ? role : Role.USER;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}
