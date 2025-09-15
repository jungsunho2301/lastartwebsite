package walid.jahin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import walid.jahin.model.SoloArt;

public interface SoloArtRepository extends JpaRepository<SoloArt, Long> {
    Page<SoloArt> findByExhibitionId(Long exhibitionId, Pageable pageable);

    boolean existsByIdAndExhibitionId(Long id, Long exhibitionId);
}