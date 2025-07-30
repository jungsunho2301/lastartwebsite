package walid.jahin.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import walid.jahin.model.SessionConst;
import walid.jahin.service.NewsService;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/news")
public class AdminNewsController {

    private final NewsService newsService;

    public AdminNewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNews(@RequestBody Map<String, String> request,
                                      HttpSession session) {
        // ✅ 관리자 세션 여부 확인
        String adminUsername = (String) session.getAttribute(SessionConst.LOGIN_ADMIN);
        if (adminUsername == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        // ✅ 필수 입력값 확인
        String title = request.get("title");
        String content = request.get("content");
        if (title == null || content == null || title.isBlank() || content.isBlank()) {
            return ResponseEntity.badRequest().body("제목과 내용을 모두 입력해주세요.");
        }

        // ✅ XSS 방지 처리
        String safeTitle = HtmlUtils.htmlEscape(title);
        String safeContent = HtmlUtils.htmlEscape(content);

        // ✅ 뉴스 전송
        newsService.sendNewsToSubscribers(safeTitle, safeContent);
        return ResponseEntity.ok("뉴스가 모든 구독자에게 전송되었습니다.");
    }
}
