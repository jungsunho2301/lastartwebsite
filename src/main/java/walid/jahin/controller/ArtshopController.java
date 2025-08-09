package walid.jahin.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import walid.jahin.model.Artshop;
import walid.jahin.service.ArtshopService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtshopController {

    private final ArtshopService artshopService;

    // 조회 + 정렬 (latest, lowprice, highprice)
    @GetMapping("/artshop")
    public ResponseEntity<?> getArtshopItems(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size // ✅ 프론트에서 페이지당 개수 조절
    ) {
        try {
            // ✅ 백엔드 정책 상한/하한 (예: 1~50)
            int safeSize = Math.min(Math.max(size, 1), 50);

            Page<Artshop> paged = artshopService.getPagedArtshopItems(sort, page, safeSize);

            return ResponseEntity.ok(
                    Map.of(
                            "items", paged.getContent(),
                            "hasNext", paged.hasNext(),
                            "page", paged.getNumber()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/artshop/{id}")
    public ResponseEntity<?> getArtshopById(@PathVariable Long id) {
        return artshopService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
