package com.rapchieuphim.controller;

import com.rapchieuphim.dto.TrangThaiGheDTO;
import com.rapchieuphim.entity.HoaDon;
import com.rapchieuphim.entity.KhachHang;
import com.rapchieuphim.entity.NhanVienBanHang;
import com.rapchieuphim.security.TaiKhoanChiTiet;
import com.rapchieuphim.service.NhanVienBanHangService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * UC: Ban ve tai quay. Chi goi NhanVienBanHangService.
 * Ghi chu: khong co dang nhap (demo) nen "nhan vien dang thao tac" de null tren hoa don.
 */
@Controller
public class BanVeTaiQuayController {

    private final NhanVienBanHangService nhanVienBanHangService;

    public BanVeTaiQuayController(NhanVienBanHangService nhanVienBanHangService) {
        this.nhanVienBanHangService = nhanVienBanHangService;
    }

    @GetMapping("/quay/ban-ve/tim-suat-chieu")
    public String timSuatChieu(@RequestParam(required = false, defaultValue = "") String tuKhoa,
                               Model model) {
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("danhSachSuatChieu", nhanVienBanHangService.timSuatChieu(tuKhoa));
        return "tim-suat-chieu-quay";
    }

    // BO SUNG: hien so do ghe cho quay (View chon-ghe.html dung chung 2 luong)
    @GetMapping("/quay/ban-ve/suat-chieu/{suatChieuId}/ghe")
    public String xemSoDoGhe(@PathVariable Long suatChieuId, Model model) {
        model.addAttribute("suatChieuId", suatChieuId);
        model.addAttribute("danhSachGhe", nhanVienBanHangService.layTrangThaiGhe(suatChieuId));
        model.addAttribute("kenh", "quay");
        model.addAttribute("actionXacNhan", "/quay/ban-ve/xac-nhan-ghe");
        return "chon-ghe";
    }

    @PostMapping("/quay/ban-ve/xac-nhan-ghe")
    public String xacNhanChonGhe(@RequestParam Long suatChieuId,
                                 @RequestParam List<Long> gheIds,
                                 Model model) {
        double tongTien = nhanVienBanHangService.giuGheTamThoi(suatChieuId, gheIds);

        List<Long> veIds = new ArrayList<>();
        for (TrangThaiGheDTO ghe : nhanVienBanHangService.layTrangThaiGhe(suatChieuId)) {
            if (gheIds.contains(ghe.getGheId()) && "Đang giữ".equals(ghe.getTrangThai())) {
                veIds.add(ghe.getVeId());
            }
        }

        model.addAttribute("suatChieuId", suatChieuId);
        model.addAttribute("veIds", veIds);
        model.addAttribute("tongTien", tongTien);
        return "thanh-toan-quay";
    }

    @GetMapping("/quay/ban-ve/tra-cuu-thanh-vien")
    public String traCuuThanhVien(@RequestParam String soDienThoai,
                                  @RequestParam List<Long> veIds,
                                  @RequestParam double tongTien,
                                  Model model) {
        KhachHang khachHang = nhanVienBanHangService.traCuuTheThanhVien(soDienThoai);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("veIds", veIds);
        model.addAttribute("tongTien", tongTien);
        return "tra-cuu-thanh-vien";
    }

    @PostMapping("/quay/ban-ve/ap-dung-uu-dai")
    public String apDungUuDai(@RequestParam String soDienThoai,
                              @RequestParam List<Long> veIds,
                              @RequestParam double tongTien,
                              Model model) {
        KhachHang khachHang = nhanVienBanHangService.traCuuTheThanhVien(soDienThoai);
        double tongTienSauUuDai = nhanVienBanHangService.apDungUuDaiThanhVien(khachHang, tongTien);
        model.addAttribute("veIds", veIds);
        model.addAttribute("tongTien", tongTienSauUuDai);
        model.addAttribute("soDienThoai", soDienThoai);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("daApDungUuDai", true);
        return "thanh-toan-quay";
    }

    @PostMapping("/quay/ban-ve/thanh-toan")
    public String xacNhanThanhToanQuay(@RequestParam List<Long> veIds,
                                       @RequestParam double tongTien,
                                       @RequestParam(required = false) String soDienThoai,
                                       @RequestParam(required = false, defaultValue = "Tiền mặt") String hinhThucThanhToan,
                                       @AuthenticationPrincipal TaiKhoanChiTiet taiKhoan,
                                       Model model) {
        KhachHang khachHang = null;
        if (soDienThoai != null && !soDienThoai.isBlank()) {
            khachHang = nhanVienBanHangService.traCuuTheThanhVien(soDienThoai);
        }
        // Gan nhan vien ban hang dang dang nhap (neu la NVBH); quan ly ban ho thi de null
        NhanVienBanHang nhanVien = taiKhoan.getNguoiDung() instanceof NhanVienBanHang nvbh ? nvbh : null;
        HoaDon hoaDon = nhanVienBanHangService.xuLyThanhToanTaiQuay(
                veIds, tongTien, khachHang, nhanVien, hinhThucThanhToan);
        return "redirect:/quay/ban-ve/in-hoa-don/" + hoaDon.getId();
    }

    @GetMapping("/quay/ban-ve/in-hoa-don/{hoaDonId}")
    public String inHoaDon(@PathVariable Long hoaDonId, Model model) {
        model.addAttribute("hoaDon", nhanVienBanHangService.inHoaDon(hoaDonId));
        return "in-hoa-don";
    }
}
