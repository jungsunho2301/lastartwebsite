package walid.jahin.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CsrfController {

    @GetMapping("/api/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        // Spring Security가 자동으로 CSRF 토큰을 request attribute에 넣어줍니다.
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
