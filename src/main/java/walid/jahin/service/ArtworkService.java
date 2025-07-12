package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.Artwork;
import walid.jahin.repository.ArtworkRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    private final String uploadDir = "uploads/artworks/";

    public Artwork saveArtwork(String title, MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir + filename);
        file.transferTo(dest);

        Artwork artwork = new Artwork();
        artwork.setTitle(title);
        artwork.setImagePath("/uploads/artworks/" + filename);
        return artworkRepository.save(artwork);
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }
}
