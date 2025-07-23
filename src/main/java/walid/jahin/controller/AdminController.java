package walid.jahin.controller;

import walid.jahin.dto.LoginRequest;
import walid.jahin.model.SessionConst;
import walid.jahin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
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
        System.out.println("입력된 아이디: " + loginRequest.getUsername());
        System.out.println("입력된 비밀번호: " + loginRequest.getPassword());

        if (adminService.login(loginRequest.getUsername(), loginRequest.getPassword())) {
            // 여기서 세션 생성
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionConst.LOGIN_ADMIN, loginRequest.getUsername());
            return ResponseEntity.ok("관리자 로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
}

}