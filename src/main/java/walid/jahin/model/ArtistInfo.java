package walid.jahin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artist_info")
public class ArtistInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    // 기본 생성자
    public ArtistInfo() {}

    // email을 받는 생성자
    public ArtistInfo(String email) {
        this.email = email;
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
