package walid.jahin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artist_social")
public class ArtistSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facebook_url", nullable = true)
    private String facebookUrl;

    @Column(name = "instagram_url", nullable = true)
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
