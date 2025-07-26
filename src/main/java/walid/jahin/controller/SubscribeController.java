package walid.jahin.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import walid.jahin.service.SubscribeService;

@RestController
@RequestMapping("/api/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping
    public ResponseEntity<String> subscribe(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        subscribeService.subscribe(name, email);
        return ResponseEntity.ok("Subscribed successfully");
    }
}
