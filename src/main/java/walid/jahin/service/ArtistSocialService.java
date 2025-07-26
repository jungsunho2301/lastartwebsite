package walid.jahin.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import walid.jahin.model.ArtistSocial;
import walid.jahin.repository.ArtistSocialRepository;

@Service
public class ArtistSocialService {

    private final ArtistSocialRepository repository;
    private final EntityManager entityManager;

    public ArtistSocialService(ArtistSocialRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void updateFacebook(String facebookUrl) {
        ArtistSocial current = getCurrent();
        if (current != null) {
            current.setFacebookUrl(facebookUrl);
            repository.save(current);
        } else {
            repository.save(new ArtistSocial(facebookUrl, null));
        }
    }

    @Transactional
    public void updateInstagram(String instagramUrl) {
        ArtistSocial current = getCurrent();
        if (current != null) {
            current.setInstagramUrl(instagramUrl);
            repository.save(current);
        } else {
            repository.save(new ArtistSocial(null, instagramUrl));
        }
    }

    public ArtistSocial getCurrent() {
        return repository.findAll().stream().findFirst().orElse(null);
    }
}
