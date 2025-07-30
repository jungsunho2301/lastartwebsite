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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final LoginLogRepository loginLogRepository;

    public AdminController(AdminService adminService, LoginLogRepository loginLogRepository) {
        this.adminService = adminService;
        this.loginLogRepository = loginLogRepository;
    }

    // ✅ 관리자 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String clientIp = request.getRemoteAddr();

        Integer failCount = (Integer) session.getAttribute("loginFailCount");
        Long lastFailTime = (Long) session.getAttribute("lastFailTime");
        if (failCount == null) failCount = 0;

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
            session.invalidate();
            HttpSession newSession = request.getSession(true);
            request.changeSessionId();

            newSession.setAttribute(SessionConst.LOGIN_ADMIN, loginRequest.getUsername());
            newSession.setMaxInactiveInterval(1800); // 30분

            loginLogRepository.save(new LoginLog(
                    loginRequest.getUsername(), clientIp, true, LocalDateTime.now()
            ));

            return ResponseEntity.ok("관리자 로그인 성공");
        }

        session.setAttribute("loginFailCount", failCount + 1);
        session.setAttribute("lastFailTime", System.currentTimeMillis());

        loginLogRepository.save(new LoginLog(
                loginRequest.getUsername(), clientIp, false, LocalDateTime.now()
        ));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("로그인 실패 (" + (failCount + 1) + "회)");
    }

    // ✅ 관리자 세션 확인 API
    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String admin = (String) session.getAttribute(SessionConst.LOGIN_ADMIN);
        if (admin != null) {
            return ResponseEntity.ok("authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthenticated");
        }
    }

    // ✅ 로그아웃 처리 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 완료");
    }
}
