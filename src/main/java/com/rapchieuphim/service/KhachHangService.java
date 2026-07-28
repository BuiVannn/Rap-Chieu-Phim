package com.rapchieuphim.service;

import com.rapchieuphim.client.CongThanhToanClient;
import com.rapchieuphim.client.EmailSmsClient;
import com.rapchieuphim.dto.ChiTietPhimDTO;
import com.rapchieuphim.dto.KetQuaKiemTraHuyVeDTO;
import com.rapchieuphim.dto.KetQuaThanhToanDTO;
import com.rapchieuphim.dto.ThongTinThanhToanDTO;
import com.rapchieuphim.dto.TrangThaiGheDTO;
import com.rapchieuphim.entity.*;
import com.rapchieuphim.exception.GheKhongCoSanException;
import com.rapchieuphim.exception.KhongTimThayException;
import com.rapchieuphim.exception.SuatChieuDaQuaException;
import com.rapchieuphim.exception.ThanhToanThatBaiException;
import com.rapchieuphim.exception.ThongTinTrungException;
import com.rapchieuphim.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service cho actor "Khach hang": tim phim, dat ve truc tuyen, huy ve, dang ky the thanh vien.
 * Chi goi Repository/Client.
 */
@Service
@Transactional
public class KhachHangService {

    private static final List<String> TRANG_THAI_GHE_BAN = List.of("Đang giữ", "Đã bán");

    // So gio toi thieu truoc gio chieu de duoc huy ve (cau hinh trong application.properties)
    @Value("${app.huy-ve.gio-toi-thieu:2}")
    private int gioToiThieuHuyVe;

    private final PhimRepository phimRepository;
    private final SuatChieuRepository suatChieuRepository;
    private final GheRepository gheRepository;
    private final VeRepository veRepository;
    private final KhachHangRepository khachHangRepository;
    private final HangThanhVienRepository hangThanhVienRepository;
    private final HoaDonRepository hoaDonRepository;
    private final CongThanhToanClient congThanhToanClient;
    private final EmailSmsClient emailSmsClient;
    private final PasswordEncoder passwordEncoder;

    public KhachHangService(PhimRepository phimRepository,
                            SuatChieuRepository suatChieuRepository,
                            GheRepository gheRepository,
                            VeRepository veRepository,
                            KhachHangRepository khachHangRepository,
                            HangThanhVienRepository hangThanhVienRepository,
                            HoaDonRepository hoaDonRepository,
                            CongThanhToanClient congThanhToanClient,
                            EmailSmsClient emailSmsClient,
                            PasswordEncoder passwordEncoder) {
        this.phimRepository = phimRepository;
        this.suatChieuRepository = suatChieuRepository;
        this.gheRepository = gheRepository;
        this.veRepository = veRepository;
        this.khachHangRepository = khachHangRepository;
        this.hangThanhVienRepository = hangThanhVienRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.congThanhToanClient = congThanhToanClient;
        this.emailSmsClient = emailSmsClient;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== UC: Tim kiem va xem thong tin phim =====

    public List<Phim> timPhimTheoTuKhoa(String tuKhoa) {
        return phimRepository.findByTenPhimContainingIgnoreCase(tuKhoa == null ? "" : tuKhoa);
    }

    public ChiTietPhimDTO layChiTietPhim(Long phimId) {
        Phim phim = phimRepository.findById(phimId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay phim id=" + phimId));
        List<SuatChieu> suatChieus = suatChieuRepository.findByPhimId(phimId);
        return new ChiTietPhimDTO(phim, suatChieus);
    }

    // ===== UC: Dat ve truc tuyen =====

    public List<TrangThaiGheDTO> layTrangThaiGhe(Long suatChieuId) {
        SuatChieu suatChieu = suatChieuRepository.findById(suatChieuId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay suat chieu id=" + suatChieuId));
        List<Ghe> danhSachGhe = gheRepository.findByPhongChieuId(suatChieu.getPhongChieu().getId());
        List<Ve> danhSachVe = veRepository.findBySuatChieuId(suatChieuId);

        // gheId -> ve dang "chiem" ghe (Dang giu / Da ban)
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

    public double apDungUuDaiThanhVien(String maThe, double tongTien) {
        KhachHang khachHang = khachHangRepository.findByTheThanhVien_MaThe(maThe)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay the thanh vien: " + maThe));
        double phanTram = khachHang.getHangThanhVien().getPhanTramUuDai();
        return tongTien * (100 - phanTram) / 100.0;
    }

    public HoaDon xuLyThanhToan(List<Long> veIds, double tongTien, Long khachHangId,
                                ThongTinThanhToanDTO thongTinThanhToan) {
        KetQuaThanhToanDTO ketQua = congThanhToanClient.guiYeuCauThanhToan(tongTien, thongTinThanhToan);
        if (ketQua == null || !ketQua.isThanhCong()) {
            throw new ThanhToanThatBaiException("Cong thanh toan tu choi giao dich.");
        }

        KhachHang khachHang = null;
        if (khachHangId != null) {
            khachHang = khachHangRepository.findById(khachHangId)
                    .orElseThrow(() -> new KhongTimThayException("Khong tim thay khach hang id=" + khachHangId));
        }

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon.setTongTien(tongTien);
        hoaDon.setHinhThucThanhToan(thongTinThanhToan != null ? thongTinThanhToan.getPhuongThuc() : null);
        hoaDon.setHinhThucBan("Trực tuyến");
        hoaDon.setKhachHang(khachHang);
        hoaDon = hoaDonRepository.save(hoaDon);

        List<Ve> ves = veRepository.findAllById(veIds);
        for (Ve ve : ves) {
            // Chi ban duoc ve dang o trang thai "Dang giu" (tranh ban lai ve da huy/da ban)
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

        // Self-call dung Sequence Diagram: sau khi thanh toan thi xuat ve dien tu
        xuatVeDienTu(hoaDon);
        return hoaDon;
    }

    public void xuatVeDienTu(HoaDon hoaDon) {
        String email = hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getEmail() : null;
        emailSmsClient.guiVeDienTu(email, hoaDon);
    }

    // ===== UC: Huy ve =====

    public KetQuaKiemTraHuyVeDTO kiemTraDieuKienHuyVe(Long veId) {
        Ve ve = veRepository.findById(veId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay ve id=" + veId));
        SuatChieu suatChieu = ve.getSuatChieu();
        LocalDateTime gioChieu = LocalDateTime.of(suatChieu.getNgayChieu(), suatChieu.getGioBatDau());
        LocalDateTime hanChot = gioChieu.minusHours(gioToiThieuHuyVe);

        boolean duocHuy = true;
        String lyDo = "Du dieu kien huy ve.";
        if (!"Đã bán".equals(ve.getTrangThai())) {
            duocHuy = false;
            lyDo = "Ve o trang thai '" + ve.getTrangThai() + "' khong the huy.";
        } else if (LocalDateTime.now().isAfter(hanChot)) {
            duocHuy = false;
            lyDo = "Da qua han huy (phai huy truoc gio chieu it nhat " + gioToiThieuHuyVe + " gio).";
        }
        double soTienHoan = duocHuy ? ve.getGiaVeApDung() : 0;
        return new KetQuaKiemTraHuyVeDTO(ve, duocHuy, lyDo, soTienHoan);
    }

    public void xuLyHuyVe(Long veId) {
        Ve ve = veRepository.findById(veId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay ve id=" + veId));
        ve.setTrangThai("Đã hủy");
        ve.setMaQR(null);
        veRepository.save(ve);
    }

    public void hoanTien(Long hoaDonId, double soTienHoan) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new KhongTimThayException("Khong tim thay hoa don id=" + hoaDonId));
        congThanhToanClient.hoanTien(soTienHoan, "Hoa don #" + hoaDonId);
        String email = hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getEmail() : null;
        emailSmsClient.guiThongBaoHuyVe(email, hoaDon, soTienHoan);
    }

    // ===== UC: Dang ky the thanh vien =====

    public KhachHang dangKyTheThanhVien(String hoTen, String soDienThoai, String email, String matKhau) {
        if (khachHangRepository.findBySoDienThoai(soDienThoai).isPresent()) {
            throw new ThongTinTrungException("So dien thoai da duoc dang ky: " + soDienThoai);
        }
        if (khachHangRepository.findByEmail(email).isPresent()) {
            throw new ThongTinTrungException("Email da duoc dang ky: " + email);
        }
        HangThanhVien hangThuong = hangThanhVienRepository.findByTenHang("Thường")
                .orElseThrow(() -> new KhongTimThayException(
                        "Chua cau hinh hang thanh vien 'Thuong' trong he thong."));

        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(hoTen);
        khachHang.setSoDienThoai(soDienThoai);
        khachHang.setEmail(email);
        khachHang.setMatKhau(passwordEncoder.encode(matKhau));
        khachHang.setHangThanhVien(hangThuong);

        TheThanhVien the = new TheThanhVien();
        the.setMaThe("TV" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        the.setNgayDangKy(LocalDate.now());
        khachHang.setTheThanhVien(the);

        return khachHangRepository.save(khachHang);
    }

    // ===== Ho tro =====

    /**
     * Gia ve cua 1 ghe = gia rieng theo loai ghe cua suat chieu (neu co cau hinh), nguoc lai la gia mac dinh.
     */
    /** Khong cho giu/dat ghe neu suat chieu da bat dau. */
    private void kiemTraSuatChuaChieu(SuatChieu suatChieu) {
        LocalDateTime gioChieu = LocalDateTime.of(suatChieu.getNgayChieu(), suatChieu.getGioBatDau());
        if (gioChieu.isBefore(LocalDateTime.now())) {
            throw new SuatChieuDaQuaException("Suat chieu da bat dau, khong the dat ve.");
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
