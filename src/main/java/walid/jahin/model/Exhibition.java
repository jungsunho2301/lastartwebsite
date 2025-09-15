package walid.jahin.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "exhibition")
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exhibition_seq")
    @SequenceGenerator(name = "exhibition_seq", sequenceName = "exhibition_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title; // 전시회 이름

    @Column(nullable = false, length = 7)
    private String yearMonth; // YYYY-MM

    @Column(nullable = false, length = 1000)
    private String posterImagePath; // 전시 포스터 이미지 URL
}
