package Project.GDVCNS.service;

import Project.GDVCNS.dto.CategoryDTO;
import Project.GDVCNS.entity.Category;
import Project.GDVCNS.mapper.CategoryMapper;
import Project.GDVCNS.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // Lấy tất cả danh mục
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Lấy chi tiết theo ID
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));
        return categoryMapper.toDTO(category);
    }

    // Kiểm tra slug tồn tại
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }

    // Lưu (Tạo mới hoặc Cập nhật)
    @Transactional
    public void save(CategoryDTO dto) {
        Category category;

        // 1. Kiểm tra Create hay Update
        if (dto.getId() != null) {
            // Update: Fetch entity cũ
            category = categoryRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục để cập nhật"));
            category.setName(dto.getName());
            category.setType(dto.getType());
        } else {
            // Create: Tạo mới
            category = new Category();
            category.setName(dto.getName());
            category.setType(dto.getType());
        }

        // 2. Xử lý Slug (Backend backup nếu Frontend JS lỗi hoặc API được gọi trực tiếp)
        if (StringUtils.hasText(dto.getSlug())) {
            category.setSlug(dto.getSlug());
        } else {
            category.setSlug(generateSlug(dto.getName()));
        }

        // 3. Xử lý Parent Category
        if (dto.getParentId() != null) {
            // Validation: Không thể chọn chính mình làm cha
            if (dto.getId() != null && dto.getId().equals(dto.getParentId())) {
                throw new RuntimeException("Không thể chọn chính danh mục này làm danh mục cha.");
            }

            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Danh mục cha không tồn tại"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        categoryRepository.save(category);
    }

    // Xóa danh mục
    @Transactional
    public void delete(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy danh mục để xóa");
        }
    }

    // Utility: Tạo slug tiếng Việt
    private String generateSlug(String value) {
        if (!StringUtils.hasText(value)) return "";
        String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("")
                .toLowerCase()
                .replaceAll("đ", "d")
                .replaceAll("([^a-z0-9\\s-])", "") // Bỏ ký tự đặc biệt
                .replaceAll("\\s+", "-");          // Thay khoảng trắng bằng dấu gạch ngang
    }

    // THÊM HÀM NÀY
    public List<CategoryDTO> search(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}