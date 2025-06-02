package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.OriginType;
import com.deeptactback.deeptact_back.domain.DeepfakeAnalysisLog;
import com.deeptactback.deeptact_back.domain.Test;
import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.domain.Video;
import com.deeptactback.deeptact_back.dto.LogRespDto;
import com.deeptactback.deeptact_back.dto.VideoUploadReqDto;
import com.deeptactback.deeptact_back.repository.LogRepository;
import com.deeptactback.deeptact_back.repository.TestRepository;
import com.deeptactback.deeptact_back.repository.UserRepository;
import com.deeptactback.deeptact_back.repository.VideoRepository;
import com.deeptactback.deeptact_back.vo.LogListRespVo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@Slf4j
public class CloudflareR2ServiceImpl implements CloudflareR2Service {

    @Value("${cloudflare.r2.public-url}") String publicUrl;
    private final S3Client r2Client;
    private final String bucketName;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final TestRepository testRepository;

    public CloudflareR2ServiceImpl(
        @Value("${cloudflare.r2.access-key-id}") String accessKeyId,
        @Value("${cloudflare.r2.secret-access-key}") String secretAccessKey,
        @Value("${cloudflare.r2.endpoint}") String endpoint,
        @Value("${cloudflare.r2.bucket-name}") String bucketName, VideoRepository videoRepository,
        UserRepository userRepository, LogRepository logRepository, TestRepository testRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.testRepository = testRepository;

        this.r2Client = S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
            .build();

        this.bucketName = bucketName;
    }

    @Override
    public void uploadVideo(MultipartFile video, String title, String description, Boolean isDeepfake, Float detectionScore) throws IOException {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) auth.getPrincipal();
        User user = userRepository.findByUuid(uuid);

        // 🔸 timestamp + 안전한 파일명 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String baseName = title + "_" + timestamp;
        String videoKey = "video/" + uuid + "/" + baseName + ".mp4";
        String thumbnailKey = "video/" + uuid + "/" + baseName + "_thumb.jpg";

        String videoUrl = publicUrl + videoKey;
        String thumbnailUrl = publicUrl + thumbnailKey;

        // ✅ Cloudflare R2 - 영상 업로드
        PutObjectRequest videoPutRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(videoKey)
            .contentType(video.getContentType())
            .contentLength(video.getSize())
            .build();

        r2Client.putObject(videoPutRequest, RequestBody.fromInputStream(video.getInputStream(), video.getSize()));

        // ✅ 썸네일 생성: 임시 파일 저장 → ffmpeg 실행
        File tempVideo = File.createTempFile("upload_", ".mp4");
        video.transferTo(tempVideo);

        File thumbnailFile = new File(tempVideo.getParent(), baseName + "_thumb.jpg");

        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg", "-i", tempVideo.getAbsolutePath(), "-ss", "00:00:05", "-vframes", "1", thumbnailFile.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();

        // ✅ 썸네일 업로드
        PutObjectRequest thumbPutRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(thumbnailKey)
            .contentType("image/jpeg")
            .contentLength(thumbnailFile.length())
            .build();

        r2Client.putObject(thumbPutRequest, RequestBody.fromFile(thumbnailFile));

        Test testVideo = Test.builder()
            .user(user)
            .title(title)
            .isDeepfake(isDeepfake)
            .detectionScore(detectionScore)
            .videoUrl(videoUrl)
            .thumbnailUrl(thumbnailUrl)
            .uploadedAt(LocalDateTime.now())
            .build();

        testRepository.save(testVideo);

        tempVideo.delete();
        thumbnailFile.delete();

    } catch (S3Exception | InterruptedException e) {
        throw new IOException("Cloudflare R2 업로드 또는 썸네일 생성 실패: " + e.getMessage(), e);
    }
}

    public LogRespDto analyzeVideo(MultipartFile video) throws IOException {

        byte[] videoBytes = video.getBytes();

        // ✅ Deep 모델 분석 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> bodyDirect = new LinkedMultiValueMap<>();
        bodyDirect.add("video", new MultipartInputStreamFileResource(new ByteArrayInputStream(videoBytes), video.getOriginalFilename()));
        HttpEntity<MultiValueMap<String, Object>> requestDirect = new HttpEntity<>(bodyDirect, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseDirect = restTemplate.postForEntity("http://localhost:5000/predict-deep", requestDirect, Map.class);
        Map<String, Object> resultDirect = responseDirect.getBody();

        // ✅ Attention 모델 분석 요청
        MultiValueMap<String, Object> bodyAtt = new LinkedMultiValueMap<>();
        bodyAtt.add("video", new MultipartInputStreamFileResource(new ByteArrayInputStream(videoBytes), video.getOriginalFilename()));
        HttpEntity<MultiValueMap<String, Object>> requestAtt = new HttpEntity<>(bodyAtt, headers);
        ResponseEntity<Map> responseAtt = restTemplate.postForEntity("http://localhost:5000/predict-att", requestAtt, Map.class);
        Map<String, Object> resultAtt = responseAtt.getBody();

        // ✅ 결과 파싱 (null 방지)
        boolean isDeepfakeDirect = resultDirect != null && resultDirect.get("prediction") != null && ((Integer) resultDirect.get("prediction")) == 1;
        float scoreDirect = resultDirect != null && resultDirect.get("probability") != null ? ((Number) resultDirect.get("probability")).floatValue() : -1f;

        boolean isDeepfakeAttention = resultAtt != null && resultAtt.get("prediction") != null && ((Integer) resultAtt.get("prediction")) == 1;
        float scoreAttention = resultAtt != null && resultAtt.get("deepfake_prob") != null ? ((Number) resultAtt.get("deepfake_prob")).floatValue() : -1f;

        // ✅ 로그 저장
        // DeepfakeAnalysisLog log = DeepfakeAnalysisLog.builder()
        //     .user(user)
        //     .title(title)
        //     .isDeepfake(isDeepfakeDirect || isDeepfakeAttention)
        //     .detectionScore(Math.max(scoreDirect, scoreAttention) * 100)
        //     .analysisDetail("분석 완료")
        //     .videoUrl(videoUrl)
        //     .thumbnailUrl("")
        //     .build();

        // logRepository.save(log);
        return LogRespDto.entityToDto(isDeepfakeDirect, scoreDirect, isDeepfakeAttention, scoreAttention);
    }



    public class MultipartInputStreamFileResource extends InputStreamResource {
        private final String filename;

        public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() {
            return -1; // 알 수 없음 (streaming)
        }
    }

    public List<LogListRespVo> getUserLogs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);
        List<DeepfakeAnalysisLog> logs = logRepository.findAllByUser(user);

        return logs.stream()
            .map(log -> LogListRespVo.builder()
                .logId(log.getLogId())
                .title(log.getTitle())
                .videoUrl(log.getVideoUrl())
                .isDeepfake(log.getIsDeepfake())
                .detectionScore(log.getDetectionScore())
                .detectedAt(log.getDetectedAt())
                .build())
            .toList();
    }


    @Override
    public String uploadShortsVideo(String originalFilename, MultipartFile file, VideoUploadReqDto videoUploadReqDto) throws IOException {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = timestamp + "_" + originalFilename;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String uuid = (String) authentication.getPrincipal();
            User user = userRepository.findByUuid(uuid);

            String youtubeVideoId = videoUploadReqDto.getYoutubeVideoId();
            if (youtubeVideoId == null || youtubeVideoId.isBlank()) {
                throw new IllegalArgumentException("YouTube 영상 ID는 필수입니다.");
            }

            Video video = Video.builder()
                .originType(OriginType.ADMIN)
                .youtubeVideoId(youtubeVideoId)
                .description(videoUploadReqDto.getDescription())
                .build();

            videoRepository.save(video);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

            r2Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return fileName;
        } catch (S3Exception e) {
            throw new IOException("Cloudflare R2에 파일 업로드 실패: " + e.getMessage(), e);
        }
    }


    @Override
    public InputStream downloadVideo(String fileName) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

            return r2Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            throw new IOException("Cloudflare R2에서 파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadVideoBytes(String fileName, byte[] data, String contentType) throws IOException {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .contentLength((long) data.length)
                .build();

            r2Client.putObject(putObjectRequest, RequestBody.fromBytes(data));

            return fileName;
        } catch (S3Exception e) {
            throw new IOException("Cloudflare R2에 byte 업로드 실패: " + e.getMessage(), e);
        }
    }

}
