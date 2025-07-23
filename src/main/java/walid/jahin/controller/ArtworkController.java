package walid.jahin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import walid.jahin.model.Artwork;
import walid.jahin.service.ArtworkService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;

    // ✅ 전체 작품 조회 (관리자 포함 전체 공개용)
    @GetMapping("/artworks")
    public List<Artwork> getAllArtworks() {
        return artworkService.getAllArtworks();
    }

    // ✅ 아트샵 전용 조회 (forSale = true)
    @GetMapping("/artshop")
    public List<Artwork> getArtshopItems() {
        return artworkService.getArtshopItems();
    }

    @GetMapping("/ping")
        public String ping() {
        return "pong";
    }

}
