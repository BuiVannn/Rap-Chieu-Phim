package com.rapchieuphim.controller;

import com.rapchieuphim.entity.KhachHang;
import com.rapchieuphim.security.TaiKhoanChiTiet;
import com.rapchieuphim.service.KhachHangService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UC: Dang ky the thanh vien. Chi goi KhachHangService.
 */
@Controller
public class DangKyThanhVienController {

    private final KhachHangService khachHangService;

    public DangKyThanhVienController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("/thanh-vien/dang-ky")
    public String hienThiFormDangKy() {
        return "dang-ky-the-thanh-vien";
    }

    @PostMapping("/thanh-vien/dang-ky")
    public String xacNhanDangKy(@RequestParam String hoTen,
                                @RequestParam String soDienThoai,
                                @RequestParam String email,
                                @RequestParam String matKhau,
                                Model model) {
        KhachHang khachHang = khachHangService.dangKyTheThanhVien(hoTen, soDienThoai, email, matKhau);
        model.addAttribute("khachHang", khachHang);
        return "ket-qua-dang-ky";
    }

    // UC: Xem thong tin the thanh vien cua khach dang dang nhap (US-303)
    @GetMapping("/thanh-vien/the-cua-toi")
    public String xemTheCuaToi(@AuthenticationPrincipal TaiKhoanChiTiet taiKhoan, Model model) {
        model.addAttribute("khachHang", khachHangService.layThongTinTheThanhVien(taiKhoan.getId()));
        return "the-cua-toi";
    }
}
