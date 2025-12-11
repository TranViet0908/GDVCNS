package Project.GDVCNS.mapper;

import Project.GDVCNS.dto.UserDTO;
import Project.GDVCNS.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                // Không map password trả về DTO vì lý do bảo mật
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .status(dto.getStatus())
                // Password sẽ được set riêng sau khi mã hóa
                .build();
    }
}