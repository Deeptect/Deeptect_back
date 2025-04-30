package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.VideoListRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoService {
    Page<VideoListRespDto> getAllVideos(Pageable pageable);
}
