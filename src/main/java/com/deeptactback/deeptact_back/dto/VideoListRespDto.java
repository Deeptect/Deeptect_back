package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.common.OriginType;
import com.deeptactback.deeptact_back.domain.Test;
import com.deeptactback.deeptact_back.domain.Video;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoListRespDto {
    private int testId;
    private String title;
    private String videoUrl;
    private String thumbnailUrl;
    private LocalDateTime uploadedAt;
    private Boolean attention;
    private Boolean attentionPred;
    private Float attentionOgProb;
    private Float attentionDfProb;
    private Boolean convolution;
    private Boolean convolutionPred;
    private Float convolutionOgProb;
    private Float convolutionDfProb;

    public static VideoListRespDto entityToDto(Test test) {
        return VideoListRespDto.builder()
            .testId(test.getTestId())
            .title(test.getTitle())
            .videoUrl(test.getVideoUrl())
            .thumbnailUrl(test.getThumbnailUrl())
            .uploadedAt(test.getUploadedAt())
            .attention(test.getAttention())
            .attentionPred(test.getAttention_pred())
            .attentionOgProb(test.getAttention_og_prob())
            .attentionDfProb(test.getAttention_df_prob())
            .convolution(test.getConvolution())
            .convolutionPred(test.getConvolution_pred())
            .convolutionOgProb(test.getConvolution_og_prob())
            .convolutionDfProb(test.getConvolution_df_prob())
            .build();
    }
}