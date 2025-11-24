package walid.jahin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard"; // 권한 없으면 자동 403 처리됨
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }
}
