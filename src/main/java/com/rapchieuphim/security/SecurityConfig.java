package com.rapchieuphim.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Cau hinh dang nhap va phan quyen theo 3 vai tro.
 * - Cong khai: trang chu, xem phim, dang ky thanh vien, trang dang nhap, tai nguyen tinh.
 * - /quan-ly/**  -> QUANLY;  /quay/** -> BANHANG hoac QUANLY;  /dat-ve, /ve-cua-toi -> KHACHHANG.
 * CSRF tat de don gian hoa (demo) — cac form POST hoat dong khong can token.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DangNhapThanhCongHandler dangNhapThanhCongHandler;

    public SecurityConfig(DangNhapThanhCongHandler dangNhapThanhCongHandler) {
        this.dangNhapThanhCongHandler = dangNhapThanhCongHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/error",
                                "/dang-nhap", "/thanh-vien/dang-ky", "/phim/**").permitAll()
                        .requestMatchers("/quan-ly/**").hasRole("QUANLY")
                        .requestMatchers("/quay/**").hasAnyRole("BANHANG", "QUANLY")
                        .requestMatchers("/dat-ve/**", "/ve-cua-toi/**").hasRole("KHACHHANG")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/dang-nhap")
                        .loginProcessingUrl("/dang-nhap")
                        .usernameParameter("email")
                        .passwordParameter("matKhau")
                        .successHandler(dangNhapThanhCongHandler)
                        .failureUrl("/dang-nhap?loi")
                        .permitAll())
                .logout(out -> out
                        .logoutRequestMatcher(new AntPathRequestMatcher("/dang-xuat"))
                        .logoutSuccessUrl("/?dangxuat")
                        .permitAll())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
