package walid.jahin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import walid.jahin.model.Exhibition;

@Data
@Builder
@AllArgsConstructor
public class ExhibitionResponse {
    private Long id;
    private String title;
    private String yearMonth; // "YYYY-MM"
    private String posterImagePath; // 포스터 URL

    public static ExhibitionResponse from(Exhibition e) {
        return ExhibitionResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .yearMonth(e.getYearMonth())
                .posterImagePath(e.getPosterImagePath())
                .build();
    }
}
