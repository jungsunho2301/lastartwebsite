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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("reCAPTCHA 인증에 실패했습니다.");
        }

        String clientIp = getClientIp(httpRequest);

        if (!rateLimiterService.isAllowed(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("같은 IP에서 너무 많은 요청이 감지되었습니다. 잠시 후 다시 시도해주세요.");
        }

        boolean subscribed = subscribeService.subscribe(request.getName(), request.getEmail());
        if (!subscribed) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 구독된 이메일입니다.");
        }

        return ResponseEntity.ok("구독이 성공적으로 완료되었습니다.");
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
