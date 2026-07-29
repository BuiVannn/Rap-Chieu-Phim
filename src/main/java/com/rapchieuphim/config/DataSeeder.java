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
    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(HangThanhVienRepository hangThanhVienRepository,
                      PhimRepository phimRepository,
                      PhongChieuRepository phongChieuRepository,
                      SuatChieuRepository suatChieuRepository,
                      KyDoanhThuRepository kyDoanhThuRepository,
                      KhachHangRepository khachHangRepository,
                      NhanVienQuanLyRepository nhanVienQuanLyRepository,
                      NguoiDungRepository nguoiDungRepository,
                      PasswordEncoder passwordEncoder) {
        this.hangThanhVienRepository = hangThanhVienRepository;
        this.phimRepository = phimRepository;
        this.phongChieuRepository = phongChieuRepository;
        this.suatChieuRepository = suatChieuRepository;
        this.kyDoanhThuRepository = kyDoanhThuRepository;
        this.khachHangRepository = khachHangRepository;
        this.nhanVienQuanLyRepository = nhanVienQuanLyRepository;
        this.nguoiDungRepository = nguoiDungRepository;
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

        // 2b. Nhan vien ban hang (dang nhap quay ve)
        NhanVienBanHang banHang = new NhanVienBanHang();
        banHang.setHoTen("Lê Bán Hàng");
        banHang.setSoDienThoai("0912000002");
        banHang.setEmail("banhang@cgvmini.vn");
        banHang.setMatKhau(passwordEncoder.encode("123456"));
        banHang.setChucVu("Nhân viên bán hàng");
        nguoiDungRepository.save(banHang);

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

        // 4. Phim — >= 20 phim (phan lon "Dang chieu" de xep suat, vai phim "Sap chieu")
        List<Phim> phimDangChieu = new ArrayList<>();
        phimDangChieu.add(luuPhim("Vũ Trụ Song Song", "Khoa học viễn tưởng", 132, "Trần Anh", "Minh Hằng, Quốc Trường", "Đang chiếu", LocalDate.of(2026, 8, 10)));
        phimDangChieu.add(luuPhim("Bí Ẩn Nửa Đêm", "Kinh dị", 105, "Lê Bình", "Kaity Nguyễn", "Đang chiếu", LocalDate.of(2026, 8, 12)));
        phimDangChieu.add(luuPhim("Hành Tinh Đỏ", "Phiêu lưu", 120, "Phạm Hùng", "Isaac, Diễm My", "Đang chiếu", LocalDate.of(2026, 8, 15)));
        phimDangChieu.add(luuPhim("Đại Dương Sâu Thẳm", "Tài liệu", 96, "Ngô Thanh Vân", "Liên Bỉnh Phát", "Đang chiếu", LocalDate.of(2026, 8, 5)));
        phimDangChieu.add(luuPhim("Sát Thủ Vô Hình", "Hành động", 118, "Victor Vũ", "Trấn Thành", "Đang chiếu", LocalDate.of(2026, 8, 20)));
        phimDangChieu.add(luuPhim("Tiếng Gọi Nơi Hoang Dã", "Phiêu lưu", 110, "Charlie Nguyễn", "Ninh Dương Lan Ngọc", "Đang chiếu", LocalDate.of(2026, 8, 8)));
        phimDangChieu.add(luuPhim("Người Nhện Trở Lại", "Siêu anh hùng", 140, "Jon Watts", "Tom Holland", "Đang chiếu", LocalDate.of(2026, 8, 18)));
        phimDangChieu.add(luuPhim("Cô Dâu Ma", "Kinh dị", 100, "Trần Hữu Tấn", "Lâm Thanh Mỹ", "Đang chiếu", LocalDate.of(2026, 8, 22)));
        phimDangChieu.add(luuPhim("Vượt Ngục", "Hành động", 125, "Lý Hải", "Huỳnh Đông", "Đang chiếu", LocalDate.of(2026, 8, 3)));
        phimDangChieu.add(luuPhim("Nụ Hôn Mùa Hạ", "Lãng mạn", 98, "Nguyễn Quang Dũng", "Diễm My 9x", "Đang chiếu", LocalDate.of(2026, 8, 25)));
        phimDangChieu.add(luuPhim("Thám Tử Lừng Danh", "Hình sự", 112, "Aoyama Gosho", "Kaito Kid", "Đang chiếu", LocalDate.of(2026, 8, 14)));
        phimDangChieu.add(luuPhim("Đảo Kinh Hoàng", "Kinh dị", 108, "James Wan", "Vera Farmiga", "Đang chiếu", LocalDate.of(2026, 8, 9)));
        phimDangChieu.add(luuPhim("Chiến Binh Bóng Đêm", "Siêu anh hùng", 135, "Matt Reeves", "Robert Pattinson", "Đang chiếu", LocalDate.of(2026, 8, 16)));
        phimDangChieu.add(luuPhim("Cuộc Đua Kỳ Thú", "Hài", 92, "Đức Thịnh", "Thu Trang", "Đang chiếu", LocalDate.of(2026, 8, 26)));
        phimDangChieu.add(luuPhim("Rồng Thần Trở Lại", "Hoạt hình", 95, "Studio Ghibli", "Lồng tiếng Việt", "Đang chiếu", LocalDate.of(2026, 8, 1)));
        phimDangChieu.add(luuPhim("Băng Đảng Phố Đông", "Tội phạm", 128, "Ngô Vũ Sâm", "Châu Nhuận Phát", "Đang chiếu", LocalDate.of(2026, 8, 19)));
        phimDangChieu.add(luuPhim("Giai Điệu Tình Yêu", "Nhạc kịch", 103, "Damien Chazelle", "Emma Stone", "Đang chiếu", LocalDate.of(2026, 8, 11)));
        phimDangChieu.add(luuPhim("Mật Mã Tử Thần", "Hình sự", 116, "David Fincher", "Brad Pitt", "Đang chiếu", LocalDate.of(2026, 8, 7)));

        luuPhim("Kỷ Băng Hà 6", "Hoạt hình", 90, "Blue Sky", "Lồng tiếng Việt", "Sắp chiếu", LocalDate.of(2026, 9, 5));
        luuPhim("Chuyến Tàu Sinh Tử", "Kinh dị", 118, "Yeon Sang-ho", "Gong Yoo", "Sắp chiếu", LocalDate.of(2026, 9, 10));
        luuPhim("Huyền Thoại Biển Xanh", "Giả tưởng", 122, "Jang Tae-yu", "Lee Min Ho", "Sắp chiếu", LocalDate.of(2026, 9, 12));
        luuPhim("Đế Chế Sụp Đổ", "Lịch sử", 145, "Ridley Scott", "Russell Crowe", "Sắp chiếu", LocalDate.of(2026, 9, 20));

        // 5. Phong chieu + ghe (4 phong, loai khac nhau)
        List<PhongChieu> phongs = List.of(
                luuPhong("Phòng 1", "2D"),
                luuPhong("Phòng 2", "3D"),
                luuPhong("Phòng 3", "IMAX"),
                luuPhong("Phòng 4", "2D"));

        // 6. Suat chieu — >= 30 suat, tat ca tu 30/8/2026 tro di
        LocalDate ngayBatDau = LocalDate.of(2026, 8, 30);
        LocalTime[] khungGio = {LocalTime.of(10, 0), LocalTime.of(13, 30), LocalTime.of(17, 0), LocalTime.of(20, 0)};
        int phimIdx = 0;
        for (int d = 0; d < 7; d++) {                     // 7 ngay: 30/8 -> 5/9
            LocalDate ngay = ngayBatDau.plusDays(d);
            int soSlot = d < 3 ? 2 : 1;                   // 3 ngay dau 2 suat/phong, con lai 1 suat/phong
            for (int r = 0; r < phongs.size(); r++) {
                PhongChieu phong = phongs.get(r);
                for (int s = 0; s < soSlot; s++) {
                    LocalTime gio = khungGio[(r + s) % khungGio.length];
                    Phim phim = phimDangChieu.get(phimIdx % phimDangChieu.size());
                    phimIdx++;
                    luuSuat(phim, phong, ngay, gio, giaTheoPhong(phong));
                }
            }
        }

        // 7. Ky doanh thu thang 8/2026 (chua chot) — khop moc demo 30/8/2026
        luuKy(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31));
        luuKy(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));

        log.info("[Seed] Hoan tat: {} phim, {} suat chieu, {} phong. Tai khoan demo mat khau 123456.",
                phimRepository.count(), suatChieuRepository.count(), phongs.size());
    }

    private void luuKy(LocalDate batDau, LocalDate ketThuc) {
        KyDoanhThu ky = new KyDoanhThu();
        ky.setNgayBatDau(batDau);
        ky.setNgayKetThuc(ketThuc);
        ky.setTrangThai("Chưa chốt");
        kyDoanhThuRepository.save(ky);
    }

    private double giaTheoPhong(PhongChieu phong) {
        return switch (phong.getLoaiPhong()) {
            case "IMAX" -> 130000;
            case "3D" -> 110000;
            default -> 85000;
        };
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
