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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int testId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean isDeepfake;

    @Column(nullable = false)
    private float detectionScore;

    @Column(nullable = false, length = 255)
    private String videoUrl;

    @Column(nullable = false, length = 255)
    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Builder
    public Test(User user, String title, Boolean isDeepfake, float detectionScore, String videoUrl, String thumbnailUrl, LocalDateTime uploadedAt) {
        this.user = user;
        this.title = title;
        this.isDeepfake = isDeepfake;
        this.detectionScore = detectionScore;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.uploadedAt = uploadedAt != null ? uploadedAt : LocalDateTime.now();
    }
}
