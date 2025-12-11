package Project.GDVCNS.utils;

import Project.GDVCNS.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    // Lấy user hiện tại đang đăng nhập
    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    // Kiểm tra xem có ai đang đăng nhập không
    public static boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    // Lấy Username hiện tại
    public static String getCurrentUsername() {
        CustomUserDetails user = getCurrentUser();
        return (user != null) ? user.getUsername() : null;
    }
}