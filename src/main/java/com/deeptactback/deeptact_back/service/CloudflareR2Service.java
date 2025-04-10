package com.deeptactback.deeptact_back.service;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface CloudflareR2Service {
    String uploadVideo(String originalFilename, MultipartFile file) throws IOException;
    InputStream downloadVideo(String fileName) throws IOException;
    String uploadVideoBytes(String fileName, byte[] data, String contentType) throws IOException;
}
