package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.dto.VideoUploadRequestDto;
import com.deeptactback.deeptact_back.service.CloudflareR2Service;
import com.deeptactback.deeptact_back.service.YoutubeShortsFetchService;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private final CloudflareR2Service cloudflareR2Service;
    private final YoutubeShortsFetchService youtubeShortsFetchService;

    // cloudflare 업로드
    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CMResponse<String> uploadFile(@RequestParam("video") MultipartFile video, @RequestBody VideoUploadRequestDto videoUploadRequestDto){
        try {
            String fileName = cloudflareR2Service.uploadVideo(video.getOriginalFilename(), video, videoUploadRequestDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, fileName);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (IOException e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        InputStream inputStream = cloudflareR2Service.downloadVideo(fileName);
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .body(resource);
    }

    // YT 쇼츠 fetch
    @GetMapping("/fetch-shorts")
    public CMResponse<List<String>> fetchShorts(){
        try {
            List<String> fileNames = youtubeShortsFetchService.fetchAndStoreYoutubeShorts();
            return CMResponse.success(BaseResponseStatus.SUCCESS, fileNames);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (IOException | GeneralSecurityException e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
