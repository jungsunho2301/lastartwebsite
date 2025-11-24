package walid.jahin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.Exhibition;
import walid.jahin.repository.ExhibitionRepository;
import walid.jahin.util.ObjectStorageUtil;

@Service
@Transactional
public class ExhibitionCommandService {

    private final ExhibitionRepository exhibitionRepository;
    private final ObjectStorageUtil storage;

    public ExhibitionCommandService(ExhibitionRepository exhibitionRepository,
            ObjectStorageUtil storage) {
        this.exhibitionRepository = exhibitionRepository;
        this.storage = storage;
    }

    public Exhibition create(String title, String yearMonth, MultipartFile poster) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title is required");
        if (yearMonth == null || !yearMonth.matches("\\d{4}-\\d{2}"))
            throw new IllegalArgumentException("yearMonth must be 'YYYY-MM'");
        if (poster == null || poster.isEmpty())
            throw new IllegalArgumentException("poster image is required");

        try {
            // ▶ 네 유틸 그대로 사용: 포스터도 artwork PAR로 업로드 (prefix: "artwork")
            String posterUrl = storage.uploadArtworkToObjectStorage(poster, "artwork");

            Exhibition e = Exhibition.builder()
                    .title(title)
                    .yearMonth(yearMonth)
                    .posterImagePath(posterUrl)
                    .build();
            return exhibitionRepository.save(e);
        } catch (Exception e) {
            // 유틸이 IOException 등 던지므로 런타임으로 감싸서 실패 응답
            throw new RuntimeException("Poster Upload failed: " + e.getMessage(), e);
        }
    }

    /** 삭제: 있으면 삭제하고 true, 없으면 false 반환 */
    public boolean deleteById(Long id) {
        if (!exhibitionRepository.existsById(id)) return false;
        exhibitionRepository.deleteById(id);
        return true;
    }
}
