# 02. Service — package `com.rapchieuphim.service`

3 lớp `@Service`, mỗi lớp gắn với 1 actor, dùng chung xuyên nhiều UC (đúng thiết kế 4.4b).
Mỗi phương thức ghi kèm: Repository/Client cần gọi bên trong, và UC nào cần tới nó.

## NhanVienQuanLyService

```java
@Service
public class NhanVienQuanLyService {

    // UC: Tạo suất chiếu
    List<Phim> layDanhSachPhimDangChieu();
    // -> PhimRepository.findByTrangThaiIn(List.of("Sắp chiếu", "Đang chiếu"))

    List<LocalTime> layKhungGioTrong(Long phongChieuId, LocalDate ngayChieu);
    // -> SuatChieuRepository.findByPhongChieuIdAndNgayChieu(...)
    //    rồi tự tính các khoảng giờ còn trống trong ngày

    SuatChieu taoSuatChieu(Long phimId, Long phongChieuId, LocalDate ngayChieu,
                           LocalTime gioBatDau, Double giaVe);
    // -> PhongChieuRepository.findById(...)
    // -> SuatChieuRepository.findByPhongChieuIdAndNgayChieu(...) để kiểm tra trùng lịch
    //    (ném exception nếu trùng, VD TrungLichChieuException)
    // -> SuatChieuRepository.save(...)

    void luuGiaVeRieng(Long suatChieuId, List<GiaVeTheoLoaiGhe> danhSachGia);
    // -> GiaVeTheoLoaiGheRepository.saveAll(...)

    // UC: Chốt kỳ doanh thu
    List<KyDoanhThu> layDanhSachKyChuaChot();
    // -> KyDoanhThuRepository.findByTrangThai("Chưa chốt")

    Object tongHopDoanhThuTheoKy(Long kyId);
    // -> KyDoanhThuRepository.findById(...) để lấy khoảng ngày của kỳ
    // -> HoaDonRepository.findByNgayLapBetween(...)
    //    rồi tự tính tổng số vé, tổng doanh thu, tách theo hinhThucBan

    void doiChieuVaChotKy(Long kyId, double soTienThucThu, Long nhanVienQuanLyId);
    // -> KyDoanhThuRepository.findById/save(...) — cập nhật trangThai = "Đã chốt"
    // -> NhanVienQuanLyRepository.findById(nhanVienQuanLyId) — gán vào kyDoanhThu.nhanVienQuanLy
}
```

## KhachHangService

```java
@Service
public class KhachHangService {

    // UC: Tìm kiếm và xem thông tin phim
    List<Phim> timPhimTheoTuKhoa(String tuKhoa);
    // -> PhimRepository.findByTenPhimContainingIgnoreCase(tuKhoa)

    Object layChiTietPhim(Long phimId);
    // -> PhimRepository.findById(phimId)
    // -> SuatChieuRepository.findByPhimId(phimId)

    // UC: Đặt vé trực tuyến
    Object layTrangThaiGhe(Long suatChieuId);
    // -> SuatChieuRepository.findById(suatChieuId)
    // -> GheRepository.findByPhongChieuId(...)
    // -> VeRepository.findBySuatChieuId(suatChieuId)
    //    ghép 2 danh sách: ghế có Vé -> lấy trạng thái của Vé; ghế không có Vé -> "Trống"

    double giuGheTamThoi(Long suatChieuId, List<Long> gheIds);
    // -> VeRepository.saveAll(...) — tạo mới Ve, trangThai = "Đang giữ", hoaDon = null

    double apDungUuDaiThanhVien(String maThe, double tongTien);
    // -> KhachHangRepository.findByTheThanhVien_MaThe(maThe)  [ĐÃ SỬA — không còn TheThanhVienRepository]
    //    lấy khachHang.getHangThanhVien().getPhanTramUuDai() để tính giảm giá

    HoaDon xuLyThanhToan(List<Long> veIds, double tongTien, Long khachHangId, Object thongTinThanhToan);
    // -> CongThanhToanClient.guiYeuCauThanhToan(...)
    // -> VeRepository — cập nhật trangThai = "Đã bán" cho các Ve
    // -> HoaDonRepository.save(...) — tạo mới, hinhThucBan = "Trực tuyến"
    // -> gọi tiếp xuatVeDienTu(hoaDon)  (self-call, đúng Sequence Diagram đã vẽ)

    void xuatVeDienTu(HoaDon hoaDon);
    // -> EmailSmsClient.guiVeDienTu(...)

    // UC: Hủy vé
    Object kiemTraDieuKienHuyVe(Long veId);
    // -> VeRepository.findById(veId) -> đọc ve.getSuatChieu().getNgayChieu()/getGioBatDau()
    //    so với thời điểm hiện tại (đủ X giờ mới cho hủy)

    void xuLyHuyVe(Long veId);
    // -> VeRepository — cập nhật trangThai = "Đã hủy", maQR = null

    void hoanTien(Long hoaDonId, double soTienHoan);
    // -> CongThanhToanClient.hoanTien(...)
    // -> EmailSmsClient.guiThongBaoHuyVe(...)

    // UC: Đăng ký thẻ thành viên
    KhachHang dangKyTheThanhVien(String hoTen, String soDienThoai, String email, String matKhau);
    // -> KhachHangRepository.findBySoDienThoai(...)  — kiểm tra trùng
    // -> KhachHangRepository.findByEmail(...)        — kiểm tra trùng
    // -> HangThanhVienRepository.findByTenHang("Thường")
    // -> tạo KhachHang mới, set truc tiep truong theThanhVien (Embedded) — KHÔNG gọi repository
    //    riêng cho thẻ (vì TheThanhVien không còn Repository — xem 00-quy-tac-chung.md)
    // -> KhachHangRepository.save(...)
}
```

## NhanVienBanHangService

```java
@Service
public class NhanVienBanHangService {

    // UC: Bán vé tại quầy
    List<SuatChieu> timSuatChieu(String tuKhoa);
    // -> PhimRepository.findByTenPhimContainingIgnoreCase(tuKhoa) để tìm phim khớp tên
    // -> SuatChieuRepository.findByPhimId(...) cho từng phim tìm được
    //    (không cần thêm phương thức Repository mới — ghép 2 phương thức đã có)

    double giuGheTamThoi(Long suatChieuId, List<Long> gheIds);
    // -> giống hệt KhachHangService.giuGheTamThoi() — dùng chung VeRepository

    KhachHang traCuuTheThanhVien(String soDienThoai);
    // -> KhachHangRepository.findBySoDienThoai(soDienThoai)

    double apDungUuDaiThanhVien(KhachHang khachHang, double tongTien);
    // -> đã có sẵn KhachHang (từ traCuuTheThanhVien), chỉ cần đọc
    //    khachHang.getHangThanhVien().getPhanTramUuDai(), KHÔNG cần gọi Repository lần nữa

    HoaDon xuLyThanhToanTaiQuay(List<Long> veIds, double tongTien, KhachHang khachHang,
                                 NhanVienBanHang nhanVien, String hinhThucThanhToan);
    // -> VeRepository — cập nhật trạng thái "Đã bán"
    // -> HoaDonRepository.save(...) — hinhThucBan = "Tại quầy"

    void inHoaDon(Long hoaDonId);
    // -> HoaDonRepository.findById(hoaDonId) — lấy dữ liệu để in
}
```

---

# Client — package `com.rapchieuphim.client`

2 lớp đại diện actor phụ (Cổng thanh toán, Hệ thống Email/SMS). Đây là nơi **duy nhất** trong hệ
thống được phép gọi API/thư viện bên ngoài. Ở giai đoạn đầu, có thể để trả về dữ liệu giả (mock) để
test luồng chính trước, chưa cần tích hợp API thật ngay.

```java
@Component
public class CongThanhToanClient {
    Object guiYeuCauThanhToan(double soTien, Object thongTinThanhToan);
    void hoanTien(double soTien, Object thongTinGiaoDich);
}

@Component
public class EmailSmsClient {
    void guiVeDienTu(String email, HoaDon hoaDon);
    void guiThongBaoHuyVe(String email, HoaDon hoaDon, double soTienHoan);
}
```
