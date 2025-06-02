package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.LogRespDto;
import com.deeptactback.deeptact_back.dto.VideoUploadReqDto;
import com.deeptactback.deeptact_back.vo.LogListRespVo;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public interface CloudflareR2Service {
    void uploadVideo(MultipartFile video, String title, String description, Boolean isDeepfake, Float detectionScore) throws IOException;
    // void uploadVideo(MultipartFile video, int logId, String description) throws IOException;
    LogRespDto analyzeVideo(MultipartFile video) throws IOException;
    String uploadShortsVideo(String originalFilename, MultipartFile file, VideoUploadReqDto videoUploadReqDto) throws IOException;
    InputStream downloadVideo(String fileName) throws IOException;
    String uploadVideoBytes(String fileName, byte[] data, String contentType) throws IOException;
    List<LogListRespVo> getUserLogs();
}
