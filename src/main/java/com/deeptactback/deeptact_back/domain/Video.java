package com.deeptactback.deeptact_back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
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
    private Long video_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int view;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String category;

    private String videoImageUrl;

    @Column(nullable = false)
    private String videoUrl;

    @Builder
    public Video(Long video_id, String title, int view, LocalDate date, String category, String videoImageUrl, String videoUrl) {
        this.video_id = video_id;
        this.title = title;
        this.view = view;
        this.date = date;
        this.category = category;
        this.videoImageUrl = videoImageUrl;
        this.videoUrl = videoUrl;
    }
}
