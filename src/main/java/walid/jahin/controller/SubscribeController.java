package walid.jahin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import walid.jahin.dto.SubscribeRequest;
import walid.jahin.service.CaptchaService;
import walid.jahin.service.SubscribeRateLimiterService;
import walid.jahin.service.SubscribeService;

@RestController
@RequestMapping("/api/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;
    private final CaptchaService captchaService;
    private final SubscribeRateLimiterService rateLimiterService;

    public SubscribeController(SubscribeService subscribeService,
                               CaptchaService captchaService,
                               SubscribeRateLimiterService rateLimiterService) {
        this.subscribeService = subscribeService;
        this.captchaService = captchaService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping
    public ResponseEntity<String> subscribe(@RequestBody @Valid SubscribeRequest request,
                                            @RequestParam("g-recaptcha-response") String recaptchaToken,
                                            HttpServletRequest httpRequest) {

        if (!captchaService.verify(recaptchaToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to verify reCAPTCHA.");
        }

        String clientIp = getClientIp(httpRequest);

        if (!rateLimiterService.isAllowed(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many requests detected from the same IP. Please try again later.");
        }

        boolean subscribed = subscribeService.subscribe(request.getName(), request.getEmail());
        if (!subscribed) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This email is already subscribed.");
        }

        return ResponseEntity.ok("Subscription completed successfully.");
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
