package walid.jahin.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import walid.jahin.model.ArtistSection;
import walid.jahin.service.ArtistSectionService;

@RestController
@RequestMapping("/admin/api/artist/section")
public class AdminArtistSectionController {

    private final ArtistSectionService sectionService;

    public AdminArtistSectionController(ArtistSectionService sectionService) {
        this.sectionService = sectionService;
    }

    // 전체 항목 조회
    @GetMapping
    public ResponseEntity<List<ArtistSection>> getAllSections() {
        return ResponseEntity.ok(sectionService.getAllSections());
    }

    // 특정 항목 조회
    @GetMapping("/{key}")
    public ResponseEntity<ArtistSection> getSection(@PathVariable String key) {
        return ResponseEntity.ok(sectionService.getSectionByKey(key));
    }

    // 특정 항목 수정
    @PutMapping("/{key}")
    public ResponseEntity<String> updateSection(
        @PathVariable String key,
        @RequestBody Map<String, String> body
    ) {
        sectionService.updateSectionContent(key, body.get("content"));
        return ResponseEntity.ok("Section updated");
    }
}
