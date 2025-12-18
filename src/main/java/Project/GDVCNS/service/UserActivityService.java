package Project.GDVCNS.service;

import Project.GDVCNS.entity.UserActivity;
import Project.GDVCNS.enums.ActivityType;
import Project.GDVCNS.repository.UserActivityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;

    /**
     * Ghi lại hành vi người dùng (Log Tracking)
     * @param request: Để lấy Session ID và IP Address
     * @param type: Loại hành vi (VIEW_POST, VIEW_COURSE)
     * @param targetId: ID của đối tượng được xem
     */
    public void logActivity(HttpServletRequest request, ActivityType type, Long targetId) {
        try {
            // 1. Lấy Session ID (nếu chưa có thì tạo mới)
            HttpSession session = request.getSession(true);
            String sessionId = session.getId();

            // 2. Lấy IP Address
            String ipAddress = getClientIp(request);

            // 3. Tạo bản ghi
            UserActivity activity = UserActivity.builder()
                    .sessionId(sessionId)
                    .activityType(type)
                    .targetId(targetId)
                    .ipAddress(ipAddress)
                    .build();

            // 4. Lưu vào DB
            userActivityRepository.save(activity);

        } catch (Exception e) {
            // Log lỗi nhưng không làm gián đoạn trải nghiệm người dùng
            System.err.println("Lỗi khi lưu User Activity: " + e.getMessage());
        }
    }

    // Hàm phụ trợ lấy IP chuẩn (xử lý trường hợp qua Proxy/Load Balancer)
    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}