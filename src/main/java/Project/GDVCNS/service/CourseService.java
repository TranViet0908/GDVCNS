package Project.GDVCNS.service;

import Project.GDVCNS.dto.CourseDTO;
import Project.GDVCNS.entity.Category;
import Project.GDVCNS.entity.Course;
import Project.GDVCNS.enums.CategoryType;
import Project.GDVCNS.mapper.CourseMapper;
import Project.GDVCNS.repository.CategoryRepository;
import Project.GDVCNS.repository.CourseRepository;
import Project.GDVCNS.utils.FileUploadUtils;
import Project.GDVCNS.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import này
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional // [FIX LỖI] Thêm Transactional cho toàn bộ Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true) // Tối ưu hiệu năng cho các hàm đọc
    public List<Category> getCourseCategories() {
        return categoryRepository.findByType(CategoryType.COURSE);
    }

    @Transactional(readOnly = true)
    public Page<Course> getCoursesByCategorySlug(String categorySlug, Pageable pageable) {
        // Cần thêm hàm này trong CourseRepository: Page<Course> findByCategorySlugAndIsActiveTrue(String slug, Pageable pageable);
        // HOẶC dùng Specification tương tự PostService.
        // Cách nhanh nhất hiện tại dùng Repository:
        return courseRepository.findByCategorySlugAndIsActiveTrue(categorySlug, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Course> getCourses(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return courseRepository.findAllByNameContainingIgnoreCase(keyword.trim(), pageable);
        }
        return courseRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));
        // Nhờ @Transactional, session vẫn mở để lấy Category lazy load tại đây
        return courseMapper.toDTO(course);
    }

    // Các hàm save, delete giữ nguyên logic cũ (đã có @Transactional mặc định từ class)
    public void saveCourse(CourseDTO dto, MultipartFile multipartFile) throws IOException {
        Course course;
        if (dto.getId() != null) {
            course = courseRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học cần sửa"));
        } else {
            course = new Course();
            course.setCreatedAt(LocalDateTime.now());
            course.setViewCount(0L);
        }

        course.setName(dto.getName());
        course.setSummary(dto.getSummary());
        course.setContent(dto.getContent());
        course.setDuration(dto.getDuration());
        course.setTargetAudience(dto.getTargetAudience());
        course.setTrainingFormat(dto.getTrainingFormat());
        course.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : false);

        if (dto.getSlug() == null || dto.getSlug().trim().isEmpty()) {
            course.setSlug(SlugUtils.makeSlug(dto.getName()));
        } else {
            course.setSlug(dto.getSlug());
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            course.setCategory(category);
        } else {
            course.setCategory(null);
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
            }
            String storedFileName = course.getSlug() + "-" + System.currentTimeMillis() + fileExtension;
            String uploadDir = "uploads/courses";
            FileUploadUtils.saveFile(uploadDir, storedFileName, multipartFile);
            course.setThumbnailUrl("/uploads/courses/" + storedFileName);
        }

        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Khóa học không tồn tại!");
        }
        courseRepository.deleteById(id);
    }
}