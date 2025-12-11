package Project.GDVCNS.controller.admin;

import Project.GDVCNS.dto.PostDTO;
import Project.GDVCNS.entity.FileStorage;
import Project.GDVCNS.entity.User;
import Project.GDVCNS.enums.PostStatus;
import Project.GDVCNS.repository.CategoryRepository;
import Project.GDVCNS.repository.UserRepository;
import Project.GDVCNS.service.FileStorageService;
import Project.GDVCNS.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    // 1. Hiển thị danh sách
    @GetMapping
    public String listPosts(Model model,
                            // --- KHAI BÁO ĐẦY ĐỦ THAM SỐ ĐỂ TRÁNH LỖI ĐỎ TRONG IDE ---
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) Long categoryId,
                            @RequestParam(required = false) PostStatus status,
                            @RequestParam(required = false) String type,
                            @RequestParam(defaultValue = "0") int page) {

        Page<PostDTO> postPage = postService.searchPosts(keyword, categoryId, status, type, page, 10);

        // --- LOGIC PHÂN QUYỀN CHUẨN ---
        Long currentUserId = null;
        boolean isAdmin = false;

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            currentUser = userRepository.findByEmail(currentUsername).orElse(null);
        }

        if (currentUser != null) {
            currentUserId = currentUser.getId();
            isAdmin = currentUser.getRole().name().equals("ADMIN");
        }

        // Luôn luôn add attribute, kể cả khi null
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("isAdmin", isAdmin);
        // -------------------------------

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", "posts");
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalItems", postPage.getTotalElements());
        model.addAttribute("currentPageNum", page);

        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("status", status);
        model.addAttribute("type", type);
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/posts/index";
    }

    // 2. Form thêm mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new PostDTO());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentPage", "posts");
        return "admin/posts/form";
    }

    // 3. Xử lý tạo mới
    @PostMapping
    public String createPost(@ModelAttribute PostDTO postDTO,
                             @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
                             RedirectAttributes redirectAttributes) {
        try {
            if (!thumbnailFile.isEmpty()) {
                FileStorage fileStorage = fileStorageService.storeFile(thumbnailFile);
                postDTO.setThumbnailUrl(fileStorage.getFilePath());
            }
            postService.createPost(postDTO);
            redirectAttributes.addFlashAttribute("message", "Tạo bài viết thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Lỗi: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/admin/posts/new";
        }
        return "redirect:/admin/posts";
    }

    // 4. Form chỉnh sửa
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PostDTO postDTO = postService.getPostById(id);
            model.addAttribute("post", postDTO);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("currentPage", "posts");
            return "admin/posts/form";
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("message", "Bạn không có quyền sửa bài này!");
            redirectAttributes.addFlashAttribute("messageType", "warning");
            return "redirect:/admin/posts";
        } catch (Exception e) {
            return "redirect:/admin/posts";
        }
    }

    // 5. Xử lý cập nhật
    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id,
                             @ModelAttribute PostDTO postDTO,
                             @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
                             RedirectAttributes redirectAttributes) {
        try {
            if (!thumbnailFile.isEmpty()) {
                FileStorage fileStorage = fileStorageService.storeFile(thumbnailFile);
                postDTO.setThumbnailUrl(fileStorage.getFilePath());
            }
            postService.updatePost(id, postDTO);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("message", "Không có quyền sửa!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/admin/posts";
    }

    // 6. Xóa
    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            postService.deletePost(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa bài viết!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi xóa!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/admin/posts";
    }

    // 7. Xem chi tiết bài viết (Preview) - ĐÃ FIX LỖI 500
    @GetMapping("/{id}/detail")
    public String viewPostDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PostDTO postDTO = postService.getPostById(id);

            // --- FIX LOGIC TÌM USER & SET MẶC ĐỊNH ---
            Long currentUserId = null;
            boolean isAdmin = false;

            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            // Ưu tiên tìm theo Username trước
            User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
            if (currentUser == null) {
                currentUser = userRepository.findByEmail(currentUsername).orElse(null);
            }

            if (currentUser != null) {
                currentUserId = currentUser.getId();
                isAdmin = currentUser.getRole().name().equals("ADMIN");
            }

            // Luôn thêm vào model để Thymeleaf không bị lỗi null pointer
            model.addAttribute("currentUserId", currentUserId);
            model.addAttribute("isAdmin", isAdmin);
            // -----------------------------------------

            model.addAttribute("post", postDTO);
            model.addAttribute("currentPage", "posts");
            return "admin/posts/detail";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy bài viết!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/admin/posts";
        }
    }
}