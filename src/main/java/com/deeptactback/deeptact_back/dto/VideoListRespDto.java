package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.common.OriginType;
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
    private LocalDateTime uploadTime;
    private IsDeepfake isDeepfake;
    private Float detectionScore;
    private String storageUrl;
    private String thumbnailUrl;
    private int viewCount;
    private int likeCount;

    public static VideoListRespDto entityToDto(Video video) {
        return VideoListRespDto.builder()
            .videoId(video.getVideoId())
            .nickname(
                video.getUser() != null && video.getUser().getNickname() != null ? video.getUser().getNickname() : "ADMIN")
            .originType(video.getOriginType())
            .title(video.getTitle())
            .description(video.getDescription())
            .uploadTime(video.getUploadTime())
            .isDeepfake(video.getIsDeepfake())
            .detectionScore(video.getDetectionScore())
            .storageUrl(video.getStorageUrl())
            .thumbnailUrl(video.getThumbnailUrl())
            .viewCount(video.getViewCount())
            .likeCount(video.getLikeCount())
            .build();
    }
}
