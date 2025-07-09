package walid.jahin.controller;

import walid.jahin.dto.LoginRequest;
import walid.jahin.model.SessionConst;
import walid.jahin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request) {
        if (adminService.login(loginRequest.getUsername(), loginRequest.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_ADMIN, loginRequest.getUsername());
            return "redirect:/admin/dashboard.html";  // ✅ 수정된 리다이렉트 경로
        }
        return "redirect:/admin/login.html?error";  // ❗실패 시 경로도 수정
    }
}
