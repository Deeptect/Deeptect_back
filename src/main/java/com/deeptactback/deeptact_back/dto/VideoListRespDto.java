package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.common.OriginType;
import com.deeptactback.deeptact_back.domain.DeepfakeAnalysisLog;
import com.deeptactback.deeptact_back.domain.Video;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoListRespDto {
    private int videoId;
    private String nickname;
    private OriginType originType;
    private String title;
    private String description;
    private LocalDateTime uploadedAt;
    private Boolean isDeepfake;
    private Float detectionScore;
    private String videoUrl;
    private String thumbnailUrl;
    private int viewCount;
    private int likeCount;

    public static VideoListRespDto entityToDto(Video video, DeepfakeAnalysisLog analysisLog) {
        return VideoListRespDto.builder()
            .videoId(video.getVideoId())
            .nickname(
                analysisLog.getUser() != null && analysisLog.getUser().getNickname() != null ? analysisLog.getUser().getNickname() : "ADMIN")
            .originType(video.getOriginType())
            .title(analysisLog.getTitle())
            .description(video.getDescription())
            .uploadedAt(video.getUploadedAt())
            .isDeepfake(analysisLog.getIsDeepfake())
            .detectionScore(analysisLog.getDetectionScore())
            .videoUrl(analysisLog.getVideoUrl())
            .thumbnailUrl(analysisLog.getThumbnailUrl())
            .viewCount(video.getViewCount())
            .likeCount(video.getLikeCount())
            .build();
    }
}
