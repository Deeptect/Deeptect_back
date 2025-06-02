package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogRespDto {
    private String model;
    private Boolean prediction;
    private Float original_prob;
    private Float deepfake_prob;

    public static LogRespDto entityToDto(String model, Boolean prediction, Float original_prob, Float deepfake_prob) {
        return LogRespDto.builder()
            .model(model)
            .prediction(prediction)
            .original_prob(original_prob)
            .deepfake_prob(deepfake_prob)
            .build();
    }
}
