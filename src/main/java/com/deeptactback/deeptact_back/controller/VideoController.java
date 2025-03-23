package com.deeptactback.deeptact_back.controller;

import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.dto.VideoReqDto;
import com.deeptactback.deeptact_back.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute VideoReqDto videoReqDto, @RequestParam("file") MultipartFile file) {
        log.info("파일 업로드 요청 수신: {}", file.getOriginalFilename());

        // 파일이 비어있지 않은지 확인
        if (file.isEmpty()) {
            log.error("업로드할 파일이 없습니다.");
            return "redirect:/files/upload?error=파일이 비어있습니다."; // 오류 메시지 전달
        }

        // 파일 업로드 서비스 호출
        try {
            CMResponse response = videoService.uploadVideo(file);
            log.info("파일 업로드 성공: {}", response);
            return "redirect:/files"; // 성공 시 파일 목록 페이지로 리다이렉트
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            return "redirect:/files/upload?error=" + e.getMessage(); // 오류 발생 시 업로드 페이지로 리다이렉트
        }
    }

}
