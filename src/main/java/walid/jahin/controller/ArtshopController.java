package walid.jahin.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(defaultValue = "0") int page) {
        try {
            Page<Artshop> pagedItems = artshopService.getPagedArtshopItems(sort, page);
            return ResponseEntity.ok(pagedItems);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
