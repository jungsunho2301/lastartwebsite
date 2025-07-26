package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;
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

    // ✅ 운영 서버용 업로드 디렉토리 (JAR 파일 기준 외부 경로)
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/artwork/";

    public Artwork saveArtwork(MultipartFile image) throws IOException {
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
        Artwork artwork = Artwork.builder()
                .imagePath("/uploads/artwork/" + filename)  // 정적 리소스 접근 경로
                .build();

        return artworkRepository.save(artwork);
    }

    public Artwork updateArtworkImage(Long id, MultipartFile newImage) throws IOException {
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 작품이 없습니다."));

        // ✅ 경로 준비
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            System.out.println("디렉토리 생성 여부: " + created);
        }

        //  ✅ 파일명 검증
        String originalFilename = newImage.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IOException("파일명이 유효하지 않습니다.");
        }

        // ✅ 새 파일명 생성 및 저장
        String filename = UUID.randomUUID() + "_" + originalFilename;
        File dest = new File(uploadPath, filename);
        newImage.transferTo(dest);

        // ✅ 기존 파일 삭제
        String oldImagePath = artwork.getImagePath();
        if (oldImagePath != null && oldImagePath.startsWith("/uploads/artwork/")) {
            String fullOldPath = uploadDir + oldImagePath.replace("/uploads/artwork/", "");
            File oldFile = new File(fullOldPath);
            System.out.println("삭제 시도 경로: " + oldFile.getAbsolutePath());
            if (oldFile.exists()) {
                boolean deleted = oldFile.delete();
                System.out.println("삭제 성공 여부: " + deleted);
            } else {
                System.out.println("삭제할 파일 없음");
            }
        }

        // ✅ DB 갱신
        artwork.setImagePath("/uploads/artwork/" + filename);
        System.out.println("업데이트된 imagePath: " + artwork.getImagePath());
        return artworkRepository.save(artwork);
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }

    public List<Artwork> getSortedArtworkItems(String sort) {
        System.out.println("🔍 정렬 기준: " + sort);
        
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "id");
            default -> throw new IllegalArgumentException("에러");
        };
        return artworkRepository.findAll(sortOption);
    }

}
