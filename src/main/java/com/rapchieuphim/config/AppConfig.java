package com.rapchieuphim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Cau hinh chung. Hien chi khai bao PasswordEncoder (BCrypt) de bam mat khau khi dang ky thanh vien.
 * Day la bo sung ngoai pham vi thiet ke goc (thiet ke khong co xac thuc dang nhap).
 */
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
