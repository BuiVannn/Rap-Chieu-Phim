package com.rapchieuphim.controller;

import com.rapchieuphim.dto.KetQuaKiemTraHuyVeDTO;
import com.rapchieuphim.entity.HoaDon;
import com.rapchieuphim.entity.Ve;
import com.rapchieuphim.exception.KhongTimThayException;
import com.rapchieuphim.security.TaiKhoanChiTiet;
import com.rapchieuphim.service.KhachHangService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * UC: Huy ve + man "Ve cua toi" (xem ve da mua, xem chi tiet ve dien tu, huy ve).
 * Nho co dang nhap, chi hien/thao tac tren ve cua chinh khach hang dang dang nhap.
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
    public String xemChiTietVe(@PathVariable Long veId,
                               @AuthenticationPrincipal TaiKhoanChiTiet taiKhoan,
                               Model model) {
        KetQuaKiemTraHuyVeDTO ketQua = khachHangService.kiemTraDieuKienHuyVe(veId);
        kiemTraQuyenSoHuu(ketQua.getVe(), taiKhoan);
        model.addAttribute("ketQua", ketQua);
        return "chi-tiet-ve";
    }

    @PostMapping("/ve-cua-toi/{veId}/huy")
    public String xacNhanHuyVe(@PathVariable Long veId,
                               @AuthenticationPrincipal TaiKhoanChiTiet taiKhoan,
                               Model model) {
        KetQuaKiemTraHuyVeDTO ketQua = khachHangService.kiemTraDieuKienHuyVe(veId);
        kiemTraQuyenSoHuu(ketQua.getVe(), taiKhoan);
        if (!ketQua.isDuocHuy()) {
            model.addAttribute("ketQua", ketQua);
            return "chi-tiet-ve";
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

    /** Chan xem/huy ve khong thuoc ve khach dang dang nhap. */
    private void kiemTraQuyenSoHuu(Ve ve, TaiKhoanChiTiet taiKhoan) {
        Long chuVe = ve.getHoaDon() != null && ve.getHoaDon().getKhachHang() != null
                ? ve.getHoaDon().getKhachHang().getId() : null;
        if (chuVe == null || !chuVe.equals(taiKhoan.getId())) {
            throw new KhongTimThayException("Không tìm thấy vé này trong tài khoản của bạn.");
        }
    }
}
