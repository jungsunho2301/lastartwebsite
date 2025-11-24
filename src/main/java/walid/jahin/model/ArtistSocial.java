package walid.jahin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artist_social")
public class ArtistSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_social_seq")
    @SequenceGenerator(name = "artist_social_seq", sequenceName = "artist_social_seq", allocationSize = 1)
    private Long id;

    @Column(name = "facebook_url", length = 255, nullable = true)
    private String facebookUrl;

    @Column(name = "instagram_url", length = 255, nullable = true)
    private String instagramUrl;

    public ArtistSocial() {}

    public ArtistSocial(String facebookUrl, String instagramUrl) {
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
    }

    public Long getId() {
        return id;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }
}
