package com.deeptactback.deeptact_back.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LogListRespVo {
    private int logId;
    private String title;
    private String videoUrl;
    private boolean isUploaded;
    private LocalDateTime detectedAt;
    private boolean isDeepfake;
    private double detectionScore;
}
