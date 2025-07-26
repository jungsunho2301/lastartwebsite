package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.Artshop;
import walid.jahin.repository.ArtshopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtshopService {

    private final ArtshopRepository artshopRepository;

    // ✅ 운영 서버용 업로드 디렉토리 (JAR 파일 기준 외부 경로)
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/artshop/";

    public Artshop saveArtshop(String title, String description, int price, MultipartFile image) throws IOException {
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
            throw e;
        }

        // 5. DB 저장
        Artshop artshop = Artshop.builder()
                .title(title)
                .description(description)
                .price(price)
                .imagePath("/uploads/artshop/" + filename)  // 정적 리소스 접근 경로
                .build();

        return artshopRepository.save(artshop);
    }

    public List<Artshop> getAllArtshops() {
        return artshopRepository.findAll();
    }

    public void deleteArtshop(Long id) {
        artshopRepository.deleteById(id);
    }

    public Page<Artshop> getPagedArtshopItems(String sort, int page) {
        System.out.println("🔍 정렬 기준: " + sort + ", 페이지 번호: " + page);
        
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "id");
            case "lowprice" -> Sort.by(Sort.Direction.ASC, "price");
            case "highprice" -> Sort.by(Sort.Direction.DESC, "price");
            default -> throw new IllegalArgumentException("정렬 기준은 latest, lowprice, highprice 중 하나여야 합니다.");
        };

        // 페이지당 20개 고정
        Pageable pageable = PageRequest.of(page, 20, sortOption);
        System.out.println("📦 Pageable 객체: " + pageable);

        return artshopRepository.findAll(pageable);
    }

}
