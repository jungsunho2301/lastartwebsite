package walid.jahin.controller;

import walid.jahin.dto.LoginRequest;
import walid.jahin.model.SessionConst;
import walid.jahin.model.LoginLog;
import walid.jahin.repository.LoginLogRepository;
import walid.jahin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final LoginLogRepository loginLogRepository;

    public AdminController(AdminService adminService, LoginLogRepository loginLogRepository) {
        this.adminService = adminService;
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
                    .body("로그인 5회 이상 실패. 10분 후 다시 시도해주세요.");
        }

        if (adminService.login(loginRequest.getUsername(), loginRequest.getPassword())) {

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), null,
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            session.setAttribute(SessionConst.LOGIN_ADMIN, loginRequest.getUsername());
            session.setMaxInactiveInterval(1800);

            loginLogRepository.save(new LoginLog(
                    loginRequest.getUsername(), clientIp, true, LocalDateTime.now()));

            request.changeSessionId();

            return ResponseEntity.ok("관리자 로그인 성공");
        }

        session.setAttribute("loginFailCount", failCount + 1);
        session.setAttribute("lastFailTime", System.currentTimeMillis());

        loginLogRepository.save(new LoginLog(
                loginRequest.getUsername(), clientIp, false, LocalDateTime.now()));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("로그인 실패 (" + (failCount + 1) + "회)");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("로그아웃 완료");
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
