package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.dto.LogRespDto;
import lombok.Builder;

public class LogRespVo {
    private Boolean isDeepfake;
    private Float detectionScore;

    @Builder
    public LogRespVo(Boolean isDeepfake, Float detectionScore) {
        this.isDeepfake = isDeepfake;
        this.detectionScore = detectionScore;
    }

    public static LogRespVo dtoToVo(LogRespDto logRespDto) {
        return LogRespVo.builder()
            .isDeepfake(logRespDto.getIsDeepfake())
            .detectionScore(logRespDto.getDetectionScore())
            .build();
    }
}
