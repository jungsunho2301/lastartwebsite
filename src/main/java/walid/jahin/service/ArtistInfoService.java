package walid.jahin.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import walid.jahin.model.ArtistInfo;
import walid.jahin.repository.ArtistInfoRepository;

@Service
public class ArtistInfoService {

    private final ArtistInfoRepository repository;
    private final EntityManager entityManager;

    public ArtistInfoService(ArtistInfoRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void resetAndSaveEmail(String email) {
        repository.deleteAll();  // 기존 데이터 삭제
        entityManager.createNativeQuery("ALTER TABLE artist_info AUTO_INCREMENT = 1").executeUpdate(); // id 초기화
        repository.save(new ArtistInfo(email));  // 새 이메일 저장
    }
}
