package Project.GDVCNS.controller.admin;

import Project.GDVCNS.service.SystemConfigService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class AdminConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping
    public String viewConfigForm(Model model) {
        // Lấy Map config đẩy ra view để hiển thị vào các ô input
        model.addAttribute("configs", systemConfigService.getAllConfigs());
        return "admin/config/index"; // Đường dẫn file template
    }

    @PostMapping
    public String saveConfig(HttpServletRequest request,
                             @RequestParam(value = "file_logo", required = false) MultipartFile logo,
                             @RequestParam(value = "file_favicon", required = false) MultipartFile favicon,
                             RedirectAttributes redirectAttributes) {
        try {
            // Lấy tất cả params text từ form
            Map<String, String[]> parameterMap = request.getParameterMap();

            // Chuyển đổi Map<String, String[]> sang Map<String, String> để Service dễ xử lý
            Map<String, String> params = parameterMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

            systemConfigService.saveConfig(params, logo, favicon);

            redirectAttributes.addFlashAttribute("message", "Đã lưu cấu hình thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu file: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }

        return "redirect:/admin/config";
    }
}