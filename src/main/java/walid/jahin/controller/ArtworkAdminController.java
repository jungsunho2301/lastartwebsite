package walid.jahin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.Artwork;
import walid.jahin.model.SessionConst;
import walid.jahin.repository.ArtworkRepository;
import walid.jahin.service.ArtworkService;

import java.io.IOException;

@RestController
@RequestMapping("/admin/api/artworks")
@RequiredArgsConstructor
public class ArtworkAdminController {

    private final ArtworkService artworkService;
    private final ArtworkRepository artworkRepository;

    // ✅ 작품 등록
    @PostMapping("/upload")
    public ResponseEntity<?> uploadArtwork(@RequestParam("title") String title,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") int price,
                                           @RequestParam("forSale") boolean forSale,
                                           @RequestParam("image") MultipartFile image,
                                           HttpServletRequest request) {

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

        try {
            Artwork saved = artworkService.saveArtwork(title, description, price, forSale, image);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
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
}
