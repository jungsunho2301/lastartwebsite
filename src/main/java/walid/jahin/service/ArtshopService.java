package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import walid.jahin.model.Artshop;
import walid.jahin.repository.ArtshopRepository;
import walid.jahin.util.ObjectStorageUtil;

import org.springframework.data.domain.*;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtshopService {

    private final ArtshopRepository artshopRepository;
    private final ObjectStorageUtil objectStorageUtil; // ✅ OCI 업로드 유틸

    // ✅ 등록
    public Artshop saveArtshop(String title, String description, Integer price, MultipartFile image)
            throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IOException("이미지 파일이 비어 있습니다.");
        }
        String imageUrl = objectStorageUtil.uploadArtworkToObjectStorage(image, "artshop");
        Artshop artshop = Artshop.builder()
                .title(HtmlUtils.htmlEscape(title))
                .description(HtmlUtils.htmlEscape(description))
                .price(price)
                .imagePath(imageUrl)
                .build();
        return artshopRepository.save(artshop);
    }

    // ✅ 수정
    public Artshop updateArtshop(Long id, String title, String description, Integer price, MultipartFile image)
            throws IOException {
        Artshop artshop = artshopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작품입니다."));

        if (title != null && !title.isBlank())
            artshop.setTitle(HtmlUtils.htmlEscape(title));
        if (description != null && !description.isBlank())
            artshop.setDescription(HtmlUtils.htmlEscape(description));
        if (price != null)
            artshop.setPrice(price);

        if (image != null && !image.isEmpty()) {
            // ⚠ PAR 방식 파일 삭제 권한 없음 → DB 경로만 교체
            String imageUrl = objectStorageUtil.uploadArtworkToObjectStorage(image, "artshop");
            artshop.setImagePath(imageUrl);
        }
        return artshopRepository.save(artshop);
    }

    // ✅ 전체 조회
    public List<Artshop> getAllArtshops() {
        return artshopRepository.findAll();
    }

    // ✅ 삭제 (DB만)
    public void deleteArtshop(Long id) {
        artshopRepository.deleteById(id);
    }

    // ✅ 페이지네이션 + 정렬 (size 지원, 인피니트 스크롤용)
    public Page<Artshop> getPagedArtshopItems(String sort, int page, int size) {
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "id");
            case "lowprice" -> Sort.by(Sort.Direction.ASC, "price");
            case "highprice" -> Sort.by(Sort.Direction.DESC, "price");
            default -> throw new IllegalArgumentException("정렬 기준은 latest, lowprice, highprice 중 하나여야 합니다.");
        };
        Pageable pageable = PageRequest.of(page, size, sortOption);
        return artshopRepository.findAll(pageable);
    }

    // ✅ 기존 시그니처 유지(호환용): size=20 고정
    public Page<Artshop> getPagedArtshopItems(String sort, int page) {
        return getPagedArtshopItems(sort, page, 20);
    }

    // ✅ 단건 조회
    public Optional<Artshop> findById(Long id) {
        return artshopRepository.findById(id);
    }
}
