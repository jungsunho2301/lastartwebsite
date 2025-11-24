package walid.jahin.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.Exhibition;
import walid.jahin.model.SoloArt;
import walid.jahin.repository.ExhibitionRepository;
import walid.jahin.repository.SoloArtRepository;
import walid.jahin.util.ObjectStorageUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class SoloArtAdminService {

    private final SoloArtRepository soloArtRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final ObjectStorageUtil storage;

    public SoloArt create(Long exhibitionId, String title, MultipartFile imageFile) {
        if (exhibitionId == null) throw new IllegalArgumentException("exhibitionId is required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title is required");
        if (imageFile == null || imageFile.isEmpty()) throw new IllegalArgumentException("image file is required");

        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> new EntityNotFoundException("Exhibition not found: " + exhibitionId));

        try {
            String url = storage.uploadArtworkToObjectStorage(imageFile, "artwork");

            SoloArt soloArt = SoloArt.builder()
                    .exhibition(exhibition)
                    .title(title)
                    .imagePath(url)
                    .build();

            return soloArtRepository.save(soloArt);
        } catch (Exception e) {
            throw new RuntimeException("SoloArt Upload failed: " + e.getMessage(), e);
        }
    }

    public boolean delete(Long exhibitionId, Long soloArtId) {
        if (!soloArtRepository.existsByIdAndExhibitionId(soloArtId, exhibitionId)) return false;
        soloArtRepository.deleteById(soloArtId);
        return true;
    }
}
