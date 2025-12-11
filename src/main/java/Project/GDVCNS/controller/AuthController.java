package Project.GDVCNS.controller;

import Project.GDVCNS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // URL này phải khớp với .loginPage("/admin/login") trong SecurityConfig
    @GetMapping("/admin/login")
    public String loginPage() {
        // Trả về file templates/admin/login.html
        return "admin/login";
    }
}