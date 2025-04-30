package com.deeptactback.deeptact_back.domain;

import com.google.api.client.util.DateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int report_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(nullable = false, length = 50)
    private String reason;

    @Column(nullable = false)
    private DateTime reported_at;

    @Column(nullable = false)
    private Boolean is_resolved;

    @Builder
    public VideoReport(User user, Video video, String reason, DateTime reported_at) {
        this.user = user;
        this.video = video;
        this.reason = reason;
        this.reported_at = reported_at;
    }
}
