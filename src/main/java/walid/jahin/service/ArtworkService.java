package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.Artwork;
import walid.jahin.repository.ArtworkRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    // ✅ 절대경로 기반 저장 디렉토리 설정
    private final String uploadDir = new File("src/main/resources/static/uploads/artwork/").getAbsolutePath() + "/";

    public Artwork saveArtwork(String title, String description, int price, boolean forSale, MultipartFile image) throws IOException {
        // 1. 경로 준비
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            System.out.println("📁 디렉토리 생성됨: " + created);
        }

        // 2. 파일명 검증
        String originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IOException("파일명이 유효하지 않습니다.");
        }
        System.out.println("🧾 원본 파일명: " + originalFilename);

        // 3. 저장 경로 설정
        String filename = UUID.randomUUID() + "_" + originalFilename;
        File dest = new File(uploadPath, filename);  // 안전한 경로 결합
        System.out.println("📁 저장 경로: " + dest.getAbsolutePath());

        // 4. 파일 저장
        try {
            image.transferTo(dest);
        } catch (IOException e) {
            System.out.println("🧨 파일 저장 실패: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        // 5. DB 저장
        Artwork artwork = Artwork.builder()
                .title(title)
                .description(description)
                .price(price)
                .forSale(forSale)
                .imagePath("/uploads/artwork/" + filename)  // 웹 접근 경로
                .build();

        return artworkRepository.save(artwork);
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public List<Artwork> getArtshopItems() {
        return artworkRepository.findByForSaleTrueOrderByIdAsc();
    }

    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }
}
