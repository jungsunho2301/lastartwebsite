package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import walid.jahin.model.Artwork;
import walid.jahin.repository.ArtworkRepository;
import walid.jahin.util.ObjectStorageUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ObjectStorageUtil objectStorageUtil;

    // ✅ 작품 등록
    public Artwork saveArtwork(MultipartFile image) throws IOException {
        String prefix = "artwork"; // 'artwork' 폴더
        String fileUrl = objectStorageUtil.uploadArtworkToObjectStorage(image, prefix);

        Artwork artwork = Artwork.builder()
                .imagePath(fileUrl)
                .build();

        return artworkRepository.save(artwork);
    }

    // ✅ 작품 이미지 수정
    public Artwork updateArtworkImage(Long id, MultipartFile newImage) throws IOException {
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No artwork found with the given ID."));

        String prefix = "artwork";
        String fileUrl = objectStorageUtil.uploadArtworkToObjectStorage(newImage, prefix);

        // ⚠️ PAR 방식은 삭제 권한이 없으므로 기존 파일은 그대로 둠
        artwork.setImagePath(fileUrl);

        return artworkRepository.save(artwork);
    }

    // ✅ 전체 조회
    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    // ✅ 삭제 (DB에서만 삭제)
    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }

    // ✅ 페이지네이션 + 정렬 (인피니트 스크롤용) — 사이즈 지정 가능
    public Page<Artwork> getPagedArtworkItems(String sort, int page, int size) {
        // 현재 안전한 기본 정렬: id DESC (최신 먼저)
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "id");
            // 필요 시 추후 확장 (price 컬럼이 있을 때 아래 주석 해제)
            // case "lowprice" -> Sort.by(Sort.Direction.ASC, "price");
            // case "highprice" -> Sort.by(Sort.Direction.DESC, "price");
            default -> throw new IllegalArgumentException("정렬 기준이 올바르지 않습니다.");
        };

        Pageable pageable = PageRequest.of(page, size, sortOption);
        return artworkRepository.findAll(pageable);
    }

    // ✅ 기존 20개 고정 메서드 (호환용) — 내부적으로 size=20 호출
    public Page<Artwork> getPagedArtworkItems(String sort, int page) {
        return getPagedArtworkItems(sort, page, 20);
    }

    // ✅ 단건 조회
    public Optional<Artwork> findById(Long id) {
        return artworkRepository.findById(id);
    }
}
