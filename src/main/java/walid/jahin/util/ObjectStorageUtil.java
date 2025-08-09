package walid.jahin.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Component
@Slf4j
public class ObjectStorageUtil {

    @Value("${oracle.objectstorage.par.artwork}")
    private String artworkParUrl;

    @Value("${oracle.objectstorage.par.artshop}")
    private String artshopParUrl;

    @Value("${oracle.objectstorage.base-url}")
    private String baseUrl;

    public String uploadArtworkToObjectStorage(MultipartFile file, String prefix) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IOException("파일명이 유효하지 않습니다.");
        }

        // ✅ 파일명 URL 인코딩
        String filename = java.net.URLEncoder.encode(
                java.util.UUID.randomUUID() + "_" + originalFilename,
                java.nio.charset.StandardCharsets.UTF_8);

        // 업로드 대상 PAR URL (artwork 폴더 기준)
        String targetUrl;
        if ("artwork".equals(prefix)) {
            targetUrl = artworkParUrl.endsWith("/") ? artworkParUrl + filename : artworkParUrl + "/" + filename;

        } else if ("artshop".equals(prefix)) {
            targetUrl = artshopParUrl.endsWith("/") ? artshopParUrl + filename : artshopParUrl + "/" + filename;

        } else {
            throw new IllegalArgumentException("유효하지 않은 prefix입니다.");
        }

        // 실제 다운로드 URL (읽기 전용 URL로 DB에 저장)
        String accessUrl;
        if ("artwork".equals(prefix)) {
            accessUrl = artworkParUrl.endsWith("/") ? artworkParUrl + filename : artworkParUrl + "/" + filename;

        } else if ("artshop".equals(prefix)) {
            accessUrl = artshopParUrl.endsWith("/") ? artshopParUrl + filename : artshopParUrl + "/" + filename;

        } else {
            throw new IllegalArgumentException("유효하지 않은 prefix입니다.");
        }

        log.info("🔼 업로드 대상 URL: {}", targetUrl);
        log.info("🔗 저장될 접근 URL: {}", accessUrl);

        // HTTP PUT 요청 전송
        HttpURLConnection connection = (HttpURLConnection) new URL(targetUrl).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", file.getContentType());
        connection.setFixedLengthStreamingMode((int) file.getSize());

        try (var outputStream = connection.getOutputStream()) {
            file.getInputStream().transferTo(outputStream);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200 && responseCode != 201) {
            throw new IOException("파일 업로드 실패: HTTP " + responseCode);
        }

        return accessUrl;
    }
}
