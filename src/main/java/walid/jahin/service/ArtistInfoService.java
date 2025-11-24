package walid.jahin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import walid.jahin.repository.ArtistInfoRepository;

@Service
public class ArtistInfoService {

    private final ArtistInfoRepository repository;

    public ArtistInfoService(ArtistInfoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void resetAndSaveEmail(String email) {
        // ID=1 한 행에 대해 MERGE 업서트 (Repository.upsertEmail)
        repository.upsertEmail(email);
    }
}
