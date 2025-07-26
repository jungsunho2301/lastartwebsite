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
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ArtshopService {

    private final ArtshopRepository artshopRepository;

    // ✅ 운영 서버용 업로드 디렉토리 (JAR 파일 기준 외부 경로)
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/artshop/";

    public Artshop saveArtshop(String title, String description, Integer price, MultipartFile image) throws IOException {
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

    public Artshop updateArtshop(Long id, String title, String description, Integer price, MultipartFile image) throws IOException {
        // 1. 기존 작품 가져오기
        Artshop artshop = artshopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작품입니다."));

        // 2. 변경 요청된 필드만 조건부로 업데이트
        if (title != null && !title.isBlank()) {
            artshop.setTitle(title);
        }

        if (description != null && !description.isBlank()) {
            artshop.setDescription(description);
        }

        if (price != null) {
            artshop.setPrice(price);
        }

        // 3. 이미지가 첨부된 경우에만 교체
        if (image != null && !image.isEmpty()) {
            // ✅ 경로 준비
            File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) uploadPath.mkdirs();

            // ✅ 파일명 검증
            String originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new IOException("파일명이 유효하지 않습니다.");
            }

            // ✅ 새 파일명 생성 및 저장
            String filename = UUID.randomUUID() + "_" + originalFilename;
            File dest = new File(uploadPath, filename);
            image.transferTo(dest);

        // ✅ 기존 파일 삭제
        String oldImagePath = artshop.getImagePath();
        if (oldImagePath != null && oldImagePath.startsWith("/uploads/artshop/")) {
            // 파일명만 추출
            String filenameOnly = Paths.get(oldImagePath).getFileName().toString();
    
            // 전체 경로 생성
            Path fullOldPath = Paths.get(uploadDir).resolve(filenameOnly);
            File oldFile = fullOldPath.toFile();

            System.out.println("🧾 삭제 대상 파일 경로: " + fullOldPath);

            if (oldFile.exists()) {
                boolean deleted = oldFile.delete();
                System.out.println("🗑 삭제 성공 여부: " + deleted);
            } else {
                System.out.println("❌ 삭제 대상 파일이 존재하지 않음");
            }
        }

            // ✅ DB 갱신
            artshop.setImagePath("/uploads/artshop/" + filename);
            }
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
