package com.deeptactback.deeptact_back.vo;

import com.deeptactback.deeptact_back.common.OriginType;
import com.deeptactback.deeptact_back.dto.VideoShowRespDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideoShowRespVo {
    private OriginType originType;
    private String title;
    private String description;
    private LocalDateTime uploadedAt;
    private String videoUrl;
    private String thumbnailUrl;
    private Boolean isDeepfake;
    private int viewCount;
    private int likeCount;

    @Builder
    public VideoShowRespVo(OriginType originType, String title, String description, LocalDateTime uploadedAt, String videoUrl, String thumbnailUrl, Boolean isDeepfake, int viewCount, int likeCount) {
        this.originType = originType;
        this.title = title;
        this.description = description;
        this.uploadedAt = uploadedAt;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.isDeepfake = isDeepfake;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public static VideoShowRespVo dtoToVo(
        VideoShowRespDto videoShowRespDto) {
        return VideoShowRespVo.builder()
            .originType(videoShowRespDto.getOriginType())
            .title(videoShowRespDto.getTitle())
            .description(videoShowRespDto.getDescription())
            .uploadedAt(videoShowRespDto.getUploadedAt())
            .videoUrl(videoShowRespDto.getVideoUrl())
            .thumbnailUrl(videoShowRespDto.getThumbnailUrl())
            .isDeepfake(videoShowRespDto.getIsDeepfake())
            .viewCount(videoShowRespDto.getViewCount())
            .likeCount(videoShowRespDto.getLikeCount())
            .build();
    }
}
