package walid.jahin.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        @RequestParam(defaultValue = "0") int page  // 0부터 시작
    ) {
        try {
            Page<Artwork> pagedItems = artworkService.getPagedArtworkItems(sort, page);
            return ResponseEntity.ok(pagedItems);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
