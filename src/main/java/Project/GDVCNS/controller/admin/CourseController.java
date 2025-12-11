package Project.GDVCNS.controller.admin;

import Project.GDVCNS.dto.CourseDTO;
import Project.GDVCNS.entity.Course;
import Project.GDVCNS.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // ... (Giữ nguyên các hàm listCourses, showCreateForm) ...
    @GetMapping
    public String listCourses(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "") String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<Course> coursePage = courseService.getCourses(keyword, pageable);

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("totalItems", coursePage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "admin/courses/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("categories", courseService.getCourseCategories());
        model.addAttribute("pageTitle", "Thêm khóa học mới");
        return "admin/courses/form";
    }

    // [MỚI] Hiển thị trang chi tiết
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try {
            CourseDTO course = courseService.getCourseById(id);
            model.addAttribute("course", course);
            model.addAttribute("pageTitle", "Chi tiết khóa học: " + course.getName());
            return "admin/courses/detail"; // Trỏ về file detail.html
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy khóa học ID: " + id);
            return "redirect:/admin/courses";
        }
    }

    // [CHECK] Hàm Edit này đã đúng, nếu service fix lỗi Transactional thì sẽ chạy được
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try {
            CourseDTO course = courseService.getCourseById(id);
            model.addAttribute("course", course);
            model.addAttribute("categories", courseService.getCourseCategories());
            model.addAttribute("pageTitle", "Chỉnh sửa khóa học");
            return "admin/courses/form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/courses";
        }
    }

    // ... (Giữ nguyên saveCourse và deleteCourse) ...
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("course") CourseDTO courseDTO,
                             @RequestParam(value = "image", required = false) MultipartFile multipartFile,
                             RedirectAttributes ra) {
        try {
            courseService.saveCourse(courseDTO, multipartFile);
            ra.addFlashAttribute("successMessage", "Lưu khóa học thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/courses/new";
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            courseService.deleteCourse(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi xóa: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }
}