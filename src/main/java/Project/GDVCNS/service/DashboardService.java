package Project.GDVCNS.service;

import Project.GDVCNS.entity.Contact;
import Project.GDVCNS.enums.ContactStatus;
import Project.GDVCNS.repository.ContactRepository;
import Project.GDVCNS.repository.CourseRepository; // Import CourseRepo
import Project.GDVCNS.repository.PostRepository;
import Project.GDVCNS.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PostRepository postRepository;
    private final ContactRepository contactRepository;
    private final UserActivityRepository userActivityRepository;
    private final CourseRepository courseRepository; // Inject CourseRepo

    // 1. Các hàm thống kê cơ bản (Giữ nguyên)
    public long getTotalPosts() { return postRepository.count(); }
    public long getNewContactsCount() { return contactRepository.countByStatus(ContactStatus.NEW); }
    public long getTotalViews() { return userActivityRepository.count(); }
    public long getActiveProjects() { return postRepository.countByCategoryId(4L); } // Giả sử ID 4 là Dự án
    public List<Contact> getRecentContacts() { return contactRepository.findTop5ByOrderByCreatedAtDesc(); }

    // --- CÁC HÀM MỚI CHO BIỂU ĐỒ ---

    // 6. Lấy dữ liệu Traffic theo tháng (Năm hiện tại)
    public List<Integer> getMonthlyTraffic() {
        int currentYear = LocalDate.now().getYear();
        List<Object[]> rawData = userActivityRepository.countViewsByMonth(currentYear);
        return processMonthlyData(rawData);
    }

    // 7. Lấy dữ liệu Bài viết mới theo tháng
    public List<Integer> getMonthlyPosts() {
        int currentYear = LocalDate.now().getYear();
        List<Object[]> rawData = postRepository.countPostsByMonth(currentYear);
        return processMonthlyData(rawData);
    }

    // 8. Lấy dữ liệu Khóa học mới theo tháng
    public List<Integer> getMonthlyCourses() {
        int currentYear = LocalDate.now().getYear();
        List<Object[]> rawData = courseRepository.countCoursesByMonth(currentYear);
        return processMonthlyData(rawData);
    }

    // 9. Lấy dữ liệu Trạng thái Liên hệ [New, Processing, Done]
    public List<Integer> getContactStatusData() {
        List<Integer> data = new ArrayList<>();
        data.add((int) contactRepository.countByStatus(ContactStatus.NEW));
        data.add((int) contactRepository.countByStatus(ContactStatus.PROCESSING));
        data.add((int) contactRepository.countByStatus(ContactStatus.DONE));
        return data;
    }

    // --- HELPER: Chuyển dữ liệu thô từ DB thành mảng 12 tháng ---
    // Input: List<[Tháng, Số lượng]> (Ví dụ: [[1, 5], [3, 10]])
    // Output: List<Integer> [5, 0, 10, 0, 0, ...] (Đủ 12 phần tử)
    private List<Integer> processMonthlyData(List<Object[]> rawData) {
        // Khởi tạo mảng 12 phần tử giá trị 0
        int[] monthlyCounts = new int[12];

        for (Object[] row : rawData) {
            // DB trả về tháng từ 1-12
            int month = (int) row[0];
            long count = (long) row[1];

            // Mảng index từ 0-11 nên phải trừ 1
            if (month >= 1 && month <= 12) {
                monthlyCounts[month - 1] = (int) count;
            }
        }

        // Chuyển int[] sang List<Integer>
        List<Integer> result = new ArrayList<>();
        for (int count : monthlyCounts) {
            result.add(count);
        }
        return result;
    }
}