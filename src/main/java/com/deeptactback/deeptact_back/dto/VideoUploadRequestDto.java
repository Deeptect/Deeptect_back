package com.deeptactback.deeptact_back.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class VideoUploadRequestDto {
    private String title;
    private String category;
    private String videoImageUrl;
    private String videoUrl;
}
