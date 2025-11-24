package walid.jahin.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "solo_art")
public class SoloArt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solo_art_seq")
    @SequenceGenerator(name = "solo_art_seq", sequenceName = "solo_art_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Exhibition exhibition;

    @Column(nullable = false, length = 300)
    private String title; // 작품명

    @Column(nullable = false, length = 1000)
    private String imagePath; // 작품 이미지 URL
}
