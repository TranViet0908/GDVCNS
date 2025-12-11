package Project.GDVCNS.controller.admin;

import Project.GDVCNS.service.DashboardService;
import Project.GDVCNS.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 1. Dữ liệu cấu hình giao diện (Title, Active Menu, Breadcrumb)
        model.addAttribute("title", "Dashboard Quản trị");
        model.addAttribute("currentPage", "dashboard"); // Để highlight menu sidebar
        model.addAttribute("breadcrumb", "Tổng quan");

        // 2. Dữ liệu User đăng nhập
        model.addAttribute("username", SecurityUtils.getCurrentUsername());

        // 3. Dữ liệu Thống kê (Gọi từ Service)
        model.addAttribute("totalPosts", dashboardService.getTotalPosts());
        model.addAttribute("newContacts", dashboardService.getNewContactsCount());
        model.addAttribute("activeProjects", dashboardService.getActiveProjects());
        model.addAttribute("totalViews", dashboardService.getTotalViews());

        // 4. Danh sách liên hệ gần đây
        model.addAttribute("recentContacts", dashboardService.getRecentContacts());

        return "admin/dashboard";
    }
}