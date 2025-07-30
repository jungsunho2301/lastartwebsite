package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import walid.jahin.model.Artwork;
import walid.jahin.repository.ArtworkRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    // ✅ application.yml에서 경로 주입
    @Value("${app.upload.artwork-dir}")
    private String relativeUploadDir;

    private String getUploadDir() {
        return System.getProperty("user.dir") + File.separator + relativeUploadDir;
    }

    public Artwork saveArtwork(MultipartFile image) throws IOException {
        File uploadPath = new File(getUploadDir());
        if (!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            System.out.println("📁 디렉토리 생성됨: " + created);
        }

        String originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IOException("파일명이 유효하지 않습니다.");
        }
        System.out.println("🧾 원본 파일명: " + originalFilename);

        String filename = UUID.randomUUID() + "_" + originalFilename;
        File dest = new File(uploadPath, filename);
        System.out.println("📁 저장 경로: " + dest.getAbsolutePath());

        try {
            image.transferTo(dest);
        } catch (IOException e) {
            System.out.println("🧨 파일 저장 실패: " + e.getMessage());
            throw e;
        }

        Artwork artwork = Artwork.builder()
                .imagePath("/" + relativeUploadDir + filename)  // 앞에 / 추가해서 접근 경로 형식 통일
                .build();

        return artworkRepository.save(artwork);
    }

    public Artwork updateArtworkImage(Long id, MultipartFile newImage) throws IOException {
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 작품이 없습니다."));

        File uploadPath = new File(getUploadDir());
        if (!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            System.out.println("📁 디렉토리 생성됨: " + created);
        }

        String originalFilename = newImage.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IOException("파일명이 유효하지 않습니다.");
        }

        String filename = UUID.randomUUID() + "_" + originalFilename;
        File dest = new File(uploadPath, filename);
        newImage.transferTo(dest);

        // 기존 파일 삭제
        String oldImagePath = artwork.getImagePath();
        if (oldImagePath != null && oldImagePath.startsWith("/" + relativeUploadDir)) {
            String filenameOnly = Paths.get(oldImagePath).getFileName().toString();
            File oldFile = Paths.get(getUploadDir(), filenameOnly).toFile();

            System.out.println("🧾 삭제 대상 파일 경로: " + oldFile.getAbsolutePath());
            if (oldFile.exists()) {
                boolean deleted = oldFile.delete();
                System.out.println("🗑 삭제 성공 여부: " + deleted);
            } else {
                System.out.println("❌ 삭제 대상 파일이 존재하지 않음");
            }
        }

        artwork.setImagePath("/" + relativeUploadDir + filename);
        System.out.println("✅ 업데이트된 imagePath: " + artwork.getImagePath());

        return artworkRepository.save(artwork);
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }

    public Page<Artwork> getPagedArtworkItems(String sort, int page) {
        System.out.println("🔍 정렬 기준: " + sort + ", 페이지 번호: " + page);

        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "id");
            default -> throw new IllegalArgumentException("정렬 기준이 올바르지 않습니다.");
        };

        Pageable pageable = PageRequest.of(page, 3, sortOption);
        System.out.println("📦 Pageable 객체: " + pageable);

        return artworkRepository.findAll(pageable);
    }
}
