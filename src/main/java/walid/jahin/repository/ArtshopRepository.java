package walid.jahin.repository;

import walid.jahin.model.Artshop;
import walid.jahin.repository.ArtshopRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ArtshopRepository extends JpaRepository<Artshop, Long> {
    List<Artshop> findAllByOrderByIdAsc();                 // 전체 

    List<Artshop> findAllByOrderByIdDesc();         // 최신순 (id 기준 내림차순)

    List<Artshop> findAllByOrderByPriceAsc();       // 가격 낮은 순

    List<Artshop> findAllByOrderByPriceDesc();      // 가격 높은 순

    Page<Artshop> findAll(Pageable pageable);     // 페이징
}
