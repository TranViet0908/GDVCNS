package Project.GDVCNS.security;

import Project.GDVCNS.entity.User;
import Project.GDVCNS.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security yêu cầu Role thường bắt đầu bằng prefix "ROLE_"
        // Ví dụ: ADMIN -> ROLE_ADMIN
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
    public String getFullName() {
        if (user != null && user.getFullName() != null) {
            return user.getFullName();
        }
        return user != null ? user.getUsername() : "Admin";
    }
    // --- [SỬA LẠI: TRẢ VỀ NULL ĐỂ TRÁNH LỖI] ---
    public String getAvatar() {
        return null; // Vì DB không có, ta trả về null luôn
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Tài khoản không bao giờ hết hạn (trừ khi bạn muốn thêm logic này)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Kiểm tra tài khoản có bị khóa không (dựa vào Enum UserStatus)
    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == UserStatus.ACTIVE;
    }

    // Chứng thực (mật khẩu) không bao giờ hết hạn
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Tài khoản được kích hoạt
    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }
}