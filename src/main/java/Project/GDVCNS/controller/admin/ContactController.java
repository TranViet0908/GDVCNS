package Project.GDVCNS.controller.admin;

import Project.GDVCNS.dto.ContactDTO;
import Project.GDVCNS.enums.ContactStatus;
import Project.GDVCNS.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // 1. Hiển thị danh sách (có Search + Filter + Pagination)
    @GetMapping
    public String listContacts(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) ContactStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        // Mặc định sắp xếp ngày tạo mới nhất lên đầu
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ContactDTO> contactPage = contactService.getAllContacts(keyword, status, pageable);

        model.addAttribute("contacts", contactPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactPage.getTotalPages());
        model.addAttribute("totalItems", contactPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentStatus", status); // Để giữ lại trạng thái filter trên dropdown

        return "admin/contacts/index"; // File: templates/admin/contacts/index.html
    }

    // 2. Form Tạo mới
    @GetMapping("/create")
    public String createContactForm(Model model) {
        model.addAttribute("contact", new ContactDTO());
        model.addAttribute("pageTitle", "Tạo liên hệ mới");
        return "admin/contacts/form"; // File: templates/admin/contacts/form.html
    }

    // 3. Form Chỉnh sửa
    @GetMapping("/edit/{id}")
    public String editContactForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ContactDTO contactDTO = contactService.getContactById(id);
            model.addAttribute("contact", contactDTO);
            model.addAttribute("pageTitle", "Cập nhật liên hệ");
            return "admin/contacts/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy liên hệ!");
            return "redirect:/admin/contacts";
        }
    }

    // 4. Xử lý Lưu (Create & Update chung 1 hàm)
    @PostMapping("/save")
    public String saveContact(@ModelAttribute("contact") ContactDTO contactDTO, RedirectAttributes redirectAttributes) {
        try {
            contactService.saveContact(contactDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/contacts";
    }

    // 5. Xử lý Xóa
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            contactService.deleteContact(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa liên hệ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }
        return "redirect:/admin/contacts";
    }
}