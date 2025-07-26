package walid.jahin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walid.jahin.model.ArtistInfo;

public interface ArtistInfoRepository extends JpaRepository<ArtistInfo, Long> {
}
