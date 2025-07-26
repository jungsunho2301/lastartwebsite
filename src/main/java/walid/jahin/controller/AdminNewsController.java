package walid.jahin.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import walid.jahin.service.NewsService;

@RestController
@RequestMapping("/admin/api/news")
public class AdminNewsController {

    private final NewsService newsService;

    public AdminNewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNews(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String content = request.get("content");
        newsService.sendNewsToSubscribers(title, content);
        return ResponseEntity.ok("News sent to all subscribers");
    }
}
