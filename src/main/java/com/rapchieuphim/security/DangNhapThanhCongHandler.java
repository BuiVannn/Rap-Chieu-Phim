package com.rapchieuphim.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Sau khi dang nhap, dieu huong ve khu vuc dung voi vai tro.
 */
@Component
public class DangNhapThanhCongHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> vaiTro = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String dich;
        if (vaiTro.contains("ROLE_QUANLY")) {
            dich = "/quan-ly";
        } else if (vaiTro.contains("ROLE_BANHANG")) {
            dich = "/quay/ban-ve/tim-suat-chieu";
        } else {
            dich = "/"; // Khach hang
        }
        response.sendRedirect(request.getContextPath() + dich);
    }
}
