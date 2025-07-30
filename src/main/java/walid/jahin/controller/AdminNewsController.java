package walid.jahin.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import walid.jahin.dto.NewsRequest;
import walid.jahin.model.SessionConst;
import walid.jahin.service.NewsService;

@RestController
@RequestMapping("/admin/api/news")
public class AdminNewsController {

    private final NewsService newsService;

    public AdminNewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNews(@RequestBody @Valid NewsRequest request,
                                      HttpSession session) {
        // ✅ 관리자 세션 여부 확인
        String adminUsername = (String) session.getAttribute(SessionConst.LOGIN_ADMIN);
        if (adminUsername == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        // ✅ XSS 방지 처리
        String safeTitle = HtmlUtils.htmlEscape(request.getTitle());
        String safeContent = HtmlUtils.htmlEscape(request.getContent());

        // ✅ 뉴스 전송
        newsService.sendNewsToSubscribers(safeTitle, safeContent);
        return ResponseEntity.ok("뉴스가 모든 구독자에게 전송되었습니다.");
    }
}
