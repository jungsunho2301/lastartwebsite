package walid.jahin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import walid.jahin.service.ArtistSocialService;

import java.util.Map;

@RestController
public class ArtistSocialController {

    private final ArtistSocialService service;

    public ArtistSocialController(ArtistSocialService service) {
        this.service = service;
    }

    @PostMapping("/admin/api/social/facebook")
    public ResponseEntity<String> updateFacebook(@RequestBody Map<String, String> body) {
        String facebook = body.get("facebook");
        service.updateFacebook(facebook);
        return ResponseEntity.ok("Facebook link has been saved.");
    }

    @PostMapping("/admin/api/social/instagram")
    public ResponseEntity<String> updateInstagram(@RequestBody Map<String, String> body) {
        String instagram = body.get("instagram");
        service.updateInstagram(instagram);
        return ResponseEntity.ok("Instagram link has been saved.");
    }

    @GetMapping("/api/social/facebook")
    public ResponseEntity<String> getFacebook() {
        var current = service.getCurrent();
        return ResponseEntity.ok(current != null && current.getFacebookUrl() != null ? current.getFacebookUrl() : "");
    }

    @GetMapping("/api/social/instagram")
    public ResponseEntity<String> getInstagram() {
        var current = service.getCurrent();
        return ResponseEntity.ok(current != null && current.getInstagramUrl() != null ? current.getInstagramUrl() : "");
    }
}
