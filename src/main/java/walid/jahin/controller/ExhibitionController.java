package walid.jahin.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import walid.jahin.dto.ExhibitionResponse;
import walid.jahin.service.ExhibitionService;

@RequiredArgsConstructor
@Validated
@RestController
public class ExhibitionController {

    private final ExhibitionService service;

    // ===================== 공개 조회용 =====================

    /** 목록 조회 (기본: page=0, size=12) */
    @GetMapping("/api/solo/exhibitions")
    public List<ExhibitionResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return service.listPaged(page, size);
    }

    /** 단건 조회 */
    @GetMapping("/api/solo/exhibitions/{id}")
    public ExhibitionResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
