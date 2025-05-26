package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.common.OriginType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoShowRespDto {
    private OriginType originType;
    private String title;
    private String description;
    private LocalDateTime uploadTime;
    private String storageUrl;
    private String thumbnailUrl;
    private Boolean isDeepfake;
    private int viewCount;
    private int likeCount;

    public static VideoShowRespDto entityToDto(OriginType originType, String title, String description, LocalDateTime uploadTime, String storageUrl,
        String thumbnailUrl, Boolean isDeepfake, int viewCount, int likeCount) {
        return VideoShowRespDto.builder()
            .originType(originType)
            .title(title)
            .description(description)
            .uploadTime(uploadTime)
            .storageUrl(storageUrl)
            .thumbnailUrl(thumbnailUrl)
            .isDeepfake(isDeepfake)
            .viewCount(viewCount)
            .likeCount(likeCount)
            .build();
    }
}
