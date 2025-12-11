package Project.GDVCNS.dto;

import Project.GDVCNS.enums.UserRole;
import Project.GDVCNS.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private UserRole role;
    private UserStatus status;
    private String password; // Chỉ dùng khi tạo mới/đổi pass, cẩn thận khi trả về client
    private LocalDateTime createdAt;
}