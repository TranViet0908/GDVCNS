package Project.GDVCNS.repository;

import Project.GDVCNS.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findBySlug(String slug);

    @EntityGraph(attributePaths = {"category"})
    Page<Course> findByIsActiveTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    Page<Course> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    Page<Course> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

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

    // [MỚI - RECOMMENDATION] Lấy 5 khóa học liên quan cùng danh mục, TRỪ khóa hiện tại
    @EntityGraph(attributePaths = {"category"})
    List<Course> findTop5ByCategoryIdAndIsActiveTrueAndIdNotOrderByCreatedAtDesc(Long categoryId, Long excludeCourseId);
}