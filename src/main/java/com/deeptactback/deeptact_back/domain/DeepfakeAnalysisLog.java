package com.deeptactback.deeptact_back.domain;

import com.google.api.client.util.DateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
    private int log_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "video_id")
    private Video video;

    @Lob
    @Column(nullable = false)
    private String analysis_detail;

    @Column(nullable = false)
    private DateTime created_at;

    @Builder
    public DeepfakeAnalysisLog(Video video, String analysis_detail, DateTime created_at) {
        this.video = video;
        this.analysis_detail = analysis_detail;
        this.created_at = created_at;
    }
}
