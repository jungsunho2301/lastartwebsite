package walid.jahin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils; // ✅ 추가
import walid.jahin.model.ArtistSection;
import walid.jahin.repository.ArtistSectionRepository;

@Service
public class ArtistSectionService {

    private final ArtistSectionRepository repository;

    public ArtistSectionService(ArtistSectionRepository repository) {
        this.repository = repository;
    }

    // 전체 조회
    public List<ArtistSection> getAllSections() {
        return repository.findAll();
    }

    // 단일 항목 조회
    public ArtistSection getSectionByKey(String key) {
        return repository.findBySectionKey(key)
            .orElseThrow(() -> new IllegalArgumentException("Section not found: " + key));
    }

    // 단일 항목 수정
    public void updateSectionContent(String key, String newContent) {
        ArtistSection section = repository.findBySectionKey(key)
            .orElseThrow(() -> new IllegalArgumentException("Section not found: " + key));

        // ✅ XSS 방지 처리
        String safeContent = HtmlUtils.htmlEscape(newContent);
        section.setContent(safeContent);

        repository.save(section);
    }
}
