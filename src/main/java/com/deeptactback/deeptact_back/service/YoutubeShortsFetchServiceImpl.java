package com.deeptactback.deeptact_back.service;

import com.deeptactback.deeptact_back.common.OriginType;
import com.deeptactback.deeptact_back.domain.User;
import com.deeptactback.deeptact_back.domain.Video;
import com.deeptactback.deeptact_back.repository.UserRepository;
import com.deeptactback.deeptact_back.repository.VideoRepository;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class YoutubeShortsFetchServiceImpl implements YoutubeShortsFetchService {

    private final CloudflareR2Service cloudflareR2Service;

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Youtube Shorts Fetcher";
    private static final long MAX_RESULTS = 10; // 영상 개수

    public List<String> fetchAndStoreYoutubeShorts() throws IOException, GeneralSecurityException {
        List<String> storedFileNames = new ArrayList<>();
        List<SearchResult> shortVideos = searchShorts();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();
        User user = userRepository.findByUuid(uuid);

        for (SearchResult video : shortVideos) {
            String videoId = video.getId().getVideoId();
            String title = video.getSnippet().getTitle();
            String description = video.getSnippet().getDescription();
            String thumbnailUrl = video.getSnippet().getThumbnails().getDefault().getUrl();

            try {
                String fileName = streamVideoDirectlyToR2(videoId, title);
                if (fileName != null) {
                    String storageUrl = publicUrl + fileName;

                    Video entity = Video.builder()
                        .originType(OriginType.ADMIN)
                        .youtubeVideoId(videoId)
                        .description(description)
                        .build();

                    videoRepository.save(entity);
                    storedFileNames.add(fileName);
                }
            } catch (Exception e) {
                log.error("YouTube 쇼츠 처리 실패: {} - {}", videoId, title, e);
            }
        }

        return storedFileNames;
    }


    public List<SearchResult> searchShorts() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        YouTube youtube = new YouTube.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build();

        YouTube.Search.List request = youtube.search().list("id,snippet");
        request.setKey(apiKey);
        request.setQ("#shorts");
        request.setType("video");
        request.setVideoDuration("short");
        request.setMaxResults(MAX_RESULTS);

        SearchListResponse response = request.execute();
        return response.getItems();
    }

    public String streamVideoDirectlyToR2(String videoId, String title) throws IOException, InterruptedException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String safeTitle = title.replaceAll("[^a-zA-Z0-9가-힣]", "_");
        if (safeTitle.length() > 30) {
            safeTitle = safeTitle.substring(0, 30);
        }
        String fileName = timestamp + "_" + videoId + "_" + safeTitle + ".mp4";

        ProcessBuilder processBuilder = new ProcessBuilder(
            "yt-dlp",
            "-f", "mp4",
            "-o", "-",
            "https://youtube.com/watch?v=" + videoId
        );

        Process process = processBuilder.start();

        try (InputStream videoStream = process.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            videoStream.transferTo(buffer);
            byte[] videoBytes = buffer.toByteArray();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("yt-dlp failed with exit code: " + exitCode);
            }

            // byte 기반 영상 업로드
            cloudflareR2Service.uploadVideoBytes(fileName, videoBytes, "video/mp4");

            log.info("YouTube 쇼츠 업로드 완료: {}", fileName);
            return fileName;
        }
    }
}
