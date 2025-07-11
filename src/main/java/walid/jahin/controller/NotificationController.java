package walid.jahin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import walid.jahin.service.NotificationService;

import java.util.Map;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/notify")
    public String sendNotification(@RequestBody Map<String, String> request) {
        String subject = request.get("subject");
        String content = request.get("content");

        notificationService.sendNewNotification(subject, content);

        return "Notification sent to subscribers";
    }
}
