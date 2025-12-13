package Project.GDVCNS.repository;

import Project.GDVCNS.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph; // [MỚI] Import cái này
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findBySlug(String slug);

    // [FIX LỖI] Thêm @EntityGraph để fetch luôn category khi query
    // attributePaths = {"category"} trùng với tên biến 'category' trong Entity Course
    @EntityGraph(attributePaths = {"category"})
    Page<Course> findByIsActiveTrue(Pageable pageable);

    // [FIX LỖI] Thêm @EntityGraph cho hàm tìm kiếm
    @EntityGraph(attributePaths = {"category"})
    Page<Course> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    // [MỚI] Tìm kiếm theo tên NHƯNG phải đang Active
    @EntityGraph(attributePaths = {"category"})
    Page<Course> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    // [FIX LỖI] Override lại hàm findAll mặc định để fetch category khi không tìm kiếm
    @Override
    @EntityGraph(attributePaths = {"category"})
    Page<Course> findAll(Pageable pageable);

    boolean existsBySlug(String slug);
    List<Course> findTop6ByIsActiveTrueOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"category"})
    Page<Course> findByCategorySlugAndIsActiveTrue(String slug, Pageable pageable);

    // [MỚI] Thống kê khóa học mới theo tháng
    @Query("SELECT MONTH(c.createdAt), COUNT(c) FROM Course c WHERE YEAR(c.createdAt) = :year GROUP BY MONTH(c.createdAt)")
    List<Object[]> countCoursesByMonth(@Param("year") int year);
}