package Project.GDVCNS.service;

import Project.GDVCNS.dto.UserDTO;
import Project.GDVCNS.entity.User;
import Project.GDVCNS.enums.UserRole;
import Project.GDVCNS.enums.UserStatus;
import Project.GDVCNS.exception.AppException;
import Project.GDVCNS.mapper.UserMapper;
import Project.GDVCNS.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // --- 1. CHỨC NĂNG TỰ ĐỘNG TẠO ADMIN (DATA SEEDING) ---
    @PostConstruct
    public void initAdminAccount() {
        // Kiểm tra xem đã có tài khoản admin nào chưa
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Mật khẩu mặc định
            admin.setFullName("Administrator");
            admin.setEmail("admin@gdvcns.vn");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);

            userRepository.save(admin);
            System.out.println(">>> ĐÃ TẠO TÀI KHOẢN ADMIN MẶC ĐỊNH: admin / admin123");
        }
    }

    // --- 2. CÁC CHỨC NĂNG NGHIỆP VỤ ---

    // Tạo người dùng mới (Dùng cho Admin tạo Editor hoặc User đăng ký)
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        // 1. Kiểm tra trùng lặp
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new AppException("Username đã tồn tại: " + dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AppException("Email đã tồn tại: " + dto.getEmail());
        }

        // 2. Map từ DTO sang Entity
        User user = userMapper.toEntity(dto);

        // 3. Xử lý mật khẩu (Mã hóa)
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 4. Set mặc định nếu thiếu
        if (user.getRole() == null) user.setRole(UserRole.EDITOR);
        if (user.getStatus() == null) user.setStatus(UserStatus.ACTIVE);

        // 5. Lưu và trả về
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // Lấy danh sách tất cả user
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Lấy chi tiết user
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Không tìm thấy user ID: " + id));
        return userMapper.toDTO(user);
    }

    // Cập nhật thông tin user
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Không tìm thấy user để update"));

        // Chỉ cập nhật các trường cho phép
        existingUser.setFullName(dto.getFullName());
        existingUser.setEmail(dto.getEmail());

        // Nếu có gửi role/status lên thì mới update (chỉ admin mới gửi mấy cái này)
        if (dto.getRole() != null) existingUser.setRole(dto.getRole());
        if (dto.getStatus() != null) existingUser.setStatus(dto.getStatus());

        // Nếu muốn đổi mật khẩu thì xử lý riêng (thường làm API riêng)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    // Xóa user (Khóa tài khoản thay vì xóa vĩnh viễn)
    @Transactional
    public void lockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found"));
        user.setStatus(UserStatus.LOCKED);
        userRepository.save(user);
    }

    // --- 3. CÁC CHỨC NĂNG MỚI CHO ADMIN UI (THÊM VÀO DƯỚI ĐÂY) ---

    // Tìm kiếm và Phân trang (Dùng cho bảng dữ liệu admin)
    public Page<UserDTO> getUsers(String keyword, UserRole role, UserStatus status, int page, int size) {
        // Sắp xếp mặc định theo ngày tạo mới nhất
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        // Gọi hàm searchUsers đã định nghĩa trong UserRepository
        Page<User> userPage = userRepository.searchUsers(keyword, role, status, pageable);

        return userPage.map(userMapper::toDTO);
    }

    // Mở khóa tài khoản (Ngược lại với lockUser)
    @Transactional
    public void unlockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found"));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}