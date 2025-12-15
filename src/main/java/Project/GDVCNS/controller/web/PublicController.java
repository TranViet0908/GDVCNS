package Project.GDVCNS.controller.web;

import Project.GDVCNS.dto.ContactDTO;
import Project.GDVCNS.dto.PostDTO;
import Project.GDVCNS.entity.Category;
import Project.GDVCNS.entity.Course;
import Project.GDVCNS.entity.Faq;
import Project.GDVCNS.entity.Post;
import Project.GDVCNS.enums.ContactStatus;
import Project.GDVCNS.mapper.PostMapper;
import Project.GDVCNS.repository.CategoryRepository;
import Project.GDVCNS.repository.CourseRepository;
import Project.GDVCNS.repository.FaqRepository;
import Project.GDVCNS.repository.PostRepository;
import Project.GDVCNS.service.ContactService;
import Project.GDVCNS.service.CourseService;
import Project.GDVCNS.service.PostService;
import Project.GDVCNS.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final SystemConfigService systemConfigService;
    private final CourseService courseService;
    private final PostService postService;
    private final ContactService contactService;
    private final FaqRepository faqRepository;

    private final CourseRepository courseRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostMapper postMapper;

    // --- HÀM HỖ TRỢ LẤY CONFIGS ---
    private Map<String, String> getGlobalConfigs() {
        // Vì service đã trả về Map rồi nên lấy dùng luôn
        return systemConfigService.getAllConfigs();
    }

    // --- TRANG CHỦ ---
    @GetMapping("/")
    public String homePage(Model model) {
        // [FIX] Sort by createdAt DESC (Mới tạo lên đầu)
        Pageable coursePageable = PageRequest.of(0, 6, Sort.by("createdAt").descending());

        // Bài viết cũng sort theo createdAt DESC
        model.addAttribute("courses", courseService.getCourses(null, coursePageable).getContent());

        // Lưu ý: getLatestPosts trong Service đang dùng hàm getPostsByCategorySlug, ta cần sửa hàm đó trong Service (Bước 4) hoặc sửa trực tiếp ở đây nếu gọi repository.
        // Để đồng bộ, tôi sẽ sửa hàm getLatestPosts trong PostService ở Bước 4.
        model.addAttribute("posts", postService.getLatestPosts(3).getContent());

        model.addAttribute("globalConfigs", getGlobalConfigs());
        model.addAttribute("pageTitle", "Trang chủ - GDVCNS");
        return "public/index";
    }

    // --- GIỚI THIỆU ---
    @GetMapping("/gioi-thieu")
    public String aboutPage(Model model) {
        model.addAttribute("globalConfigs", getGlobalConfigs());
        model.addAttribute("pageTitle", "Về chúng tôi - GDVCNS");
        return "public/about";
    }

    // ================= KHÓA HỌC (COURSES) =================

    @GetMapping("/khoa-hoc")
    public String coursesPage(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "9") int size,
                              @RequestParam(required = false) String keyword) {

        // [FIX] Sort by createdAt DESC
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Course> coursePage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            coursePage = courseRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword.trim(), pageable);
        } else {
            coursePage = courseService.getCourses(null, pageable);
        }

        model.addAttribute("courseList", coursePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("pageTitle", "Khóa học & Dịch vụ");
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/courses/list";
    }

    @GetMapping("/khoa-hoc/danh-muc/{slug}")
    public String coursesByCategory(@PathVariable String slug,
                                    Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "9") int size) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        // [FIX] Sort by createdAt DESC
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Course> coursePage = courseService.getCoursesByCategorySlug(slug, pageable);

        model.addAttribute("courseList", coursePage);
        model.addAttribute("category", category);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/courses/list";
    }

    @GetMapping("/khoa-hoc/{slug}")
    public String courseDetail(@PathVariable String slug, Model model) {
        Course course = courseRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        Long currentView = course.getViewCount() == null ? 0L : course.getViewCount();
        course.setViewCount(currentView + 1);
        courseRepository.save(course);

        model.addAttribute("service", course);
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/courses/detail";
    }


    // ================= TIN TỨC (NEWS) =================

    @GetMapping("/tin-tuc")
    public String newsPage(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String keyword) {

        // [FIX] Sort by createdAt DESC
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostDTO> postPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            Page<Post> posts = postRepository.searchPublishedPosts(keyword.trim(), pageable);
            postPage = posts.map(postMapper::toDTO);
        } else {
            postPage = postService.getPostsByCategorySlug("tin-tuc-su-kien", page, size);
        }

        model.addAttribute("newsList", postPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("recentNews", postService.getLatestPosts(5).getContent());
        model.addAttribute("pageTitle", "Tin tức & Sự kiện");
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/news/list";
    }

    @GetMapping("/tin-tuc/danh-muc/{slug}")
    public String postsByCategory(@PathVariable String slug,
                                  Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        // Hàm này trong service đã được sửa sort theo createdAt (xem Bước 4)
        Page<PostDTO> postPage = postService.getPostsByCategorySlug(slug, page, size);

        model.addAttribute("newsList", postPage);
        model.addAttribute("projects", postPage);
        model.addAttribute("recentNews", postService.getLatestPosts(5).getContent());
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("globalConfigs", getGlobalConfigs());

        if (slug.contains("du-an")) {
            return "public/projects/list";
        }
        return "public/news/list";
    }

    @GetMapping("/tin-tuc/{slug}")
    public String newsDetail(@PathVariable String slug, Model model) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        Long currentView = post.getViewCount() == null ? 0L : post.getViewCount();
        post.setViewCount(currentView + 1);
        postRepository.save(post);

        model.addAttribute("news", postMapper.toDTO(post));
        model.addAttribute("relatedNews", postService.getLatestPosts(5).getContent());
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/news/detail";
    }

    // ================= DỰ ÁN (PROJECTS) =================
    @GetMapping("/du-an")
    public String projectsPage(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "9") int size,
                               @RequestParam(required = false) String keyword) {

        // [FIX] createdAt DESC
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostDTO> projectPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            Page<Post> posts = postRepository.searchPublishedPosts(keyword.trim(), pageable);
            projectPage = posts.map(postMapper::toDTO);
        } else {
            projectPage = postService.getPostsByCategorySlug("du-an-tieu-bieu", page, size);
        }

        model.addAttribute("projects", projectPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Dự án tiêu biểu");
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/projects/list";
    }

    @GetMapping("/du-an/{slug}")
    public String projectDetail(@PathVariable String slug, Model model) {
        Post project = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Dự án không tồn tại"));

        Long currentView = project.getViewCount() == null ? 0L : project.getViewCount();
        project.setViewCount(currentView + 1);
        postRepository.save(project);

        model.addAttribute("project", postMapper.toDTO(project));
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/projects/detail";
    }

    // ================= LIÊN HỆ =================
    @GetMapping("/lien-he")
    public String contactPage(Model model) {
        model.addAttribute("pageTitle", "Liên hệ - GDVCNS");
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/contact";
    }

    @PostMapping("/lien-he")
    public String submitContact(@ModelAttribute ContactDTO contactDTO, RedirectAttributes redirectAttributes) {
        try {
            contactDTO.setStatus(ContactStatus.NEW);
            contactService.saveContact(contactDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Gửi liên hệ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại.");
        }
        return "redirect:/lien-he";
    }

    @GetMapping("/chinh-sach-bao-mat")
    public String privacyPolicy(Model model) {
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/privacy";
    }

    @GetMapping("/dieu-khoan-su-dung")
    public String termsOfService(Model model) {
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/terms";
    }

    @GetMapping("/cau-hoi-thuong-gap")
    public String faqPage(Model model, @RequestParam(required = false) String keyword) {
        List<Faq> faqs;
        if (keyword != null && !keyword.trim().isEmpty()) {
            faqs = faqRepository.searchActiveFaqs(keyword.trim());
        } else {
            faqs = faqRepository.findByIsActiveTrueOrderByOrderIndexAsc();
        }
        model.addAttribute("faqList", faqs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Câu hỏi thường gặp - GDVCNS");
        model.addAttribute("globalConfigs", getGlobalConfigs());
        return "public/faq";
    }
}