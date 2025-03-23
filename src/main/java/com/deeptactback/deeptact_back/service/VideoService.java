package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.CMResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    // 영상 업로드
    CMResponse uploadVideo(MultipartFile file);
}
