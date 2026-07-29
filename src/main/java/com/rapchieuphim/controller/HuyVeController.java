package com.rapchieuphim.controller;

import com.rapchieuphim.dto.KetQuaKiemTraHuyVeDTO;
import com.rapchieuphim.entity.HoaDon;
import com.rapchieuphim.security.TaiKhoanChiTiet;
import com.rapchieuphim.service.KhachHangService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * UC: Huy ve + man "Ve cua toi". Chi goi KhachHangService.
 * Nho co dang nhap, liet ke duoc ve theo khach hang hien tai (thay vi bat nhap ma ve nhu truoc).
 */
@Controller
public class HuyVeController {

    private final KhachHangService khachHangService;

    public HuyVeController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("/ve-cua-toi")
    public String chonVeCanHuy(@AuthenticationPrincipal TaiKhoanChiTiet taiKhoan, Model model) {
        model.addAttribute("danhSachVe", khachHangService.layVeCuaKhachHang(taiKhoan.getId()));
        return "danh-sach-ve-cua-toi";
    }

    @GetMapping("/ve-cua-toi/{veId}")
    public String xemXacNhanHuy(@PathVariable Long veId, Model model) {
        model.addAttribute("ketQua", khachHangService.kiemTraDieuKienHuyVe(veId));
        return "xac-nhan-huy-ve";
    }

    @PostMapping("/ve-cua-toi/{veId}/huy")
    public String xacNhanHuyVe(@PathVariable Long veId, Model model) {
        KetQuaKiemTraHuyVeDTO ketQua = khachHangService.kiemTraDieuKienHuyVe(veId);
        if (!ketQua.isDuocHuy()) {
            model.addAttribute("ketQua", ketQua);
            return "xac-nhan-huy-ve";
        }
        HoaDon hoaDon = ketQua.getVe().getHoaDon();
        khachHangService.xuLyHuyVe(veId);
        if (hoaDon != null) {
            khachHangService.hoanTien(hoaDon.getId(), ketQua.getSoTienHoanDuKien());
        }
        model.addAttribute("soTienHoan", ketQua.getSoTienHoanDuKien());
        model.addAttribute("veId", veId);
        return "thong-bao-huy-ve";
    }
}
