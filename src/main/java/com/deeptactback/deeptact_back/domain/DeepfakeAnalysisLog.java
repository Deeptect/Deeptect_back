package com.deeptactback.deeptact_back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeepfakeAnalysisLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String isDeepfake;

    @Column(nullable = false)
    private float detectionScore;

    @Lob
    @Column(nullable = false)
    private String analysisDetail;

    @Column(nullable = false, length = 50)
    private String videoUrl;

    @Column(nullable = false, length = 50)
    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDateTime detectedAt;

    @Builder
    public DeepfakeAnalysisLog(int logId, User user, String title, String isDeepfake, float detectionScore, String analysisDetail, String videoUrl, String thumbnailUrl) {
        this.logId = logId;
        this.user = user;
        this.title = title;
        this.isDeepfake = isDeepfake;
        this.detectionScore = detectionScore;
        this.analysisDetail = analysisDetail != null ? analysisDetail : "empty";
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.detectedAt = LocalDateTime.now();
    }
}
