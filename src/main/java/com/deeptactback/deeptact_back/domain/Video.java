package com.deeptactback.deeptact_back.domain;

import com.deeptactback.deeptact_back.common.IsDeepfake;
import com.deeptactback.deeptact_back.common.OriginType;
import com.google.api.client.util.DateTime;
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
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OriginType originType;

    @Column(length = 50)
    private String youtubeVideoId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime uploadTime;

    @Column(nullable = false)
    private String storageUrl;

    @Column(nullable = false, length = 50)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private IsDeepfake isDeepfake;

    private float detectionScore;

    private LocalDateTime detectionTime;

    @Column(nullable = false, length = 10)
    private int viewCount;

    @Column(nullable = false, length = 10)
    private int likeCount;

    @Builder
    public Video(User user, String title, int viewCount, LocalDateTime uploadTime, OriginType originType, String youtubeVideoId,
        String description, String storageUrl, String thumbnailUrl, IsDeepfake isDeepfake, float detectionScore, LocalDateTime detectionTime, int likeCount) {
        this.user = user;
        this.originType = originType;
        this.youtubeVideoId = youtubeVideoId;
        this.title = title;
        this.description = description;
        this.uploadTime = uploadTime;
        this.storageUrl = storageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.isDeepfake = isDeepfake;
        this.detectionScore = detectionScore;
        this.detectionTime = detectionTime;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}
