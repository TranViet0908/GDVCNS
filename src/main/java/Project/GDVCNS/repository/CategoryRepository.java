package Project.GDVCNS.repository;

import Project.GDVCNS.entity.Category;
import Project.GDVCNS.entity.Course;
import Project.GDVCNS.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    List<Category> findByType(CategoryType type);

    // Tìm các danh mục gốc (không có cha)
    List<Category> findByParentIsNull();

    boolean existsBySlug(String slug);
    List<Category> findByNameContainingIgnoreCase(String name);
}