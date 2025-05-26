package com.deeptactback.deeptact_back.domain;

import com.deeptactback.deeptact_back.common.OriginType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int videoId;

    @ManyToOne()
    @JoinColumn(name = "log_id")
    private DeepfakeAnalysisLog deepfakeAnalysisLog;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OriginType originType;

    @Column(length = 50)
    private String youtubeVideoId;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 10)
    private int viewCount;

    @Column(nullable = false, length = 10)
    private int likeCount;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Builder
    public Video(int videoId, DeepfakeAnalysisLog deepfakeAnalysisLog, OriginType originType, String youtubeVideoId, String description, int viewCount, int likeCount, LocalDateTime uploadedAt) {
        this.videoId = videoId;
        this.deepfakeAnalysisLog = deepfakeAnalysisLog;
        this.originType = originType != null ? originType : OriginType.USER;
        this.youtubeVideoId = youtubeVideoId;
        this.description = description;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.uploadedAt = uploadedAt;
    }
}
