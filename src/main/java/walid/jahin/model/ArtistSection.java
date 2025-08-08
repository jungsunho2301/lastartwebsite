package walid.jahin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "artist_section")
public class ArtistSection {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_section_seq")
    @SequenceGenerator(name = "artist_section_seq", sequenceName = "artist_section_seq", allocationSize = 1)
    private Long id;

    @Column(name = "section_key", unique = true, nullable = false, length = 100)
    private String sectionKey; // 예: "major_solo", "international"

    @Lob
    @Column(nullable = true)
    private String content;

    public ArtistSection() {}

    public ArtistSection(String sectionKey, String content) {
        this.sectionKey = sectionKey;
        this.content = content;
    }

    public Long getId() { return id; }
    public String getSectionKey() { return sectionKey; }
    public void setSectionKey(String sectionKey) { this.sectionKey = sectionKey; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
