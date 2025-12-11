package Project.GDVCNS.controller.advice;

import Project.GDVCNS.common.ConfigKey;
import Project.GDVCNS.entity.Category;
import Project.GDVCNS.enums.CategoryType;
import Project.GDVCNS.repository.CategoryRepository;
import Project.GDVCNS.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackages = "Project.GDVCNS.controller.web")
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final SystemConfigService systemConfigService;
    private final CategoryRepository categoryRepository;

    // 1. DATA CHO MENU DROPDOWN KHÓA HỌC (Biến tên 'services')
    @ModelAttribute("services")
    public List<Category> getCourseCategories() {
        return categoryRepository.findByType(CategoryType.COURSE);
    }

    // 2. DATA CHO MENU DROPDOWN TIN TỨC (Biến tên 'newsCategories')
    @ModelAttribute("newsCategories")
    public List<Category> getPostCategories() {
        return categoryRepository.findByType(CategoryType.POST);
    }

    // 3. DATA CẤU HÌNH HỆ THỐNG
    @ModelAttribute("globalConfigs")
    public Map<String, String> globalConfigs() {
        return systemConfigService.getAllConfigs();
    }

    // --- CÁC BIẾN RIÊNG LẺ CHO HEADER/FOOTER ---
    @ModelAttribute("siteLogo")
    public String siteLogo() {
        return systemConfigService.getAllConfigs().getOrDefault(ConfigKey.SITE_LOGO, "/public/images/logo-default.png");
    }

    @ModelAttribute("siteName")
    public String siteName() {
        return systemConfigService.getAllConfigs().getOrDefault(ConfigKey.COMPANY_NAME_VI, "GDVCNS");
    }

    @ModelAttribute("sitePhone")
    public String sitePhone() {
        return systemConfigService.getAllConfigs().getOrDefault(ConfigKey.HOTLINE, "");
    }

    @ModelAttribute("siteEmail")
    public String siteEmail() {
        return systemConfigService.getAllConfigs().getOrDefault(ConfigKey.EMAIL, "");
    }

    @ModelAttribute("siteAddress")
    public String siteAddress() {
        return systemConfigService.getAllConfigs().getOrDefault(ConfigKey.HEADQUARTERS, "");
    }

    @ModelAttribute("socialFacebook")
    public String socialFacebook() { return "#"; }
    @ModelAttribute("socialYoutube")
    public String socialYoutube() { return "#"; }
    @ModelAttribute("socialZalo")
    public String socialZalo() { return "#"; }
}