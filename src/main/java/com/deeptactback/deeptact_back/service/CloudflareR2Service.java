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
    void uploadVideo(MultipartFile video, String title, String category, Boolean attention, Boolean attentionPred, Float attentionOGProb, Float attentionDFProb, Boolean convolution, Boolean convolutionPred, Float convolutionOGProb, Float convolutionDFProb) throws IOException;
    LogRespDto analyzeVideoAttention(MultipartFile video) throws IOException;
    LogRespDto analyzeVideoConvolution(MultipartFile video) throws IOException;
    String uploadShortsVideo(String originalFilename, MultipartFile file, VideoUploadReqDto videoUploadReqDto) throws IOException;
    InputStream downloadVideo(String fileName) throws IOException;
    String uploadVideoBytes(String fileName, byte[] data, String contentType) throws IOException;
    List<LogListRespVo> getUserLogs();
}
