package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.dto.LogRespDto;
import lombok.Builder;
import lombok.Data;

@Data
public class LogRespVo {
    private Boolean isDeepfakeDirect;
    private Float scoreDirect;
    private Boolean isDeepfakeAttention;
    private Float scoreAttention;

    @Builder
    public LogRespVo(Boolean isDeepfakeDirect, Float scoreDirect, Boolean isDeepfakeAttention, Float scoreAttention) {
        this.isDeepfakeDirect = isDeepfakeDirect;
        this.scoreDirect = scoreDirect;
        this.isDeepfakeAttention = isDeepfakeAttention;
        this.scoreAttention = scoreAttention;
    }

    public static LogRespVo dtoToVo(LogRespDto logRespDto) {
        return LogRespVo.builder()
            .isDeepfakeDirect(logRespDto.getIsDeepfakeDirect())
            .scoreDirect(logRespDto.getScoreDirect())
            .isDeepfakeAttention(logRespDto.getIsDeepfakeAttention())
            .scoreAttention(logRespDto.getScoreAttention())
            .build();
    }
}
