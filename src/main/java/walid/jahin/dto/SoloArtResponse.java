package walid.jahin.dto;

import lombok.*;
import walid.jahin.model.SoloArt;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoloArtResponse {
    private Long id;
    private Long exhibitionId;
    private String title;
    private String imagePath;

    public static SoloArtResponse from(SoloArt a) {
        return SoloArtResponse.builder()
                .id(a.getId())
                .exhibitionId(a.getExhibition().getId())
                .title(a.getTitle())
                .imagePath(a.getImagePath())
                .build();
    }
}