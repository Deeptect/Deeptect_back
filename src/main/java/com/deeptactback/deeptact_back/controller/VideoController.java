package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.service.VideoService;
import com.deeptactback.deeptact_back.service.YoutubeShortsFetchService;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;
    private final YoutubeShortsFetchService youtubeShortsFetchService;


    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam("video") MultipartFile video) throws IOException {
        String fileName = videoService.uploadVideo(video.getOriginalFilename(), video);
        return ResponseEntity.ok("파일이 Cloudflare R2에 업로드 되었습니다: " + fileName);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        InputStream inputStream = videoService.downloadVideo(fileName);
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .body(resource);
    }

    @GetMapping("/fetch-shorts")
    public ResponseEntity<List<String>> fetchShorts() {
        try {
            List<String> fileNames = youtubeShortsFetchService.fetchAndStoreYoutubeShorts();
            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            log.error("YouTube 쇼츠 가져오기 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
