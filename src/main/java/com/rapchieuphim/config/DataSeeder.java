package com.rapchieuphim.config;

import com.rapchieuphim.entity.*;
import com.rapchieuphim.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Seed du lieu mau khi database con trong, de mo link len la demo duoc ngay.
 * Chi chay khi chua co hang thanh vien nao (tranh seed lai moi lan khoi dong).
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final HangThanhVienRepository hangThanhVienRepository;
    private final PhimRepository phimRepository;
    private final PhongChieuRepository phongChieuRepository;
    private final SuatChieuRepository suatChieuRepository;
    private final KyDoanhThuRepository kyDoanhThuRepository;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienQuanLyRepository nhanVienQuanLyRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(HangThanhVienRepository hangThanhVienRepository,
                      PhimRepository phimRepository,
                      PhongChieuRepository phongChieuRepository,
                      SuatChieuRepository suatChieuRepository,
                      KyDoanhThuRepository kyDoanhThuRepository,
                      KhachHangRepository khachHangRepository,
                      NhanVienQuanLyRepository nhanVienQuanLyRepository,
                      PasswordEncoder passwordEncoder) {
        this.hangThanhVienRepository = hangThanhVienRepository;
        this.phimRepository = phimRepository;
        this.phongChieuRepository = phongChieuRepository;
        this.suatChieuRepository = suatChieuRepository;
        this.kyDoanhThuRepository = kyDoanhThuRepository;
        this.khachHangRepository = khachHangRepository;
        this.nhanVienQuanLyRepository = nhanVienQuanLyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (hangThanhVienRepository.count() > 0) {
            log.info("[Seed] Da co du lieu, bo qua seed.");
            return;
        }
        log.info("[Seed] Database trong -> tao du lieu mau...");

        // 1. Hang thanh vien
        HangThanhVien thuong = luuHang("Thường", 0);
        luuHang("Bạc", 5);
        luuHang("Vàng", 10);
        luuHang("Kim cương", 15);

        // 2. Nhan vien quan ly (id = 1, dung lam mac dinh khi chot ky)
        NhanVienQuanLy quanLy = new NhanVienQuanLy();
        quanLy.setHoTen("Nguyễn Quản Lý");
        quanLy.setSoDienThoai("0912000001");
        quanLy.setEmail("quanly@cgvmini.vn");
        quanLy.setMatKhau(passwordEncoder.encode("123456"));
        quanLy.setChucVu("Quản lý rạp");
        nhanVienQuanLyRepository.save(quanLy);

        // 3. Khach hang thanh vien mau
        KhachHang kh = new KhachHang();
        kh.setHoTen("Trần Khách Hàng");
        kh.setSoDienThoai("0900000001");
        kh.setEmail("khachhang@gmail.com");
        kh.setMatKhau(passwordEncoder.encode("123456"));
        kh.setNgaySinh(LocalDate.of(1998, 5, 20));
        kh.setHangThanhVien(thuong);
        TheThanhVien the = new TheThanhVien();
        the.setMaThe("TVDEMO0001");
        the.setNgayDangKy(LocalDate.now());
        kh.setTheThanhVien(the);
        khachHangRepository.save(kh);

        // 4. Phim
        Phim p1 = luuPhim("Vũ Trụ Song Song", "Khoa học viễn tưởng", 132,
                "Trần Anh", "Minh Hằng, Quốc Trường", "Đang chiếu", LocalDate.now().minusDays(5));
        Phim p2 = luuPhim("Bí Ẩn Nửa Đêm", "Kinh dị", 105,
                "Lê Bình", "Kaity Nguyễn", "Đang chiếu", LocalDate.now().minusDays(2));
        luuPhim("Hành Tinh Đỏ", "Phiêu lưu", 120,
                "Phạm Hùng", "Isaac, Diễm My", "Sắp chiếu", LocalDate.now().plusDays(10));

        // 5. Phong chieu + ghe
        PhongChieu phong1 = luuPhong("Phòng 1", "2D");
        PhongChieu phong2 = luuPhong("Phòng 2", "3D");

        // 6. Suat chieu (hom nay & ngay mai)
        luuSuat(p1, phong1, LocalDate.now(), LocalTime.of(19, 0), 80000);
        luuSuat(p1, phong2, LocalDate.now(), LocalTime.of(20, 30), 90000);
        luuSuat(p2, phong1, LocalDate.now().plusDays(1), LocalTime.of(18, 0), 75000);

        // 7. Ky doanh thu thang hien tai (chua chot)
        LocalDate dauThang = LocalDate.now().withDayOfMonth(1);
        LocalDate cuoiThang = dauThang.plusMonths(1).minusDays(1);
        KyDoanhThu ky = new KyDoanhThu();
        ky.setNgayBatDau(dauThang);
        ky.setNgayKetThuc(cuoiThang);
        ky.setTrangThai("Chưa chốt");
        kyDoanhThuRepository.save(ky);

        log.info("[Seed] Hoan tat. Tai khoan mau: KH SDT 0900000001, the TVDEMO0001; quan ly id=1.");
    }

    private HangThanhVien luuHang(String ten, double phanTram) {
        HangThanhVien h = new HangThanhVien();
        h.setTenHang(ten);
        h.setPhanTramUuDai(phanTram);
        return hangThanhVienRepository.save(h);
    }

    private Phim luuPhim(String ten, String theLoai, int thoiLuong, String daoDien,
                         String dienVien, String trangThai, LocalDate khoiChieu) {
        Phim p = new Phim();
        p.setTenPhim(ten);
        p.setTheLoai(theLoai);
        p.setThoiLuong(thoiLuong);
        p.setDaoDien(daoDien);
        p.setDienVien(dienVien);
        p.setMoTa("Phim demo phục vụ đồ án quản lý rạp chiếu phim.");
        p.setNgayKhoiChieu(khoiChieu);
        p.setTrangThai(trangThai);
        return phimRepository.save(p);
    }

    private PhongChieu luuPhong(String tenPhong, String loaiPhong) {
        PhongChieu phong = new PhongChieu();
        phong.setTenPhong(tenPhong);
        phong.setLoaiPhong(loaiPhong);

        List<Ghe> ghes = new ArrayList<>();
        String[] hangGhe = {"A", "B", "C"};
        for (String hang : hangGhe) {
            String loaiGhe = "C".equals(hang) ? "VIP" : "Thường";
            for (int so = 1; so <= 8; so++) {
                Ghe ghe = new Ghe();
                ghe.setSoGhe(hang + so);
                ghe.setHangGhe(hang);
                ghe.setLoaiGhe(loaiGhe);
                ghe.setPhongChieu(phong);
                ghes.add(ghe);
            }
        }
        phong.setDanhSachGhe(ghes);
        phong.setSucChua(ghes.size());
        return phongChieuRepository.save(phong);
    }

    private void luuSuat(Phim phim, PhongChieu phong, LocalDate ngay, LocalTime gioBatDau, double gia) {
        SuatChieu s = new SuatChieu();
        s.setPhim(phim);
        s.setPhongChieu(phong);
        s.setNgayChieu(ngay);
        s.setGioBatDau(gioBatDau);
        s.setGioKetThuc(gioBatDau.plusMinutes(phim.getThoiLuong() + 15));
        s.setGiaVeMacDinh(gia);
        suatChieuRepository.save(s);
    }
}
