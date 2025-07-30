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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String clientIp = request.getRemoteAddr();

        // 로그인 실패 관련 세션 정보
        Integer failCount = (Integer) session.getAttribute("loginFailCount");
        Long lastFailTime = (Long) session.getAttribute("lastFailTime");
        if (failCount == null) failCount = 0;

        // 실패 후 10분 경과 시 초기화
        if (lastFailTime != null && System.currentTimeMillis() - lastFailTime > 10 * 60 * 1000) {
            failCount = 0;
            session.removeAttribute("loginFailCount");
            session.removeAttribute("lastFailTime");
        }

        // 로그인 차단 (5회 초과)
        if (failCount >= 5) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("로그인 5회 이상 실패. 10분 후 다시 시도해주세요.");
        }

        // 로그인 성공
        if (adminService.login(loginRequest.getUsername(), loginRequest.getPassword())) {
            session.invalidate(); // 기존 세션 무효화
            HttpSession newSession = request.getSession(true);
            request.changeSessionId(); // 세션 고정 공격 방지

            newSession.setAttribute(SessionConst.LOGIN_ADMIN, loginRequest.getUsername());
            newSession.setMaxInactiveInterval(1800); // 30분

            // ✅ 로그인 성공 로그 기록
            loginLogRepository.save(new LoginLog(
                    loginRequest.getUsername(), clientIp, true, LocalDateTime.now()
            ));

            return ResponseEntity.ok("관리자 로그인 성공");
        }

        // 로그인 실패 처리
        session.setAttribute("loginFailCount", failCount + 1);
        session.setAttribute("lastFailTime", System.currentTimeMillis());

        // ✅ 로그인 실패 로그 기록
        loginLogRepository.save(new LoginLog(
                loginRequest.getUsername(), clientIp, false, LocalDateTime.now()
        ));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("로그인 실패 (" + (failCount + 1) + "회)");
    }
}
