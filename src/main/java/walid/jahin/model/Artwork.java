package walid.jahin.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;           // 작품명
    private String description;     // 설명
    private int price;              // 가격
    private String imagePath;       // 이미지 경로 (/uploads/artwork/xxx.jpg)
    private boolean forSale;        // 아트샵 등록 여부
}
