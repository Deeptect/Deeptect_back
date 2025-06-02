package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.dto.LogRespDto;
import lombok.Builder;
import lombok.Data;

@Data
public class LogRespVo {
    private String model;
    private Boolean prediction;
    private Float original_prob;
    private Float deepfake_prob;

    @Builder
    public LogRespVo(String model, Boolean prediction, Float original_prob,  Float deepfake_prob) {
        this.model = model;
        this.prediction = prediction;
        this.original_prob = original_prob;
        this.deepfake_prob = deepfake_prob;
    }

    public static LogRespVo dtoToVo(LogRespDto logRespDto) {
        return LogRespVo.builder()
            .model(logRespDto.getModel())
            .prediction(logRespDto.getPrediction())
            .original_prob(logRespDto.getOriginalProb())
            .deepfake_prob(logRespDto.getDeepfakeProb())
            .build();
    }
}
