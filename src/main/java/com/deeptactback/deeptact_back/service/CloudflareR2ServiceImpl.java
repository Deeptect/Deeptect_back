package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.IsDeepfake;
import com.deeptactback.deeptact_back.common.OriginType;
import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.domain.Video;
import com.deeptactback.deeptact_back.dto.VideoUploadReqDto;
import com.deeptactback.deeptact_back.repository.UserRepository;
import com.deeptactback.deeptact_back.repository.VideoRepository;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

    public CloudflareR2ServiceImpl(
        @Value("${cloudflare.r2.access-key-id}") String accessKeyId,
        @Value("${cloudflare.r2.secret-access-key}") String secretAccessKey,
        @Value("${cloudflare.r2.endpoint}") String endpoint,
        @Value("${cloudflare.r2.bucket-name}") String bucketName, VideoRepository videoRepository,
        UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;

        this.r2Client = S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
            .build();

        this.bucketName = bucketName;
    }

    @Override
    public String uploadVideo(String originalFilename, MultipartFile file, VideoUploadReqDto videoUploadReqDto) throws IOException {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = timestamp + "_" + originalFilename;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String uuid = (String) authentication.getPrincipal();

            User user = userRepository.findByUuid(uuid);

            Video video = Video.builder()
                .user(user)
                .originType(OriginType.USER)
                .title(videoUploadReqDto.getTitle())
                .description(videoUploadReqDto.getDescription())
                .uploadTime(LocalDateTime.now())
                .storageUrl(publicUrl + fileName)
                .thumbnailUrl(videoUploadReqDto.getThumbnailUrl())
                .isDeepfake(null) // 추후 변경 필요
                .detectionScore(0) // 추후 변경 필요
                .detectionTime(null) // 추후 변경 필요
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
                .user(user)
                .originType(OriginType.ADMIN)
                .youtubeVideoId(youtubeVideoId)
                .title(videoUploadReqDto.getTitle())
                .description(videoUploadReqDto.getDescription())
                .uploadTime(LocalDateTime.now())
                .storageUrl(publicUrl + fileName)
                .thumbnailUrl(videoUploadReqDto.getThumbnailUrl())
                .isDeepfake(null)
                .detectionScore(0)
                .detectionTime(null)
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
