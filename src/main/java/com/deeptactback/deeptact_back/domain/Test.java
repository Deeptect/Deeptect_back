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
    private Long view;

    @Column(nullable = false)
    private String category;

    private Boolean attention;

    @Column(nullable = false)
    private Boolean attention_pred;

    @Column(nullable = false)
    private float attention_og_prob;

    @Column(nullable = false)
    private float attention_df_prob;

    private Boolean convolution;

    @Column(nullable = false)
    private Boolean convolution_pred;

    @Column(nullable = false)
    private float convolution_og_prob;

    @Column(nullable = false)
    private float convolution_df_prob;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Builder
    public Test(
        User user,
        String title,
        Long view,
        String category,
        Boolean attention,
        Boolean attention_pred,
        float attention_og_prob,
        float attention_df_prob,
        Boolean convolution,
        Boolean convolution_pred,
        float convolution_og_prob,
        float convolution_df_prob,
        String videoUrl,
        String thumbnailUrl,
        LocalDateTime uploadedAt
    ) {
        this.user = user;
        this.title = title;
        this.view = view;
        this.category = category;
        this.attention = attention;
        this.attention_pred = attention_pred;
        this.attention_og_prob = attention_og_prob;
        this.attention_df_prob = attention_df_prob;
        this.convolution = convolution;
        this.convolution_pred = convolution_pred;   
        this.convolution_og_prob = convolution_og_prob;
        this.convolution_df_prob = convolution_df_prob;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.uploadedAt = uploadedAt != null ? uploadedAt : LocalDateTime.now();
    }
}
