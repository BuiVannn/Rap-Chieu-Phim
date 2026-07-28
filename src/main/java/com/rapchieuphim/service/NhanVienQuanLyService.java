package com.rapchieuphim.service;

import com.rapchieuphim.dto.BaoCaoDoanhThuDTO;
import com.rapchieuphim.entity.*;
import com.rapchieuphim.exception.KhongTimThayException;
import com.rapchieuphim.exception.KyDaChotException;
import com.rapchieuphim.exception.TrungLichChieuException;
import com.rapchieuphim.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho actor "Nhan vien quan ly": lap lich chieu va chot doanh thu.
 * Chi goi Repository/Client (khong goi Controller hay Service khac).
 */
@Service
@Transactional
public class NhanVienQuanLyService {

    private static final LocalTime GIO_MO_CUA = LocalTime.of(8, 0);
    private static final LocalTime GIO_DONG_CUA = LocalTime.of(23, 0);
    private static final int BUOC_KHUNG_GIO_PHUT = 30;
    // Thoi gian don dep/quang cao giua 2 suat trong cung phong
    private static final int DEM_NGHI_PHUT = 15;

    private final PhimRepository phimRepository;
    private final PhongChieuRepository phongChieuRepository;
    private final SuatChieuRepository suatChieuRepository;
    private final GiaVeTheoLoaiGheRepository giaVeTheoLoaiGheRepository;
    private final KyDoanhThuRepository kyDoanhThuRepository;
    private final HoaDonRepository hoaDonRepository;
    private final NhanVienQuanLyRepository nhanVienQuanLyRepository;

    public NhanVienQuanLyService(PhimRepository phimRepository,
                                 PhongChieuRepository phongChieuRepository,
                                 SuatChieuRepository suatChieuRepository,
                                 GiaVeTheoLoaiGheRepository giaVeTheoLoaiGheRepository,
                                 KyDoanhThuRepository kyDoanhThuRepository,
                                 HoaDonRepository hoaDonRepository,
                                 NhanVienQuanLyRepository nhanVienQuanLyRepository) {
        this.phimRepository = phimRepository;
        this.phongChieuRepository = phongChieuRepository;
        this.suatChieuRepository = suatChieuRepository;
        this.giaVeTheoLoaiGheRepository = giaVeTheoLoaiGheRepository;
        this.kyDoanhThuRepository = kyDoanhThuRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.nhanVienQuanLyRepository = nhanVienQuanLyRepository;
    }

    // ===== UC: Tao suat chieu =====

    public List<Phim> layDanhSachPhimDangChieu() {
        return phimRepository.findByTrangThaiIn(List.of("Sắp chiếu", "Đang chiếu"));
    }

    public List<LocalTime> layKhungGioTrong(Long phongChieuId, LocalDate ngayChieu) {
        List<SuatChieu> daCo = suatChieuRepository.findByPhongChieuIdAndNgayChieu(phongChieuId, ngayChieu);
        List<LocalTime> khungGioTrong = new ArrayList<>();
        for (LocalTime moc = GIO_MO_CUA; !moc.isAfter(GIO_DONG_CUA);
             moc = moc.plusMinutes(BUOC_KHUNG_GIO_PHUT)) {
            LocalTime candidate = moc;
            boolean biTrung = daCo.stream().anyMatch(s ->
                    !candidate.isBefore(s.getGioBatDau()) && candidate.isBefore(s.getGioKetThuc()));
            if (!biTrung) {
                khungGioTrong.add(candidate);
            }
        }
        return khungGioTrong;
    }

    public SuatChieu taoSuatChieu(Long phimId, Long phongChieuId, LocalDate ngayChieu,
                                  LocalTime gioBatDau, Double giaVe) {
        Phim phim = phimRepository.findById(phimId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay phim id=" + phimId));
        PhongChieu phong = phongChieuRepository.findById(phongChieuId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay phong chieu id=" + phongChieuId));

        LocalTime gioKetThuc = gioBatDau.plusMinutes(phim.getThoiLuong() + DEM_NGHI_PHUT);

        List<SuatChieu> daCo = suatChieuRepository.findByPhongChieuIdAndNgayChieu(phongChieuId, ngayChieu);
        boolean trung = daCo.stream().anyMatch(s ->
                gioBatDau.isBefore(s.getGioKetThuc()) && s.getGioBatDau().isBefore(gioKetThuc));
        if (trung) {
            throw new TrungLichChieuException(
                    "Khung gio bi trung voi suat chieu da co trong phong " + phong.getTenPhong()
                            + " ngay " + ngayChieu);
        }

        SuatChieu suatChieu = new SuatChieu();
        suatChieu.setPhim(phim);
        suatChieu.setPhongChieu(phong);
        suatChieu.setNgayChieu(ngayChieu);
        suatChieu.setGioBatDau(gioBatDau);
        suatChieu.setGioKetThuc(gioKetThuc);
        suatChieu.setGiaVeMacDinh(giaVe);
        return suatChieuRepository.save(suatChieu);
    }

    public void luuGiaVeRieng(Long suatChieuId, List<GiaVeTheoLoaiGhe> danhSachGia) {
        SuatChieu suatChieu = suatChieuRepository.findById(suatChieuId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay suat chieu id=" + suatChieuId));
        for (GiaVeTheoLoaiGhe gia : danhSachGia) {
            gia.setSuatChieu(suatChieu);
        }
        giaVeTheoLoaiGheRepository.saveAll(danhSachGia);
    }

    // ===== UC: Chot ky doanh thu =====

    public List<KyDoanhThu> layDanhSachKyChuaChot() {
        return kyDoanhThuRepository.findByTrangThai("Chưa chốt");
    }

    public BaoCaoDoanhThuDTO tongHopDoanhThuTheoKy(Long kyId) {
        KyDoanhThu ky = kyDoanhThuRepository.findById(kyId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay ky doanh thu id=" + kyId));

        LocalDateTime tu = ky.getNgayBatDau().atStartOfDay();
        LocalDateTime den = ky.getNgayKetThuc().atTime(LocalTime.MAX);
        List<HoaDon> hoaDons = hoaDonRepository.findByNgayLapBetween(tu, den);

        int tongSoVe = 0;
        double tongDoanhThu = 0;
        double doanhThuTrucTuyen = 0;
        double doanhThuTaiQuay = 0;
        for (HoaDon hd : hoaDons) {
            tongSoVe += hd.getDanhSachVe() != null ? hd.getDanhSachVe().size() : 0;
            tongDoanhThu += hd.getTongTien();
            if ("Trực tuyến".equals(hd.getHinhThucBan())) {
                doanhThuTrucTuyen += hd.getTongTien();
            } else {
                doanhThuTaiQuay += hd.getTongTien();
            }
        }

        return new BaoCaoDoanhThuDTO(ky.getId(), ky.getNgayBatDau(), ky.getNgayKetThuc(),
                ky.getTrangThai(), tongSoVe, hoaDons.size(),
                tongDoanhThu, doanhThuTrucTuyen, doanhThuTaiQuay);
    }

    public void doiChieuVaChotKy(Long kyId, double soTienThucThu, Long nhanVienQuanLyId) {
        KyDoanhThu ky = kyDoanhThuRepository.findById(kyId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay ky doanh thu id=" + kyId));
        if ("Đã chốt".equals(ky.getTrangThai())) {
            throw new KyDaChotException("Ky doanh thu id=" + kyId + " da duoc chot, khong the chot lai.");
        }
        NhanVienQuanLy nhanVien = nhanVienQuanLyRepository.findById(nhanVienQuanLyId)
                .orElseThrow(() -> new KhongTimThayException(
                        "Khong tim thay nhan vien quan ly id=" + nhanVienQuanLyId));

        // Gan cac hoa don trong khoang ngay cua ky vao ky nay -> sau khi chot khong duoc sua nua
        LocalDateTime tu = ky.getNgayBatDau().atStartOfDay();
        LocalDateTime den = ky.getNgayKetThuc().atTime(LocalTime.MAX);
        List<HoaDon> hoaDons = hoaDonRepository.findByNgayLapBetween(tu, den);
        for (HoaDon hd : hoaDons) {
            hd.setKyDoanhThu(ky);
        }
        hoaDonRepository.saveAll(hoaDons);

        ky.setTrangThai("Đã chốt");
        ky.setThoiDiemChot(LocalDateTime.now());
        ky.setNhanVienQuanLy(nhanVien);
        kyDoanhThuRepository.save(ky);
    }
}
