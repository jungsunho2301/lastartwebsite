package walid.jahin.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;
import walid.jahin.model.Artwork;
import walid.jahin.service.ArtworkService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;

    // 조회 + 정렬 (latest, lowprice, highprice)
    @GetMapping("/artwork")
    public ResponseEntity<?> getPagedArtworkItems(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,     // 0부터 시작
            @RequestParam(defaultValue = "20") int size     // 페이지 사이즈
    ) {
        try {
            Page<Artwork> paged = artworkService.getPagedArtworkItems(sort, page, size);

            // 프론트가 쓰기 좋은 형태로 축소 응답
            return ResponseEntity.ok(
                Map.of(
                    "items", paged.getContent(),   // 썸네일 카드들
                    "hasNext", paged.hasNext(),    // 다음 페이지 존재 여부
                    "page", paged.getNumber()      // 현재 페이지 번호
                )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ 작품 단일 조회
    @GetMapping("/artwork/{id}")
    public ResponseEntity<?> getArtworkById(@PathVariable Long id) {
        return artworkService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}