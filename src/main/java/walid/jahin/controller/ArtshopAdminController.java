package walid.jahin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.SessionConst;
import walid.jahin.model.Artshop;
import walid.jahin.repository.ArtshopRepository;
import walid.jahin.service.ArtshopService;

import java.io.IOException;

@RestController
@RequestMapping("/admin/api/artshop")
@RequiredArgsConstructor
public class ArtshopAdminController {

    private final ArtshopService artshopService;
    private final ArtshopRepository artshopRepository;

    // ✅ 작품 등록
    @PostMapping("/upload")
    public ResponseEntity<?> uploadArtshop(@RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") Integer price,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {

        // ✅ 세션 확인
        HttpSession session = request.getSession(false);
        System.out.println("🧪 세션 존재 여부: " + (session != null));
        if (session != null) {
            System.out.println("🧪 세션 loginAdmin 값: " + session.getAttribute(SessionConst.LOGIN_ADMIN));
        }

        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 로그인 필요");
        }

        try {
            Artshop saved = artshopService.saveArtshop(title, description, price, image);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
        }
    }

    // ✅ 작품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtshop(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 로그인 필요");
        }

        if (!artshopRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 작품이 존재하지 않습니다.");
        }

        artshopService.deleteArtshop(id);
        return ResponseEntity.ok("작품 삭제 완료");
    }

    // ✅ 작품 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtshop(@PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) MultipartFile image,
            HttpServletRequest request) {

        // ✅ 세션 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 로그인 필요");
        }

        if (!artshopRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 작품이 존재하지 않습니다.");
        }

        try {
            Artshop updated = artshopService.updateArtshop(id, title, description, price, image);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
        }
    }

    // ✅ 작품 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getArtshopById(@PathVariable Long id) {
        return artshopService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
