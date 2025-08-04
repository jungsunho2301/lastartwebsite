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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 로그인 필요");
        }

        try {
            System.out.println("📦 Content-Type: " + request.getContentType());
            for (jakarta.servlet.http.Part part : request.getParts()) {
                System.out.println("📦 Part-Name: " + part.getName());
            }

            Artwork saved = artworkService.saveArtwork(image);
            return ResponseEntity.ok(saved);
        } catch (IOException | ServletException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload Failed");
        }

    }

    // ✅ 작품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtwork(@PathVariable Long id, HttpServletRequest request) {

        // ✅ 세션 확인 및 로그 출력
        HttpSession session = request.getSession(false);
        System.out.println("🧪 세션 존재 여부: " + (session != null));
        if (session != null) {
            Object loginAttr = session.getAttribute(SessionConst.LOGIN_ADMIN);
            System.out.println("🧪 세션 loginAdmin 값: " + loginAttr);
        }

        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 로그인 필요");
        }

        if (!artworkRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 작품이 존재하지 않습니다.");
        }

        artworkService.deleteArtwork(id);
        return ResponseEntity.ok("작품 삭제 완료");
    }

    // ✅ 작품 이미지 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtworkImage(@PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {
        // ✅ 세션 확인
        HttpSession session = request.getSession(false);
        System.out.println("🧪 세션 존재 여부: " + (session != null));
        if (session != null) {
            Object loginAttr = session.getAttribute(SessionConst.LOGIN_ADMIN);
            System.out.println("🧪 세션 loginAdmin 값: " + loginAttr);
        }

        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 로그인 필요");
        }

        // ✅ 수정 처리
        if (!artworkRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 작품이 존재하지 않습니다.");
        }

        try {
            Artwork updated = artworkService.updateArtworkImage(id, image);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 수정 실패");
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