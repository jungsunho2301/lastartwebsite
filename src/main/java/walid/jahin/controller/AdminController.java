package walid.jahin.controller;

import walid.jahin.dto.LoginRequest;
import walid.jahin.model.SessionConst;
import walid.jahin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        if (adminService.login(loginRequest.getUsername(), loginRequest.getPassword())) {

            // ✅ 기존 세션 무효화
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // ✅ 새로운 세션 생성 후 로그인 정보 저장
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute(SessionConst.LOGIN_ADMIN, loginRequest.getUsername());

            return ResponseEntity.ok("관리자 로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }
}
