package walid.jahin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getArtworkItems(
        @RequestParam(defaultValue = "latest") String sort
    ) {
        try {
            List<Artwork> items = artworkService.getSortedArtworkItems(sort);
            return ResponseEntity.ok(items);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
