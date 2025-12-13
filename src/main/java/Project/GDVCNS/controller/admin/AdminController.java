package Project.GDVCNS.controller.admin;

import Project.GDVCNS.service.DashboardService;
import Project.GDVCNS.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 1. Cấu hình giao diện
        model.addAttribute("title", "Dashboard Quản trị");
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("breadcrumb", "Tổng quan");

        // 2. User info
        model.addAttribute("username", SecurityUtils.getCurrentUsername());

        // 3. Thống kê tổng quan (Cards) - Dữ liệu thật
        model.addAttribute("totalPosts", dashboardService.getTotalPosts());
        model.addAttribute("newContacts", dashboardService.getNewContactsCount());
        model.addAttribute("activeProjects", dashboardService.getActiveProjects());
        model.addAttribute("totalViews", dashboardService.getTotalViews());

        // 4. Bảng liên hệ gần đây - Dữ liệu thật
        model.addAttribute("recentContacts", dashboardService.getRecentContacts());

        // 5. BIỂU ĐỒ (CHARTS) - DỮ LIỆU THẬT TỪ DB

        // Chart 1: Traffic (Line)
        List<Integer> trafficData = dashboardService.getMonthlyTraffic();
        model.addAttribute("trafficData", trafficData);

        // Label cho trục hoành (cố định 12 tháng)
        List<String> trafficLabels = Arrays.asList(
                "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"
        );
        model.addAttribute("trafficLabels", trafficLabels);

        // Chart 2: Contact Status (Doughnut)
        List<Integer> contactStatusData = dashboardService.getContactStatusData();
        model.addAttribute("contactStatusData", contactStatusData);

        // Chart 3: Content Growth (Bar) - Bài viết vs Khóa học
        List<Integer> postsGrowth = dashboardService.getMonthlyPosts();
        List<Integer> coursesGrowth = dashboardService.getMonthlyCourses();
        model.addAttribute("postsGrowth", postsGrowth);
        model.addAttribute("coursesGrowth", coursesGrowth);

        return "admin/dashboard";
    }
}