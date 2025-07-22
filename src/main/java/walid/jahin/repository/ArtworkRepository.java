package walid.jahin.repository;

import walid.jahin.model.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findAllByOrderByIdAsc(); // id 순 정렬
}
