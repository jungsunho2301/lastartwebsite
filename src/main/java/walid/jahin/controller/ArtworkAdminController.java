package walid.jahin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.SessionConst;
import java.io.IOException;
import walid.jahin.model.Artwork;
import walid.jahin.repository.ArtworkRepository;
import walid.jahin.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/admin/api/artwork")
@RequiredArgsConstructor
public class ArtworkAdminController {

    private final ArtworkService artworkService;
    private final ArtworkRepository artworkRepository;

    // ✅ 작품 등록
    @PostMapping("/upload")
    public ResponseEntity<?> uploadArtwork(@RequestPart("image") MultipartFile image,
            HttpServletRequest request) {

        // ✅ 세션 확인 및 로그 출력
        HttpSession session = request.getSession(false);
        System.out.println("🧪 Session T/F: " + (session != null));
        if (session != null) {
            Object loginAttr = session.getAttribute(SessionConst.LOGIN_ADMIN);
            System.out.println("🧪 Session loginAdmin Value: " + loginAttr);
        }

        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
        }

        try {
            System.out.println("Content-Type: " + request.getContentType());
            for (jakarta.servlet.http.Part part : request.getParts()) {
                System.out.println("Part-Name: " + part.getName());
            }

            Artwork saved = artworkService.saveArtwork(image);
            return ResponseEntity.ok(saved);

        } catch (javax.net.ssl.SSLHandshakeException e) {
            // ✅ TLS Handshake 실패 구분
            e.printStackTrace(); // 콘솔에 전체 스택
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("UPLOAD_ERROR: SSLHandshakeException - " + e.getMessage());

        } catch (IOException | ServletException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload Failed");
        }

    }

    // ✅ 작품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtwork(@PathVariable Long id, HttpServletRequest request) {

        // ✅ 세션 확인 및 로그 출력
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginAttr = session.getAttribute(SessionConst.LOGIN_ADMIN);
        }

        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
        }

        if (!artworkRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This artwork does not exist.");
        }

        artworkService.deleteArtwork(id);
        return ResponseEntity.ok("Artwork has been deleted.");
    }

    // ✅ 작품 이미지 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtworkImage(@PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {
        // ✅ 세션 확인
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginAttr = session.getAttribute(SessionConst.LOGIN_ADMIN);
        }

        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
        }

        // ✅ 수정 처리
        if (!artworkRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This artwork does not exist.");
        }

        try {
            Artwork updated = artworkService.updateArtworkImage(id, image);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to edit");
        }
    }

    // ✅ 작품 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getArtworkById(@PathVariable Long id) {
        return artworkService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}