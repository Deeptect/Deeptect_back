package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.common.BaseException;
import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.dto.LogRespDto;
import com.deeptactback.deeptact_back.dto.VideoListRespDto;
import com.deeptactback.deeptact_back.dto.VideoUploadReqDto;
import com.deeptactback.deeptact_back.service.CloudflareR2Service;
import com.deeptactback.deeptact_back.service.VideoService;
import com.deeptactback.deeptact_back.service.YoutubeShortsFetchService;
import com.deeptactback.deeptact_back.vo.LogRespVo;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final VideoService videoService;

    @GetMapping("/videos")
    public CMResponse<Page<VideoListRespDto>> getVideos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());
            return CMResponse.success(BaseResponseStatus.SUCCESS, videoService.getAllVideos(pageable));
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
    }

    @PostMapping(path = "/analysis/attention", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CMResponse<LogRespVo> analysisVideoAtt(
        @RequestPart("video") MultipartFile video) {
        try {
            LogRespDto logRespDto = cloudflareR2Service.analyzeVideoAttention(video);
            LogRespVo logRespVo = LogRespVo.dtoToVo(logRespDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, logRespVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/analysis/convolution", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CMResponse<LogRespVo> analysisVideoConv(
        @RequestPart("video") MultipartFile video) {
        try {
            LogRespDto logRespDto = cloudflareR2Service.analyzeVideoConvolution(video);
            LogRespVo logRespVo = LogRespVo.dtoToVo(logRespDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, logRespVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 업로드
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CMResponse<Void> uploadFile(
        @RequestParam("video") MultipartFile video,
        @RequestParam("title") String title,
        @RequestParam("category") String category,
        @RequestParam("attention") Boolean attention,
        @RequestParam("attention_pred") Boolean attentionPred,
        @RequestParam("attention_og_prob") Float attentionOGProb,
        @RequestParam("attention_df_prob") Float attentionDFProb,
        @RequestParam("convolution") Boolean convolution,
        @RequestParam("convolution_pred") Boolean convolutionPred,
        @RequestParam("convolution_og_prob") Float convolutionOGProb,
        @RequestParam("convolution_df_prob") Float convolutionDFProb
    ) {
        try {
            cloudflareR2Service.uploadVideo(
                video, title, category,
                attention, attentionPred, attentionOGProb, attentionDFProb,
                convolution, convolutionPred, convolutionOGProb, convolutionDFProb
            );
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (IOException e) {
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
