# 03. Controller — package `com.rapchieuphim.controller`

7 lớp `@Controller` (không dùng `@RestController` — trả về tên View Thymeleaf, không trả JSON).
Route (`@GetMapping`/`@PostMapping`) là đề xuất hợp lý dựa trên tên phương thức đã thiết kế ở 4.4b —
có thể đổi lại đường dẫn miễn giữ đúng tên phương thức và Service đã gọi.

## TimKiemPhimController — base `/phim`
```java
@GetMapping("/phim/tim-kiem")     public String timKiem(...)
@GetMapping("/phim/{id}")          public String xemChiTietPhim(...)
```
Gọi: `KhachHangService`

## DatVeTrucTuyenController — base `/dat-ve`
```java
@GetMapping("/dat-ve/suat-chieu/{suatChieuId}/ghe")   public String xemSoDoGhe(...)
@PostMapping("/dat-ve/xac-nhan-ghe")                   public String xacNhanChonGhe(...)
@PostMapping("/dat-ve/ap-dung-uu-dai")                 public String apDungUuDai(...)
@PostMapping("/dat-ve/thanh-toan")                     public String xacNhanThanhToan(...)
```
Gọi: `KhachHangService`

## LapLichChieuController — base `/quan-ly/lap-lich-chieu`
```java
@GetMapping("/quan-ly/lap-lich-chieu")            public String layDanhSachPhim(...)
@GetMapping("/quan-ly/lap-lich-chieu/khung-gio")  public String xemKhungGio(...)
@PostMapping("/quan-ly/lap-lich-chieu/tao")       public String xacNhanTaoSuatChieu(...)
@PostMapping("/quan-ly/lap-lich-chieu/gia-rieng") public String luuGiaRieng(...)
```
Gọi: `NhanVienQuanLyService`

## BanVeTaiQuayController — base `/quay/ban-ve`
```java
@GetMapping("/quay/ban-ve/tim-suat-chieu")          public String timSuatChieu(...)
@PostMapping("/quay/ban-ve/xac-nhan-ghe")            public String xacNhanChonGhe(...)
@GetMapping("/quay/ban-ve/tra-cuu-thanh-vien")       public String traCuuThanhVien(...)
@PostMapping("/quay/ban-ve/ap-dung-uu-dai")          public String apDungUuDai(...)
@PostMapping("/quay/ban-ve/thanh-toan")              public String xacNhanThanhToanQuay(...)
@GetMapping("/quay/ban-ve/in-hoa-don/{hoaDonId}")    public String inHoaDon(...)
```
Gọi: `NhanVienBanHangService`

## HuyVeController — base `/ve-cua-toi`
```java
@GetMapping("/ve-cua-toi")                public String chonVeCanHuy(...)
@PostMapping("/ve-cua-toi/{veId}/huy")     public String xacNhanHuyVe(...)
```
Gọi: `KhachHangService`

## DangKyThanhVienController — base `/thanh-vien`
```java
@GetMapping("/thanh-vien/dang-ky")     public String hienThiFormDangKy(...)
@PostMapping("/thanh-vien/dang-ky")    public String xacNhanDangKy(...)
```
Gọi: `KhachHangService`

## ChotDoanhThuController — base `/quan-ly/chot-doanh-thu`
```java
@GetMapping("/quan-ly/chot-doanh-thu")                    public String xemDanhSachKyChuaChot(...)
@GetMapping("/quan-ly/chot-doanh-thu/{kyId}")              public String xemChiTietKy(...)
@PostMapping("/quan-ly/chot-doanh-thu/{kyId}/xac-nhan")    public String xacNhanChotKy(...)
```
Gọi: `NhanVienQuanLyService`

## Lưu ý — `@Autowired`/constructor injection

Mỗi Controller chỉ inject đúng 1 Service tương ứng (theo đúng bảng phía trên), không inject
Repository hay Client trực tiếp — vi phạm quy tắc "chỉ gọi tầng liền kề" ở `00-quy-tac-chung.md`.
