package walid.jahin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artist_section")
public class ArtistSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_key", unique = true, nullable = false)
    private String sectionKey; // 예: "major_solo", "international"

    @Column(columnDefinition = "TEXT")
    private String content;

    // 기본 생성자
    public ArtistSection() {}

    // 생성자
    public ArtistSection(String sectionKey, String content) {
        this.sectionKey = sectionKey;
        this.content = content;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public String getSectionKey() { return sectionKey; }
    public void setSectionKey(String sectionKey) { this.sectionKey = sectionKey; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
