package Project.GDVCNS.repository;

import Project.GDVCNS.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    // Thống kê lượt truy cập theo từng tháng trong năm cụ thể
    // Kết quả trả về List các mảng Object: [Tháng (int), Số lượng (long)]
    @Query("SELECT MONTH(u.createdAt), COUNT(u) FROM UserActivity u WHERE YEAR(u.createdAt) = :year GROUP BY MONTH(u.createdAt)")
    List<Object[]> countViewsByMonth(@Param("year") int year);}