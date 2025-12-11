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

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final SystemConfigService systemConfigService;
    private final CourseService courseService;
    private final PostService postService;
    private final ContactService contactService;
    private final FaqRepository faqRepository;

    // Repositories & Mapper
    private final CourseRepository courseRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostMapper postMapper;

    // --- TRANG CHỦ ---
    @GetMapping("/")
    public String homePage(Model model) {
        Pageable coursePageable = PageRequest.of(0, 6, Sort.by("createdAt").descending());
        model.addAttribute("courses", courseService.getCourses(null, coursePageable).getContent());
        model.addAttribute("posts", postService.getLatestPosts(3).getContent());
        model.addAttribute("pageTitle", "Trang chủ - GDVCNS");
        return "public/index";
    }

    // --- GIỚI THIỆU ---
    @GetMapping("/gioi-thieu")
    public String aboutPage(Model model) {
        model.addAttribute("configs", systemConfigService.getAllConfigs());
        model.addAttribute("pageTitle", "Về chúng tôi - GDVCNS");
        return "public/about";
    }

    // ================= KHÓA HỌC (COURSES) =================

    @GetMapping("/khoa-hoc")
    public String coursesPage(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "9") int size,
                              @RequestParam(required = false) String keyword) { // [MỚI] Thêm param keyword

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Course> coursePage;

        // [LOGIC TÌM KIẾM]
        if (keyword != null && !keyword.trim().isEmpty()) {
            coursePage = courseRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword.trim(), pageable);
        } else {
            coursePage = courseService.getCourses(null, pageable);
        }

        model.addAttribute("courseList", coursePage);
        model.addAttribute("keyword", keyword); // Trả lại keyword để view hiển thị trong ô input
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("pageTitle", "Khóa học & Dịch vụ");
        return "public/courses/list";
    }

    @GetMapping("/khoa-hoc/danh-muc/{slug}")
    public String coursesByCategory(@PathVariable String slug,
                                    Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "9") int size) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Course> coursePage = courseService.getCoursesByCategorySlug(slug, pageable);

        model.addAttribute("courseList", coursePage);
        model.addAttribute("category", category);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("pageTitle", category.getName());
        return "public/courses/list";
    }

    @GetMapping("/khoa-hoc/{slug}")
    public String courseDetail(@PathVariable String slug, Model model) {
        Course course = courseRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));
        course.setViewCount(course.getViewCount() + 1);
        courseRepository.save(course);
        model.addAttribute("service", course);
        return "public/courses/detail";
    }


    // ================= TIN TỨC (NEWS) =================

    @GetMapping("/tin-tuc")
    public String newsPage(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String keyword) { // [MỚI] Thêm param keyword

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        Page<PostDTO> postPage;

        // [LOGIC TÌM KIẾM]
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm kiếm tất cả bài viết Published theo từ khóa
            Page<Post> posts = postRepository.searchPublishedPosts(keyword.trim(), pageable);
            // Convert Entity sang DTO
            postPage = posts.map(postMapper::toDTO);
        } else {
            // Mặc định lấy danh mục tin tức
            postPage = postService.getPostsByCategorySlug("tin-tuc-su-kien", page, size);
        }

        model.addAttribute("newsList", postPage);
        model.addAttribute("keyword", keyword); // Trả lại keyword

        model.addAttribute("recentNews", postService.getLatestPosts(5).getContent());
        model.addAttribute("pageTitle", "Tin tức & Sự kiện");
        return "public/news/list";
    }

    // ... (Các phần code dưới giữ nguyên: postsByCategory, newsDetail, Projects, Contact...)

    @GetMapping("/tin-tuc/danh-muc/{slug}")
    public String postsByCategory(@PathVariable String slug,
                                  Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        Page<PostDTO> postPage = postService.getPostsByCategorySlug(slug, page, size);

        model.addAttribute("newsList", postPage);
        model.addAttribute("projects", postPage);
        model.addAttribute("recentNews", postService.getLatestPosts(5).getContent());
        model.addAttribute("pageTitle", category.getName());

        if (slug.contains("du-an")) {
            return "public/projects/list";
        }
        return "public/news/list";
    }

    @GetMapping("/tin-tuc/{slug}")
    public String newsDetail(@PathVariable String slug, Model model) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        model.addAttribute("news", postMapper.toDTO(post));
        model.addAttribute("relatedNews", postService.getLatestPosts(5).getContent());
        return "public/news/detail";
    }

    // ... (Giữ nguyên phần Dự án và Liên hệ như file gốc của bạn) ...

    // ================= DỰ ÁN (PROJECTS) =================
    // (Đoạn này thêm tìm kiếm cho Dự án luôn nếu bạn cần, logic tương tự Tin tức)
    @GetMapping("/du-an")
    public String projectsPage(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "9") int size,
                               @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        Page<PostDTO> projectPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tận dụng hàm search của Post, nhưng có thể sẽ ra cả tin tức.
            // Nếu muốn chuẩn, cần viết thêm hàm searchByCategoryId trong Repo.
            // Tạm thời dùng search chung:
            Page<Post> posts = postRepository.searchPublishedPosts(keyword.trim(), pageable);
            projectPage = posts.map(postMapper::toDTO);
        } else {
            projectPage = postService.getPostsByCategorySlug("du-an-tieu-bieu", page, size);
        }

        model.addAttribute("projects", projectPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Dự án tiêu biểu");
        return "public/projects/list";
    }

    @GetMapping("/du-an/{slug}")
    public String projectDetail(@PathVariable String slug, Model model) {
        Post project = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Dự án không tồn tại"));
        project.setViewCount(project.getViewCount() + 1);
        postRepository.save(project);
        model.addAttribute("project", postMapper.toDTO(project));
        return "public/projects/detail";
    }

    // ================= LIÊN HỆ =================
    @GetMapping("/lien-he")
    public String contactPage(Model model) {
        model.addAttribute("pageTitle", "Liên hệ - GDVCNS");
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
        return "public/privacy";
    }

    @GetMapping("/dieu-khoan-su-dung")
    public String termsOfService(Model model) {
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

        return "public/faq";
    }
}