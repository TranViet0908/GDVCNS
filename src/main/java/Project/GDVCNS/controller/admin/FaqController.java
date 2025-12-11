package Project.GDVCNS.controller.admin;

import Project.GDVCNS.entity.Faq;
import Project.GDVCNS.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // 1. Danh sách FAQ (CÓ TÍCH HỢP TÌM KIẾM)
    @GetMapping
    public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        // Gọi hàm getAllFaqs có tham số keyword bên Service
        model.addAttribute("faqs", faqService.getAllFaqs(keyword));

        // Trả lại keyword về view để giữ lại text trong ô tìm kiếm
        model.addAttribute("keyword", keyword);

        model.addAttribute("pageTitle", "Quản lý FAQ");
        return "admin/faq/index";
    }

    // 2. Form Thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Faq faq = new Faq();
        faq.setIsActive(true); // Mặc định là hiện
        faq.setOrderIndex(1);  // Mặc định thứ tự là 1
        model.addAttribute("faq", faq);
        model.addAttribute("pageTitle", "Thêm câu hỏi mới");
        return "admin/faq/form";
    }

    // 3. Form Chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            // Dùng RuntimeException để code gọn hơn
            Faq faq = faqService.getFaqById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy FAQ ID: " + id));

            model.addAttribute("faq", faq);
            model.addAttribute("pageTitle", "Chỉnh sửa câu hỏi");
            return "admin/faq/form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/faqs";
        }
    }

    // 4. Xử lý Lưu (Create/Update)
    @PostMapping("/save")
    public String saveFaq(@ModelAttribute("faq") Faq faq, RedirectAttributes ra) {
        faqService.saveFaq(faq);
        ra.addFlashAttribute("successMessage", "Lưu dữ liệu thành công!");
        return "redirect:/admin/faqs";
    }

    // 5. Xóa
    @GetMapping("/delete/{id}")
    public String deleteFaq(@PathVariable Long id, RedirectAttributes ra) {
        faqService.deleteFaq(id);
        ra.addFlashAttribute("successMessage", "Đã xóa câu hỏi thành công.");
        return "redirect:/admin/faqs";
    }
}