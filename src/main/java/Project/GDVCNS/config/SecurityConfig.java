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
                // 1. Cấu hình quyền truy cập (Authorization)
                .authorizeHttpRequests(auth -> auth
                        // A. TÀI NGUYÊN TĨNH
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/uploads/**", "/public/**").permitAll()

                        // B. TRANG LOGIN ADMIN
                        .requestMatchers("/admin/login", "/admin/perform_login").permitAll()

                        // C. CÁC TRANG PUBLIC (Khách vãng lai xem thoải mái)
                        .requestMatchers("/", "/home", "/gioi-thieu", "/lien-he", "/error").permitAll()
                        .requestMatchers("/chinh-sach-bao-mat", "/dieu-khoan-su-dung", "/cau-hoi-thuong-gap").permitAll() // <--- THÊM DÒNG NÀY

                        .requestMatchers("/tin-tuc/**", "/khoa-hoc/**", "/dich-vu/**", "/du-an/**").permitAll()
                        .requestMatchers("/api/**").permitAll()

                        // D. KHU VỰC ADMIN
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "EDITOR")

                        // E. CÁC TRANG CÒN LẠI
                        .anyRequest().authenticated()
                )

                // 2. Cấu hình Form Login
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/perform_login")
                        .defaultSuccessUrl("/admin/dashboard", true) // Login xong vào dashboard
                        .failureUrl("/admin/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )

                // 3. Cấu hình Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Logout xong về trang chủ cho thân thiện (hoặc về /admin/login)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // 4. Xử lý lỗi 403 (Không có quyền)
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403")
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