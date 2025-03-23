package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.BaseResponseStatus;
import com.deeptactback.deeptact_back.common.CMResponse;
import com.deeptactback.deeptact_back.domain.Video;
import com.deeptactback.deeptact_back.repository.VideoRepository;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final String uploadDir = "C:\\git\\deeptect_back\\deeptact_back\\src\\main\\resources\\uploads\\";

    @Override
    @Transactional // readOnly = true 제거
    public CMResponse uploadVideo(MultipartFile file) {
        BaseResponseStatus status;

        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists() && !uploadDirFile.mkdirs()) {
            log.error("업로드 디렉토리 생성 실패: {}", uploadDir);
            return CMResponse.fail(BaseResponseStatus.FORBIDDEN.getCode(), BaseResponseStatus.FORBIDDEN, null);
        }

        String fileName = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        File dest = new File(uploadDir + uniqueFileName);

        try {
            file.transferTo(dest);
            log.info("파일이 성공적으로 저장되었습니다: {}", dest.getAbsolutePath());

            Video video = Video.builder()
                .name(uniqueFileName)
                .filePath(dest.getAbsolutePath())
                .build();

            log.info("비디오 객체 생성됨: {}", video);
            videoRepository.save(video);
            log.info("비디오 정보가 DB에 저장되었습니다.");

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, null);
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            status = BaseResponseStatus.FORBIDDEN;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }


}
