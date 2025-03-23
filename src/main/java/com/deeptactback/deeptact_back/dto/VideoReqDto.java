package com.deeptactback.deeptact_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoReqDto {

    private String name;
    private String file_path;

    @Builder
    public VideoReqDto(String name, String file_path) {
        this.name = name;
        this.file_path = file_path;
    }
}
