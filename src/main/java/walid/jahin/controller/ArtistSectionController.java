package walid.jahin.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import walid.jahin.model.ArtistSection;
import walid.jahin.service.ArtistSectionService;

@RestController
@RequestMapping("/api/artist/section")
public class ArtistSectionController {

    private final ArtistSectionService sectionService;

    public ArtistSectionController(ArtistSectionService sectionService) {
        this.sectionService = sectionService;
    }

    // 특정 항목 조회
    @GetMapping("/{key}")
    public ResponseEntity<String> getSectionContent(@PathVariable String key) {
        ArtistSection section = sectionService.getSectionByKey(key);
        return ResponseEntity.ok(section.getContent());
    }
}
