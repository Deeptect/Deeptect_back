package com.deeptactback.deeptact_back.dto;

import com.deeptactback.deeptact_back.common.OriginType;
import lombok.Data;

@Data
public class VideoUploadReqDto {
    private String title;
    private String description;
    private OriginType originType;
    private String thumbnailUrl;
    private String youtubeVideoId;
}
