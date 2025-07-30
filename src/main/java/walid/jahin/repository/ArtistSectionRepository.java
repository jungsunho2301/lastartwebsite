package walid.jahin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walid.jahin.model.ArtistSection;
import java.util.Optional;

public interface ArtistSectionRepository extends JpaRepository<ArtistSection, Long> {
    Optional<ArtistSection> findBySectionKey(String sectionKey);
}
