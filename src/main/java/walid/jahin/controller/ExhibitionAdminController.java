package walid.jahin.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import walid.jahin.model.Exhibition;
import walid.jahin.service.ExhibitionCommandService;

@RestController
@RequestMapping("/admin/solo/exhibitions")
public class ExhibitionAdminController {

    private final ExhibitionCommandService service;

    public ExhibitionAdminController(ExhibitionCommandService service) {
        this.service = service;
    }

    /**
     * 전시 생성 (multipart)
     * form-data: title, yearMonth(YYYY-MM), poster(file)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Exhibition create(@RequestParam String title,
            @RequestParam String yearMonth,
            @RequestParam("poster") MultipartFile poster) {
        return service.create(title, yearMonth, poster);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.deleteById(id) ? ResponseEntity.noContent().build()
                                      : ResponseEntity.notFound().build();
    }
}
