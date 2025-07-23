package walid.jahin.repository;

import walid.jahin.model.Artwork;
import walid.jahin.repository.ArtworkRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findAllByOrderByIdAsc();                 // 전체 (아트워크용)
    List<Artwork> findByForSaleTrueOrderByIdAsc();        // 아트샵 등록용
}
