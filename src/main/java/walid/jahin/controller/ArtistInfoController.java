package walid.jahin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import walid.jahin.service.ArtistInfoService;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/email")
public class ArtistInfoController {

    private final ArtistInfoService artistInfoService;

    public ArtistInfoController(ArtistInfoService artistInfoService) {
        this.artistInfoService = artistInfoService;
    }

    @PostMapping
    public ResponseEntity<String> updateArtistEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        artistInfoService.resetAndSaveEmail(email);
        return ResponseEntity.ok("Artist email saved.");
    }
}
