package com.rapchieuphim.controller;

import com.rapchieuphim.entity.GiaVeTheoLoaiGhe;
import com.rapchieuphim.entity.SuatChieu;
import com.rapchieuphim.service.NhanVienQuanLyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UC: Tao suat chieu (lap lich chieu). Chi goi NhanVienQuanLyService.
 */
@Controller
public class LapLichChieuController {

    private final NhanVienQuanLyService nhanVienQuanLyService;

    public LapLichChieuController(NhanVienQuanLyService nhanVienQuanLyService) {
        this.nhanVienQuanLyService = nhanVienQuanLyService;
    }

    @GetMapping("/quan-ly/lap-lich-chieu")
    public String layDanhSachPhim(Model model) {
        model.addAttribute("danhSachPhim", nhanVienQuanLyService.layDanhSachPhimDangChieu());
        return "chon-phim-ngay-phong";
    }

    @GetMapping("/quan-ly/lap-lich-chieu/khung-gio")
    public String xemKhungGio(@RequestParam Long phimId,
                              @RequestParam Long phongChieuId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayChieu,
                              Model model) {
        model.addAttribute("phimId", phimId);
        model.addAttribute("phongChieuId", phongChieuId);
        model.addAttribute("ngayChieu", ngayChieu);
        model.addAttribute("khungGioTrong", nhanVienQuanLyService.layKhungGioTrong(phongChieuId, ngayChieu));
        return "chon-khung-gio";
    }

    @PostMapping("/quan-ly/lap-lich-chieu/tao")
    public String xacNhanTaoSuatChieu(@RequestParam Long phimId,
                                      @RequestParam Long phongChieuId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayChieu,
                                      @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime gioBatDau,
                                      @RequestParam Double giaVe,
                                      Model model) {
        SuatChieu suatChieu = nhanVienQuanLyService.taoSuatChieu(
                phimId, phongChieuId, ngayChieu, gioBatDau, giaVe);
        model.addAttribute("suatChieu", suatChieu);
        return "thiet-lap-gia-rieng";
    }

    @PostMapping("/quan-ly/lap-lich-chieu/gia-rieng")
    public String luuGiaRieng(@RequestParam Long suatChieuId,
                              @RequestParam(required = false) List<String> loaiGhe,
                              @RequestParam(required = false) List<Double> giaVe,
                              Model model) {
        List<GiaVeTheoLoaiGhe> danhSachGia = new ArrayList<>();
        if (loaiGhe != null && giaVe != null) {
            int n = Math.min(loaiGhe.size(), giaVe.size());
            for (int i = 0; i < n; i++) {
                if (loaiGhe.get(i) == null || loaiGhe.get(i).isBlank()) {
                    continue;
                }
                GiaVeTheoLoaiGhe g = new GiaVeTheoLoaiGhe();
                g.setLoaiGhe(loaiGhe.get(i));
                g.setGiaVe(giaVe.get(i));
                danhSachGia.add(g);
            }
        }
        if (!danhSachGia.isEmpty()) {
            nhanVienQuanLyService.luuGiaVeRieng(suatChieuId, danhSachGia);
        }
        model.addAttribute("thongBao", "Da tao suat chieu thanh cong"
                + (danhSachGia.isEmpty() ? "." : " kem " + danhSachGia.size() + " gia ve rieng."));
        model.addAttribute("danhSachPhim", nhanVienQuanLyService.layDanhSachPhimDangChieu());
        return "chon-phim-ngay-phong";
    }
}
