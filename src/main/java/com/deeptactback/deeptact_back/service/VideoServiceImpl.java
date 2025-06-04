package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.VideoListRespDto;
import com.deeptactback.deeptact_back.repository.VideoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import com.deeptactback.deeptact_back.repository.TestRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final TestRepository testRepository;

    @Override
    public Page<VideoListRespDto> getAllVideos(Pageable pageable) {
        return testRepository.findAll(pageable)
            .map(VideoListRespDto::entityToDto);
    }
}
