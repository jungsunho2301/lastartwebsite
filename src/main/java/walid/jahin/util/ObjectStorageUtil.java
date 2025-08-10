package walid.jahin.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@Slf4j
public class ObjectStorageUtil {

    @Value("${oracle.objectstorage.par.artwork}")
    private String artworkParUrl;

    @Value("${oracle.objectstorage.par.artshop}")
    private String artshopParUrl;

    @Value("${oracle.objectstorage.base-url:}")
    private String baseUrl; // 현재는 PAR 접근/업로드 URL 동일하게 사용

    // === Public API ==========================================================
    public String uploadArtworkToObjectStorage(MultipartFile file, String prefix) throws IOException {
        // 1) 파일명 생성 (+ URL 인코딩)
        final String original = requireFilename(file);
        final String encodedName = encodePath(UUID.randomUUID() + "_" + original);

        // 2) 업로드/접근 URL 구성
        final String parBase = switch (prefix) {
            case "artwork" -> artworkParUrl;
            case "artshop" -> artshopParUrl;
            default -> throw new IllegalArgumentException("유효하지 않은 prefix: " + prefix);
        };
        final String uploadUrl = joinUrl(parBase, encodedName);
        final String accessUrl = joinUrl(parBase, encodedName); // PAR 읽기 허용 환경

        log.info("🔼 업로드 대상 URL: {}", uploadUrl);
        log.info("🔗 저장될 접근 URL: {}", accessUrl);

        // 3) HTTPS PUT
        putBinary(uploadUrl, file);

        // 4) 성공 시 접근 URL 반환
        return accessUrl;
    }

    // === Internals ===========================================================

    private void putBinary(String url, MultipartFile file) throws IOException {
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setUseCaches(false);
            conn.setConnectTimeout(15_000);
            conn.setReadTimeout(30_000);

            final String contentType = (file.getContentType() == null || file.getContentType().isBlank())
                    ? "application/octet-stream" : file.getContentType();
            conn.setRequestProperty("Content-Type", contentType);

            // 파일 크기: long 모드 (대용량 안전)
            long len = file.getSize();
            if (len >= 0) conn.setFixedLengthStreamingMode(len);

            // 전송
            try (InputStream in = file.getInputStream();
                 var out = conn.getOutputStream()) {
                in.transferTo(out);
                out.flush();
            }

            final int status = conn.getResponseCode();
            final String msg = conn.getResponseMessage();

            String body = readBodySafely(conn);
            log.info("OCI PUT status={} msg='{}' bodyLen={}", status, msg, body == null ? 0 : body.length());

            if (status / 100 != 2) {
                // 실패 본문도 로그
                log.error("OCI PUT failed. status={} msg='{}' body={}", status, msg, body);
                throw new IOException("OCI upload failed: HTTP " + status + " " + (msg == null ? "" : msg));
            }
        } catch (SSLHandshakeException e) {
            // 👉 컨트롤러에서 별도 502로 내려가도록 그대로 전파
            log.error("TLS handshake to OCI failed: {}", e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            log.error("I/O during OCI PUT failed: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during OCI PUT", e);
            throw new IOException("Unexpected error during OCI PUT", e);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String readBodySafely(HttpsURLConnection conn) {
        try (InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream()) {
            if (is == null) return null;
            // 너무 큰 본문은 잘라서 로그 (최대 4KB)
            byte[] buf = is.readNBytes(4096);
            return new String(buf, StandardCharsets.UTF_8);
        } catch (Exception ignore) {
            return null;
        }
    }

    private String requireFilename(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        if (name == null || name.isBlank()) throw new IOException("파일명이 유효하지 않습니다.");
        return name;
    }

    private String encodePath(String segment) {
        // URLEncoder는 스페이스를 '+'로 인코딩 → 경로 컴포넌트에는 %20이 더 안전
        return URLEncoder.encode(segment, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String joinUrl(String base, String segment) {
        if (base.endsWith("/")) return base + segment;
        return base + "/" + segment;
    }
}
