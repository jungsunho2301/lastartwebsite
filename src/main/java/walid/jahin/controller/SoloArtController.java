package walid.jahin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import walid.jahin.dto.SoloArtResponse;
import walid.jahin.service.SoloArtService;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class SoloArtController {

    private final SoloArtService service;

    @GetMapping("/api/solo/exhibitions/{exhibitionId}/arts")
    public List<SoloArtResponse> listByExhibition(
            @PathVariable Long exhibitionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return service.listByExhibition(exhibitionId, page, size);
    }
}