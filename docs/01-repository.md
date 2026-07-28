# 01. Repository — package `com.rapchieuphim.repository`

11 interface, đều kế thừa `JpaRepository<Entity, Long>`. Chỉ liệt kê phương thức **tự viết thêm**
ngoài các phương thức có sẵn (`findById`, `save`, `saveAll`, `deleteById`...).

## PhimRepository
```java
public interface PhimRepository extends JpaRepository<Phim, Long> {
    List<Phim> findByTrangThaiIn(List<String> danhSachTrangThai);
    List<Phim> findByTenPhimContainingIgnoreCase(String tuKhoa);
}
```

## PhongChieuRepository
```java
public interface PhongChieuRepository extends JpaRepository<PhongChieu, Long> {
    // Không cần thêm phương thức nào
}
```

## SuatChieuRepository
```java
public interface SuatChieuRepository extends JpaRepository<SuatChieu, Long> {
    List<SuatChieu> findByPhongChieuIdAndNgayChieu(Long phongChieuId, LocalDate ngayChieu);
    List<SuatChieu> findByPhimId(Long phimId);
}
```

## GiaVeTheoLoaiGheRepository
```java
public interface GiaVeTheoLoaiGheRepository extends JpaRepository<GiaVeTheoLoaiGhe, Long> {
    // Không cần thêm phương thức nào
}
```

## GheRepository
```java
public interface GheRepository extends JpaRepository<Ghe, Long> {
    List<Ghe> findByPhongChieuId(Long phongChieuId);
}
```

## VeRepository
```java
public interface VeRepository extends JpaRepository<Ve, Long> {
    List<Ve> findBySuatChieuId(Long suatChieuId);
}
```

## HoaDonRepository
```java
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    List<HoaDon> findByNgayLapBetween(LocalDateTime tuNgay, LocalDateTime denNgay);
}
```

## KhachHangRepository
```java
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    Optional<KhachHang> findBySoDienThoai(String soDienThoai);
    Optional<KhachHang> findByEmail(String email);

    // MỚI — thay thế TheThanhVienRepository.findByMaThe() đã bỏ (xem 00-quy-tac-chung.md)
    Optional<KhachHang> findByTheThanhVien_MaThe(String maThe);
}
```

## HangThanhVienRepository
```java
public interface HangThanhVienRepository extends JpaRepository<HangThanhVien, Long> {
    Optional<HangThanhVien> findByTenHang(String tenHang);
}
```

## KyDoanhThuRepository
```java
public interface KyDoanhThuRepository extends JpaRepository<KyDoanhThu, Long> {
    List<KyDoanhThu> findByTrangThai(String trangThai);
}
```

## NhanVienQuanLyRepository
```java
public interface NhanVienQuanLyRepository extends JpaRepository<NhanVienQuanLy, Long> {
    // Không cần thêm phương thức nào
}
```

---

## ❌ Không tạo — đã loại bỏ

`TheThanhVienRepository` — **không tạo lớp này**. `TheThanhVien` là `@Embeddable`, không có bảng/id
riêng, không thể có Repository. Xem `00-quy-tac-chung.md` để biết lý do và cách thay thế.

`NhanVienBanHangRepository` — **không cần**, vì không có Repository nào trong 4.4b từng dùng lớp
này làm đối tượng truy vấn riêng (chỉ dùng làm khóa ngoại trong `HoaDon`, đã có sẵn qua
`@ManyToOne` trong `entity/HoaDon.java`, không cần Repository riêng).
