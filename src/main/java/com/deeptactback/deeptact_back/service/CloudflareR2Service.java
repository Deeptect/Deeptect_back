package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.dto.VideoUploadReqDto;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public interface CloudflareR2Service {
    String uploadVideo(String originalFilename, MultipartFile file, VideoUploadReqDto videoUploadReqDto) throws IOException;
    String uploadShortsVideo(String originalFilename, MultipartFile file, VideoUploadReqDto videoUploadReqDto) throws IOException;
    InputStream downloadVideo(String fileName) throws IOException;
    String uploadVideoBytes(String fileName, byte[] data, String contentType) throws IOException;
}
