package Project.GDVCNS.service;

import Project.GDVCNS.entity.Contact;
import Project.GDVCNS.enums.ContactStatus;
import Project.GDVCNS.repository.ContactRepository;
import Project.GDVCNS.repository.PostRepository;
import Project.GDVCNS.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PostRepository postRepository;
    private final ContactRepository contactRepository;
    private final UserActivityRepository userActivityRepository;

    // 1. Tổng số bài viết
    public long getTotalPosts() {
        return postRepository.count();
    }

    // 2. Số liên hệ mới (Chỉ đếm trạng thái NEW)
    public long getNewContactsCount() {
        return contactRepository.countByStatus(ContactStatus.NEW);
    }

    // 3. Tổng lượt truy cập
    public long getTotalViews() {
        return userActivityRepository.count();
    }

    // 4. Số dự án hoạt động (Category ID = 4)
    public long getActiveProjects() {
        return postRepository.countByCategoryId(4L);
    }

    // 5. Lấy danh sách liên hệ gần đây
    public List<Contact> getRecentContacts() {
        return contactRepository.findTop5ByOrderByCreatedAtDesc();
    }
}