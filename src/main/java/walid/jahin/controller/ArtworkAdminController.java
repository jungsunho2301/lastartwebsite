package walid.jahin.controller;

import walid.jahin.model.Artwork;
import walid.jahin.repository.ArtworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/artworks")
public class ArtworkAdminController {

    @Autowired
    private ArtworkRepository artworkRepository;

    @GetMapping
    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAllByOrderByIdAsc(); // 정렬된 상태
    }

    @PostMapping
    public Artwork addArtwork(@RequestBody Artwork artwork) {
        return artworkRepository.save(artwork);
    }

    @DeleteMapping("/{id}")
    public void deleteArtwork(@PathVariable Long id) {
        artworkRepository.deleteById(id);
    }
}
