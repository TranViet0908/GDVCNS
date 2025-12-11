package Project.GDVCNS.repository;

import Project.GDVCNS.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    // Mặc định đã có hàm count()
}