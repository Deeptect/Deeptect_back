package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.domain.Video;
import com.deeptactback.deeptact_back.dto.VideoListRespDto;
import com.deeptactback.deeptact_back.repository.VideoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Override
    public Page<VideoListRespDto> getAllVideos(Pageable pageable) {
        return videoRepository.findAll(pageable)
            .map(VideoListRespDto::entityToDto);
    }
}
