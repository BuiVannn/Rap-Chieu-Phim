package com.rapchieuphim.security;

import com.rapchieuphim.entity.KhachHang;
import com.rapchieuphim.entity.NguoiDung;
import com.rapchieuphim.entity.NhanVienBanHang;
import com.rapchieuphim.entity.NhanVienQuanLy;
import com.rapchieuphim.repository.NguoiDungRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Nap tai khoan tu DB theo email va suy ra vai tro tu kieu lop con (KhachHang / NhanVienBanHang / NhanVienQuanLy).
 */
@Service
public class TaiKhoanUserDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;

    public TaiKhoanUserDetailsService(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Khong tim thay tai khoan: " + email));

        String vaiTro;
        if (nguoiDung instanceof NhanVienQuanLy) {
            vaiTro = "ROLE_QUANLY";
        } else if (nguoiDung instanceof NhanVienBanHang) {
            vaiTro = "ROLE_BANHANG";
        } else if (nguoiDung instanceof KhachHang) {
            vaiTro = "ROLE_KHACHHANG";
        } else {
            throw new UsernameNotFoundException("Tai khoan khong co vai tro hop le: " + email);
        }
        return new TaiKhoanChiTiet(nguoiDung, vaiTro);
    }
}
