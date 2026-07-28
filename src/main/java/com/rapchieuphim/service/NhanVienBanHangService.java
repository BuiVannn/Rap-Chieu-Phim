package com.rapchieuphim.service;

import com.rapchieuphim.dto.TrangThaiGheDTO;
import com.rapchieuphim.entity.*;
import com.rapchieuphim.exception.GheKhongCoSanException;
import com.rapchieuphim.exception.KhongTimThayException;
import com.rapchieuphim.exception.SuatChieuDaQuaException;
import com.rapchieuphim.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service cho actor "Nhan vien ban hang": ban ve tai quay.
 * Chi goi Repository (khong goi Service khac). Logic giu ghe dung chung y het KhachHangService
 * duoc lap lai o day de tuan thu quy tac chi goi tang lien ke.
 */
@Service
@Transactional
public class NhanVienBanHangService {

    private static final List<String> TRANG_THAI_GHE_BAN = List.of("Đang giữ", "Đã bán");

    private final PhimRepository phimRepository;
    private final SuatChieuRepository suatChieuRepository;
    private final GheRepository gheRepository;
    private final VeRepository veRepository;
    private final KhachHangRepository khachHangRepository;
    private final HoaDonRepository hoaDonRepository;

    public NhanVienBanHangService(PhimRepository phimRepository,
                                  SuatChieuRepository suatChieuRepository,
                                  GheRepository gheRepository,
                                  VeRepository veRepository,
                                  KhachHangRepository khachHangRepository,
                                  HoaDonRepository hoaDonRepository) {
        this.phimRepository = phimRepository;
        this.suatChieuRepository = suatChieuRepository;
        this.gheRepository = gheRepository;
        this.veRepository = veRepository;
        this.khachHangRepository = khachHangRepository;
        this.hoaDonRepository = hoaDonRepository;
    }

    // ===== UC: Ban ve tai quay =====

    public List<SuatChieu> timSuatChieu(String tuKhoa) {
        List<Phim> phims = phimRepository.findByTenPhimContainingIgnoreCase(tuKhoa == null ? "" : tuKhoa);
        List<SuatChieu> ketQua = new ArrayList<>();
        for (Phim phim : phims) {
            ketQua.addAll(suatChieuRepository.findByPhimId(phim.getId()));
        }
        return ketQua;
    }

    /**
     * BO SUNG (ngoai danh sach 4.4b): so do ghe cho luong ban ve tai quay.
     * Doi xung voi KhachHangService.layTrangThaiGhe() vi View chon-ghe.html dung chung cho ca 2 luong.
     */
    public List<TrangThaiGheDTO> layTrangThaiGhe(Long suatChieuId) {
        SuatChieu suatChieu = suatChieuRepository.findById(suatChieuId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay suat chieu id=" + suatChieuId));
        List<Ghe> danhSachGhe = gheRepository.findByPhongChieuId(suatChieu.getPhongChieu().getId());
        List<Ve> danhSachVe = veRepository.findBySuatChieuId(suatChieuId);

        Map<Long, Ve> veTheoGhe = new HashMap<>();
        for (Ve ve : danhSachVe) {
            if (TRANG_THAI_GHE_BAN.contains(ve.getTrangThai())) {
                veTheoGhe.put(ve.getGhe().getId(), ve);
            }
        }

        List<TrangThaiGheDTO> ketQua = new ArrayList<>();
        for (Ghe ghe : danhSachGhe) {
            Ve ve = veTheoGhe.get(ghe.getId());
            String trangThai = ve != null ? ve.getTrangThai() : "Trống";
            Long veId = ve != null ? ve.getId() : null;
            double gia = tinhGiaVe(suatChieu, ghe);
            ketQua.add(new TrangThaiGheDTO(ghe.getId(), ghe.getSoGhe(), ghe.getHangGhe(),
                    ghe.getLoaiGhe(), trangThai, gia, veId));
        }
        return ketQua;
    }

    public double giuGheTamThoi(Long suatChieuId, List<Long> gheIds) {
        SuatChieu suatChieu = suatChieuRepository.findById(suatChieuId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay suat chieu id=" + suatChieuId));
        kiemTraSuatChuaChieu(suatChieu);

        List<Ve> veHienCo = veRepository.findBySuatChieuId(suatChieuId);
        for (Ve ve : veHienCo) {
            if (TRANG_THAI_GHE_BAN.contains(ve.getTrangThai()) && gheIds.contains(ve.getGhe().getId())) {
                throw new GheKhongCoSanException("Ghe " + ve.getGhe().getSoGhe()
                        + " da duoc giu hoac ban, vui long chon ghe khac.");
            }
        }

        List<Ghe> danhSachGhe = gheRepository.findAllById(gheIds);
        List<Ve> veMoi = new ArrayList<>();
        double tongTien = 0;
        for (Ghe ghe : danhSachGhe) {
            double gia = tinhGiaVe(suatChieu, ghe);
            Ve ve = new Ve();
            ve.setTrangThai("Đang giữ");
            ve.setGiaVeApDung(gia);
            ve.setSuatChieu(suatChieu);
            ve.setGhe(ghe);
            ve.setHoaDon(null);
            veMoi.add(ve);
            tongTien += gia;
        }
        veRepository.saveAll(veMoi);
        return tongTien;
    }

    public KhachHang traCuuTheThanhVien(String soDienThoai) {
        return khachHangRepository.findBySoDienThoai(soDienThoai)
                .orElseThrow(() -> new KhongTimThayException(
                        "Khong tim thay thanh vien voi so dien thoai: " + soDienThoai));
    }

    public double apDungUuDaiThanhVien(KhachHang khachHang, double tongTien) {
        double phanTram = khachHang.getHangThanhVien().getPhanTramUuDai();
        return tongTien * (100 - phanTram) / 100.0;
    }

    public HoaDon xuLyThanhToanTaiQuay(List<Long> veIds, double tongTien, KhachHang khachHang,
                                       NhanVienBanHang nhanVien, String hinhThucThanhToan) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon.setTongTien(tongTien);
        hoaDon.setHinhThucThanhToan(hinhThucThanhToan);
        hoaDon.setHinhThucBan("Tại quầy");
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVienBanHang(nhanVien);
        hoaDon = hoaDonRepository.save(hoaDon);

        List<Ve> ves = veRepository.findAllById(veIds);
        for (Ve ve : ves) {
            if (!"Đang giữ".equals(ve.getTrangThai())) {
                throw new GheKhongCoSanException("Ve ghe " + ve.getGhe().getSoGhe()
                        + " khong con o trang thai giu, khong the thanh toan.");
            }
            ve.setTrangThai("Đã bán");
            ve.setMaQR(sinhMaQR());
            ve.setHoaDon(hoaDon);
        }
        veRepository.saveAll(ves);
        hoaDon.setDanhSachVe(ves);
        return hoaDon;
    }

    /**
     * Lay du lieu hoa don de in. Thiet ke goc ghi 'void inHoaDon', nhung Controller can doi tuong
     * HoaDon de render View in-hoa-don.html, nen tra ve HoaDon (mo rong toi thieu, giu nguyen ten).
     */
    public HoaDon inHoaDon(Long hoaDonId) {
        return hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay hoa don id=" + hoaDonId));
    }

    // ===== Ho tro =====

    private void kiemTraSuatChuaChieu(SuatChieu suatChieu) {
        LocalDateTime gioChieu = LocalDateTime.of(suatChieu.getNgayChieu(), suatChieu.getGioBatDau());
        if (gioChieu.isBefore(LocalDateTime.now())) {
            throw new SuatChieuDaQuaException("Suat chieu da bat dau, khong the ban ve.");
        }
    }

    private double tinhGiaVe(SuatChieu suatChieu, Ghe ghe) {
        if (suatChieu.getDanhSachGiaTheoLoaiGhe() != null) {
            for (GiaVeTheoLoaiGhe gia : suatChieu.getDanhSachGiaTheoLoaiGhe()) {
                if (gia.getLoaiGhe() != null && gia.getLoaiGhe().equals(ghe.getLoaiGhe())) {
                    return gia.getGiaVe();
                }
            }
        }
        return suatChieu.getGiaVeMacDinh();
    }

    private String sinhMaQR() {
        return "QR-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
