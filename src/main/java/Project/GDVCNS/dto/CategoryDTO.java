package Project.GDVCNS.dto;

import Project.GDVCNS.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private CategoryType type;
    private Long parentId; // Chỉ giữ ID của cha để tránh loop vô hạn
    private String parentName; // Để hiển thị cho tiện
    private List<CategoryDTO> children; // Có thể load cây danh mục con
}