package walid.jahin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walid.jahin.model.Exhibition;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
}
