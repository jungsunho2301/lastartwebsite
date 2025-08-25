package walid.jahin.controller;

import walid.jahin.dto.LoginRequest;
import walid.jahin.model.SessionConst;
import walid.jahin.model.LoginLog;
import walid.jahin.repository.LoginLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AuthenticationManager authenticationManager;
    private final LoginLogRepository loginLogRepository;

    public AdminController(AuthenticationManager authenticationManager,
            LoginLogRepository loginLogRepository) {
        this.authenticationManager = authenticationManager;
        this.loginLogRepository = loginLogRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String clientIp = request.getRemoteAddr();

        Integer failCount = (Integer) session.getAttribute("loginFailCount");
        Long lastFailTime = (Long) session.getAttribute("lastFailTime");
        if (failCount == null)
            failCount = 0;

        if (lastFailTime != null && System.currentTimeMillis() - lastFailTime > 10 * 60 * 1000) {
            failCount = 0;
            session.removeAttribute("loginFailCount");
            session.removeAttribute("lastFailTime");
        }

        if (failCount >= 5) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("You have exceeded 5 failed login attempts. Please try again in 10 minutes.");
        }

        try {
            // ✅ Spring Security에서 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            // ✅ SecurityContext에 저장
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            // ✅ 세션 등록
            session.setAttribute(SessionConst.LOGIN_ADMIN, loginRequest.getUsername());
            session.setMaxInactiveInterval(1800); // 30분 세션 유지

            // ✅ 로그인 기록
            loginLogRepository.save(new LoginLog(
                    loginRequest.getUsername(), clientIp, true, LocalDateTime.now()));

            request.changeSessionId();

            return ResponseEntity.ok("Login completed");

        } catch (Exception ex) {
            session.setAttribute("loginFailCount", failCount + 1);
            session.setAttribute("lastFailTime", System.currentTimeMillis());

            loginLogRepository.save(new LoginLog(
                    loginRequest.getUsername(), clientIp, false, LocalDateTime.now()));

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((failCount + 1) + "failed login attempt");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("You have been logged out.");
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated()
                && !(auth.getPrincipal().equals("anonymousUser"));
        return isLoggedIn
                ? ResponseEntity.ok("authenticated")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthenticated");
    }
}