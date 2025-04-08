package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.CMResponse;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    String uploadVideo(String originalFilename, MultipartFile file) throws IOException;
    InputStream downloadVideo(String fileName) throws IOException;
    String uploadVideoBytes(String fileName, byte[] data, String contentType) throws IOException;
}
