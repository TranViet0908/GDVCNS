package Project.GDVCNS.controller.admin;

import Project.GDVCNS.dto.UserDTO;
import Project.GDVCNS.enums.UserRole;
import Project.GDVCNS.enums.UserStatus;
import Project.GDVCNS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. Hiển thị danh sách (List + Search + Filter)
    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) UserRole role,
                            @RequestParam(required = false) UserStatus status) {

        Page<UserDTO> userPage = userService.getUsers(keyword, role, status, page, size);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());

        // Giữ lại các giá trị filter để hiển thị trên giao diện sau khi reload
        model.addAttribute("keyword", keyword);
        model.addAttribute("roleParam", role);
        model.addAttribute("statusParam", status);

        // Truyền Enum vào view để đổ dữ liệu cho Dropdown
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("statuses", UserStatus.values());

        return "admin/users/index"; // Đường dẫn tới file templates/admin/users/index.html
    }

    // 2. Form thêm mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("statuses", UserStatus.values());
        return "admin/users/form"; // Đường dẫn tới file templates/admin/users/form.html
    }

    // 3. Xử lý tạo mới
    @PostMapping
    public String createUser(@ModelAttribute("user") UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(userDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/users/new";
        }
        return "redirect:/admin/users";
    }

    // 4. Form cập nhật
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            UserDTO user = userService.getUserById(id);
            model.addAttribute("user", user);
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("statuses", UserStatus.values());
            return "admin/users/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy user!");
            return "redirect:/admin/users";
        }
    }

    // 5. Xử lý cập nhật
    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, userDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/users/" + id + "/edit";
        }
        return "redirect:/admin/users";
    }

    // 6. Khóa tài khoản (Soft Delete)
    @GetMapping("/{id}/lock")
    public String lockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.lockUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã khóa tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // 7. Mở khóa tài khoản
    @GetMapping("/{id}/unlock")
    public String unlockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.unlockUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã mở khóa tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}