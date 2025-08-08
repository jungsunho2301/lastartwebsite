package walid.jahin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artist_info")
public class ArtistInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_info_seq")
    @SequenceGenerator(name = "artist_info_seq", sequenceName = "artist_info_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String email;

    public ArtistInfo() {}

    public ArtistInfo(String email) {
        this.email = email;
    }

    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
