package com.rapchieuphim.controller;

import com.rapchieuphim.service.KhachHangService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UC: Tim kiem va xem thong tin phim. Chi goi KhachHangService.
 */
@Controller
public class TimKiemPhimController {

    private final KhachHangService khachHangService;

    public TimKiemPhimController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("/phim/tim-kiem")
    public String timKiem(@RequestParam(name = "tuKhoa", required = false, defaultValue = "") String tuKhoa,
                          Model model) {
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("danhSachPhim", khachHangService.timPhimTheoTuKhoa(tuKhoa));
        return "danh-sach-phim";
    }

    @GetMapping("/phim/{id}")
    public String xemChiTietPhim(@PathVariable("id") Long id, Model model) {
        model.addAttribute("chiTiet", khachHangService.layChiTietPhim(id));
        return "chi-tiet-phim";
    }
}
