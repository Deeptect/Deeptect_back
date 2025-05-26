package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogRespDto {
    Boolean isDeepfake;
    Float detectionScore;

    public static LogRespDto entityToDto(Boolean isDeepfake, Float detectionScore) {
        return LogRespDto.builder()
            .isDeepfake(isDeepfake)
            .detectionScore(detectionScore)
            .build();
    }
}
