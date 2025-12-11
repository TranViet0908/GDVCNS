package Project.GDVCNS.controller.admin;

import Project.GDVCNS.dto.CategoryDTO;
import Project.GDVCNS.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Hiển thị danh sách
    @GetMapping
    public String listCategories(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<CategoryDTO> list;

        if (keyword != null && !keyword.isEmpty()) {
            list = categoryService.search(keyword);
            model.addAttribute("keyword", keyword); // Để giữ lại từ khóa trong ô input
        } else {
            list = categoryService.findAll();
        }

        model.addAttribute("categories", list);
        return "admin/categories/index";
    }

    // 2. Form thêm mới
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        model.addAttribute("categories", categoryService.findAll()); // Để đổ vào select box cha-con
        return "admin/categories/form"; // Trả về templates/admin/categories/form.html
    }

    // 3. Form chỉnh sửa
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        CategoryDTO category = categoryService.findById(id);
        model.addAttribute("category", category);

        // Lấy list category để chọn Parent, loại bỏ chính nó để tránh vòng lặp cha-con vô hạn
        List<CategoryDTO> allCategories = categoryService.findAll();
        allCategories.removeIf(c -> c.getId().equals(id));
        model.addAttribute("categories", allCategories);

        return "admin/categories/form";
    }

    // 4. Xử lý Lưu (Create & Update chung)
    @PostMapping
    public String saveCategory(@ModelAttribute("category") CategoryDTO categoryDTO, RedirectAttributes redirectAttributes) {
        try {
            categoryService.save(categoryDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            // Nếu lỗi thì quay lại form new để nhập lại
            return "redirect:/admin/categories/new";
        }
        return "redirect:/admin/categories";
    }

    // Endpoint Update riêng cho path variable id (Mapping dự phòng cho form action /{id})
    @PostMapping("/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute("category") CategoryDTO categoryDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryDTO.setId(id); // Đảm bảo ID chính xác từ URL
            categoryService.save(categoryDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/categories/" + id + "/edit";
        }
        return "redirect:/admin/categories";
    }

    // 5. Xử lý Xóa
    @GetMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}