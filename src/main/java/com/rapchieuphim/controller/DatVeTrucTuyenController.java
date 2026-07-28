package com.rapchieuphim.controller;

import com.rapchieuphim.dto.ThongTinThanhToanDTO;
import com.rapchieuphim.dto.TrangThaiGheDTO;
import com.rapchieuphim.entity.HoaDon;
import com.rapchieuphim.service.KhachHangService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * UC: Dat ve truc tuyen. Chi goi KhachHangService.
 */
@Controller
public class DatVeTrucTuyenController {

    private final KhachHangService khachHangService;

    public DatVeTrucTuyenController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("/dat-ve/suat-chieu/{suatChieuId}/ghe")
    public String xemSoDoGhe(@PathVariable Long suatChieuId, Model model) {
        model.addAttribute("suatChieuId", suatChieuId);
        model.addAttribute("danhSachGhe", khachHangService.layTrangThaiGhe(suatChieuId));
        model.addAttribute("kenh", "online");
        model.addAttribute("actionXacNhan", "/dat-ve/xac-nhan-ghe");
        return "chon-ghe";
    }

    @PostMapping("/dat-ve/xac-nhan-ghe")
    public String xacNhanChonGhe(@RequestParam Long suatChieuId,
                                 @RequestParam List<Long> gheIds,
                                 Model model) {
        double tongTien = khachHangService.giuGheTamThoi(suatChieuId, gheIds);

        // Sau khi giu ghe, doc lai so do de lay veId cua cac ghe vua giu -> chuyen sang thanh toan
        List<Long> veIds = new ArrayList<>();
        for (TrangThaiGheDTO ghe : khachHangService.layTrangThaiGhe(suatChieuId)) {
            if (gheIds.contains(ghe.getGheId()) && "Đang giữ".equals(ghe.getTrangThai())) {
                veIds.add(ghe.getVeId());
            }
        }

        model.addAttribute("suatChieuId", suatChieuId);
        model.addAttribute("veIds", veIds);
        model.addAttribute("tongTien", tongTien);
        return "thanh-toan";
    }

    @PostMapping("/dat-ve/ap-dung-uu-dai")
    public String apDungUuDai(@RequestParam String maThe,
                              @RequestParam double tongTien,
                              @RequestParam List<Long> veIds,
                              Model model) {
        double tongTienSauUuDai = khachHangService.apDungUuDaiThanhVien(maThe, tongTien);
        model.addAttribute("veIds", veIds);
        model.addAttribute("tongTien", tongTienSauUuDai);
        model.addAttribute("maThe", maThe);
        model.addAttribute("daApDungUuDai", true);
        return "thanh-toan";
    }

    @PostMapping("/dat-ve/thanh-toan")
    public String xacNhanThanhToan(@RequestParam List<Long> veIds,
                                   @RequestParam double tongTien,
                                   @RequestParam(required = false) Long khachHangId,
                                   @RequestParam(required = false) String phuongThuc,
                                   Model model) {
        ThongTinThanhToanDTO thongTin = new ThongTinThanhToanDTO();
        thongTin.setPhuongThuc(phuongThuc != null ? phuongThuc : "The tin dung");
        HoaDon hoaDon = khachHangService.xuLyThanhToan(veIds, tongTien, khachHangId, thongTin);
        model.addAttribute("hoaDon", hoaDon);
        return "ket-qua-dat-ve";
    }
}
