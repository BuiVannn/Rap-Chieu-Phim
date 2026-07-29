package com.rapchieuphim.controller;

import com.rapchieuphim.security.TaiKhoanChiTiet;
import com.rapchieuphim.service.NhanVienQuanLyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UC: Chot ky doanh thu. Chi goi NhanVienQuanLyService.
 * Demo khong dang nhap: id nhan vien quan ly duoc truyen qua form (mac dinh la quan ly da seed).
 */
@Controller
public class ChotDoanhThuController {

    private final NhanVienQuanLyService nhanVienQuanLyService;

    public ChotDoanhThuController(NhanVienQuanLyService nhanVienQuanLyService) {
        this.nhanVienQuanLyService = nhanVienQuanLyService;
    }

    @GetMapping("/quan-ly/chot-doanh-thu")
    public String xemDanhSachKyChuaChot(Model model) {
        model.addAttribute("danhSachKy", nhanVienQuanLyService.layDanhSachKyChuaChot());
        return "danh-sach-ky-chua-chot";
    }

    @GetMapping("/quan-ly/chot-doanh-thu/{kyId}")
    public String xemChiTietKy(@PathVariable Long kyId, Model model) {
        model.addAttribute("baoCao", nhanVienQuanLyService.tongHopDoanhThuTheoKy(kyId));
        return "chi-tiet-ky-doanh-thu";
    }

    @PostMapping("/quan-ly/chot-doanh-thu/{kyId}/xac-nhan")
    public String xacNhanChotKy(@PathVariable Long kyId,
                                @RequestParam double soTienThucThu,
                                @AuthenticationPrincipal TaiKhoanChiTiet taiKhoan,
                                Model model) {
        // Nguoi chot la quan ly dang dang nhap (khong con nhap id thu cong)
        nhanVienQuanLyService.doiChieuVaChotKy(kyId, soTienThucThu, taiKhoan.getId());
        model.addAttribute("kyId", kyId);
        model.addAttribute("soTienThucThu", soTienThucThu);
        return "ket-qua-chot-ky";
    }
}
