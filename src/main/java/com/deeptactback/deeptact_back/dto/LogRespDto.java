package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogRespDto {
    private Boolean isDeepfakeDirect;
    private Float scoreDirect;
    private Boolean isDeepfakeAttention;
    private Float scoreAttention;

    public static LogRespDto entityToDto(Boolean isDeepfakeDirect, Float scoreDirect, Boolean isDeepfakeAttention, Float scoreAttention) {
        return LogRespDto.builder()
            .isDeepfakeDirect(isDeepfakeDirect)
            .scoreDirect(scoreDirect)
            .isDeepfakeAttention(isDeepfakeAttention)
            .scoreAttention(scoreAttention)
            .build();
    }
}
