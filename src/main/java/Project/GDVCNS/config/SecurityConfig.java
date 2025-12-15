package Project.GDVCNS.config;

import Project.GDVCNS.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Cho phép tài nguyên tĩnh
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/uploads/**", "/public/**").permitAll()

                        // 2. Cho phép trang Login & Logout (Quan trọng)
                        .requestMatchers("/admin/login", "/admin/perform_login", "/logout").permitAll()

                        // 3. Cho phép các trang Public (Người dùng xem)
                        .requestMatchers("/", "/home", "/gioi-thieu", "/lien-he", "/error").permitAll()
                        .requestMatchers("/chinh-sach-bao-mat", "/dieu-khoan-su-dung", "/cau-hoi-thuong-gap").permitAll()
                        .requestMatchers("/tin-tuc/**", "/khoa-hoc/**", "/dich-vu/**", "/du-an/**").permitAll()
                        .requestMatchers("/api/**").permitAll()

                        // 4. BẮT BUỘC ĐĂNG NHẬP CHO TRANG ADMIN
                        // Đoạn này sẽ chặn tất cả truy cập vào /admin/... nếu chưa login hoặc không đủ quyền
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "EDITOR")

                        // 5. Các request còn lại phải xác thực
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/perform_login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/admin/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        // [FIX LỖI] Logout xong phải về trang login, không được về dashboard
                        .logoutSuccessUrl("/admin/login?logout=true")
                        .invalidateHttpSession(true) // Hủy session
                        .deleteCookies("JSESSIONID") // Xóa cookie
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403") // Trang lỗi khi không đủ quyền
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}