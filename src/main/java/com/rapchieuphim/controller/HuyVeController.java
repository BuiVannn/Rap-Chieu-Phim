package com.rapchieuphim.controller;

import com.rapchieuphim.dto.KetQuaKiemTraHuyVeDTO;
import com.rapchieuphim.entity.HoaDon;
import com.rapchieuphim.service.KhachHangService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UC: Huy ve. Chi goi KhachHangService.
 * Demo khong dang nhap: khach nhap ma ve (veId) can huy thay vi liet ke theo tai khoan.
 */
@Controller
public class HuyVeController {

    private final KhachHangService khachHangService;

    public HuyVeController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("/ve-cua-toi")
    public String chonVeCanHuy(@RequestParam(required = false) Long veId, Model model) {
        if (veId == null) {
            return "danh-sach-ve-cua-toi";
        }
        KetQuaKiemTraHuyVeDTO ketQua = khachHangService.kiemTraDieuKienHuyVe(veId);
        model.addAttribute("ketQua", ketQua);
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
