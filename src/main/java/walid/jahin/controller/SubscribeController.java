package walid.jahin.controller;

import walid.jahin.dto.SubscribeRequest;
import walid.jahin.service.SubscriberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    private final SubscriberService service;

    public SubscribeController(SubscriberService service) {
        this.service = service;
    }

    @PostMapping
    public String subscribe(@RequestBody SubscribeRequest request) {
        service.subscribe(request);
        return "구독되었습니다!";
    }
}
