package walid.jahin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.dto.SoloArtResponse;
import walid.jahin.model.SoloArt;
import walid.jahin.service.SoloArtAdminService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/solo/exhibitions/{exhibitionId}/arts")
public class SoloArtAdminController {

    private final SoloArtAdminService adminService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SoloArtResponse create(
            @PathVariable Long exhibitionId,
            @RequestParam String title,
            @RequestParam("image") MultipartFile imageFile) {
        SoloArt saved = adminService.create(exhibitionId, title, imageFile);
        return SoloArtResponse.from(saved);
    }

    @DeleteMapping("/{soloArtId}")
    public ResponseEntity<Void> delete(@PathVariable Long exhibitionId, @PathVariable Long soloArtId) {
        return adminService.delete(exhibitionId, soloArtId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}