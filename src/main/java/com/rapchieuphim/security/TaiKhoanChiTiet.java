package com.rapchieuphim.security;

import com.rapchieuphim.entity.NguoiDung;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Boc NguoiDung thanh UserDetails cho Spring Security, dong thoi mang theo id/ho ten
 * de Controller lay ra "nguoi dang dang nhap" ma khong can truyen id qua tham so nua.
 */
public class TaiKhoanChiTiet implements UserDetails {

    private final NguoiDung nguoiDung;
    private final String vaiTro; // "ROLE_QUANLY" | "ROLE_BANHANG" | "ROLE_KHACHHANG"

    public TaiKhoanChiTiet(NguoiDung nguoiDung, String vaiTro) {
        this.nguoiDung = nguoiDung;
        this.vaiTro = vaiTro;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public Long getId() {
        return nguoiDung.getId();
    }

    public String getHoTen() {
        return nguoiDung.getHoTen();
    }

    public String getVaiTro() {
        return vaiTro;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(vaiTro));
    }

    @Override
    public String getPassword() {
        return nguoiDung.getMatKhau();
    }

    @Override
    public String getUsername() {
        return nguoiDung.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
