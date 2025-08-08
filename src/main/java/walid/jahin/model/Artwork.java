package walid.jahin.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "artwork")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artwork_seq")
    @SequenceGenerator(name = "artwork_seq", sequenceName = "artwork_seq", allocationSize = 1)
    private Long id;

    private String imagePath; // 이미지 경로 (/uploads/artwork/xxx.jpg)
}
