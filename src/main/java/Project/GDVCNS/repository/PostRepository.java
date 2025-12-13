package Project.GDVCNS.repository;

import Project.GDVCNS.entity.Post;
import Project.GDVCNS.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Optional<Post> findBySlug(String slug);

    // Lấy danh sách bài viết theo trạng thái (dùng cho trang Public)
    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    // Tìm bài viết theo danh mục
    Page<Post> findByCategoryIdAndStatus(Long categoryId, PostStatus status, Pageable pageable);

    // Tìm kiếm bài viết (Search)
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND (p.title LIKE %:keyword% OR p.summary LIKE %:keyword%)")
    Page<Post> searchPublishedPosts(String keyword, Pageable pageable);

    boolean existsBySlug(String slug);
    long countByCategoryId(Long categoryId);
    List<Post> findTop3ByStatusOrderByPublishedAtDesc(PostStatus status);

    // [MỚI] Thống kê bài viết mới theo tháng
    @Query("SELECT MONTH(p.createdAt), COUNT(p) FROM Post p WHERE YEAR(p.createdAt) = :year GROUP BY MONTH(p.createdAt)")
    List<Object[]> countPostsByMonth(@Param("year") int year);
}