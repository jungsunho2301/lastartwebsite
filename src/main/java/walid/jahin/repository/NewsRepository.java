package walid.jahin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import walid.jahin.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
