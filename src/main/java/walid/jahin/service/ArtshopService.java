package walid.jahin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import walid.jahin.model.Artshop;
import walid.jahin.repository.ArtshopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtshopService {

    private final ArtshopRepository artshopRepository;

    @Value("${app.upload.artshop-dir}")
    private String relativeUploadDir;

    private String getUploadDir() {
        return System.getProperty("user.dir") + File.separator + relativeUploadDir;
    }

    public Artshop saveArtshop(String title, String description, Integer price, MultipartFile image) throws IOException {
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

        // ✅ XSS 방지 처리
        String safeTitle = HtmlUtils.htmlEscape(title);
        String safeDesc = HtmlUtils.htmlEscape(description);

        Artshop artshop = Artshop.builder()
                .title(safeTitle)
                .description(safeDesc)
                .price(price)
                .imagePath("/" + relativeUploadDir + filename)
                .build();

        return artshopRepository.save(artshop);
    }

    public Artshop updateArtshop(Long id, String title, String description, Integer price, MultipartFile image) throws IOException {
        Artshop artshop = artshopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작품입니다."));

        if (title != null && !title.isBlank()) {
            artshop.setTitle(HtmlUtils.htmlEscape(title)); // ✅ XSS 방지
        }

        if (description != null && !description.isBlank()) {
            artshop.setDescription(HtmlUtils.htmlEscape(description)); // ✅ XSS 방지
        }

        if (price != null) {
            artshop.setPrice(price);
        }

        if (image != null && !image.isEmpty()) {
            File uploadPath = new File(getUploadDir());
            if (!uploadPath.exists()) uploadPath.mkdirs();

            String originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new IOException("파일명이 유효하지 않습니다.");
            }

            String filename = UUID.randomUUID() + "_" + originalFilename;
            File dest = new File(uploadPath, filename);
            image.transferTo(dest);

            String oldImagePath = artshop.getImagePath();
            if (oldImagePath != null && oldImagePath.startsWith("/" + relativeUploadDir)) {
                String filenameOnly = Paths.get(oldImagePath).getFileName().toString();
                Path fullOldPath = Paths.get(getUploadDir()).resolve(filenameOnly);
                File oldFile = fullOldPath.toFile();

                System.out.println("🧾 삭제 대상 파일 경로: " + fullOldPath);

                if (oldFile.exists()) {
                    boolean deleted = oldFile.delete();
                    System.out.println("🗑 삭제 성공 여부: " + deleted);
                } else {
                    System.out.println("❌ 삭제 대상 파일이 존재하지 않음");
                }
            }

            artshop.setImagePath("/" + relativeUploadDir + filename);
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

        Pageable pageable = PageRequest.of(page, 20, sortOption);
        System.out.println("📦 Pageable 객체: " + pageable);

        return artshopRepository.findAll(pageable);
    }
}
