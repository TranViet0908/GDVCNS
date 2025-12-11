package Project.GDVCNS.repository;

import Project.GDVCNS.entity.User;
import Project.GDVCNS.enums.UserRole;
import Project.GDVCNS.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Tìm user đang hoạt động (ví dụ dùng cho login)
    Optional<User> findByUsernameAndStatus(String username, UserStatus status);

    // --- MỚI THÊM: Tìm kiếm và Phân trang ---
    @Query("SELECT u FROM User u WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            " u.fullName LIKE %:keyword% OR u.email LIKE %:keyword% OR u.username LIKE %:keyword%) " +
            "AND (:role IS NULL OR u.role = :role) " +
            "AND (:status IS NULL OR u.status = :status)")
    Page<User> searchUsers(@Param("keyword") String keyword,
                           @Param("role") UserRole role,
                           @Param("status") UserStatus status,
                           Pageable pageable);
}