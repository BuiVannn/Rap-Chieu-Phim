# Hệ thống quản lý rạp chiếu phim (Spring Boot)

Đồ án Phân tích thiết kế HTTT — hiện thực hóa thiết kế UML (Chương 3, 4) thành ứng dụng web
Spring Boot MVC + Thymeleaf + MySQL, kiến trúc phân lớp 4 tầng.

## Kiến trúc & package

```
com.rapchieuphim
├── controller/   7 Controller MVC (+ HomeController điều hướng)
├── service/      3 Service nghiệp vụ (KhachHang, NhanVienBanHang, NhanVienQuanLy)
├── client/       2 Client mock actor phụ (Cổng thanh toán, Email/SMS)
├── repository/   11 Spring Data JPA repository
├── dto/          6 DTO cho các kết quả trả về
├── exception/    Exception nghiệp vụ + GlobalExceptionHandler
├── config/       AppConfig (BCrypt) + DataSeeder (dữ liệu mẫu)
└── entity/       15 lớp Entity (JPA)
```
Quy tắc: mỗi tầng chỉ gọi tầng liền kề ngay dưới. Xem `docs/` để đối chiếu thiết kế.

## Cách 1 — Chạy nhanh bằng Docker (khuyến nghị, không cần cài Java/MySQL)

```bash
docker compose up --build
```
Mở http://localhost:8080 . Lệnh này dựng cả ứng dụng + MySQL, tự seed dữ liệu mẫu.

## Cách 2 — Chạy bằng Maven trên máy

Yêu cầu: JDK 17, MySQL đang chạy. Sửa mật khẩu MySQL qua biến môi trường hoặc
`application.properties`, rồi:
```bash
mvn spring-boot:run
```
Mặc định kết nối `jdbc:mysql://localhost:3306/quan_ly_rap_chieu_phim` (tự tạo DB nếu chưa có).

## Tài khoản / dữ liệu mẫu (tự seed khi DB trống)

- Khách hàng thành viên: SĐT `0900000001`, mã thẻ `TVDEMO0001` (hạng Thường).
- Nhân viên quản lý: `id = 1` (dùng khi chốt kỳ doanh thu).
- 3 phim, 2 phòng (mỗi phòng 24 ghế), 3 suất chiếu, 1 kỳ doanh thu tháng hiện tại (chưa chốt).

## Các luồng demo chính

| Chức năng | Đường dẫn bắt đầu |
|---|---|
| Trang chủ khách hàng | `/` |
| Tìm phim & đặt vé trực tuyến | `/phim/tim-kiem` |
| Hủy vé | `/ve-cua-toi` |
| Đăng ký thẻ thành viên | `/thanh-vien/dang-ky` |
| Bán vé tại quầy | `/quay/ban-ve/tim-suat-chieu` |
| Khu quản lý | `/quan-ly` |
| Lập lịch chiếu | `/quan-ly/lap-lich-chieu` |
| Chốt doanh thu | `/quan-ly/chot-doanh-thu` |

## Cấu hình qua biến môi trường (dùng khi deploy)

| Biến | Mặc định | Ý nghĩa |
|---|---|---|
| `SPRING_DATASOURCE_URL` | MySQL localhost | Chuỗi JDBC |
| `SPRING_DATASOURCE_USERNAME` | `root` | User DB |
| `SPRING_DATASOURCE_PASSWORD` | — | Mật khẩu DB |
| `PORT` | `8080` | Cổng HTTP (cloud tự cấp) |
| `JPA_DDL_AUTO` | `update` | Chế độ Hibernate DDL |
| `HUY_VE_GIO_TOI_THIEU` | `2` | Số giờ tối thiểu trước suất chiếu được hủy vé |

## Deploy lên cloud

Ứng dụng đã đóng gói Docker và đọc cấu hình từ biến môi trường nên deploy được lên bất kỳ nền
tảng nào chạy container:
1. Đẩy code lên GitHub (`git init && git add . && git commit && git push`).
2. Trên Railway/Render: tạo service từ repo (nền tảng tự nhận `Dockerfile`), tạo thêm 1 MySQL,
   rồi đặt các biến `SPRING_DATASOURCE_*` trỏ tới MySQL đó. `PORT` do nền tảng tự cấp.
3. Deploy — Hibernate tự tạo bảng và DataSeeder tự nạp dữ liệu mẫu ở lần chạy đầu.

## Ghi chú thiết kế

- Không có đăng nhập (đúng phạm vi thiết kế): ID actor truyền qua tham số. BCrypt chỉ dùng để băm
  mật khẩu khi đăng ký thành viên (bổ sung ngoài thiết kế gốc).
- Cổng thanh toán và Email/SMS là mock (ghi log), thay code thật chỉ cần sửa trong `client/`.
- Xem `docs/` để biết đặc tả từng tầng và các điều chỉnh so với bản vẽ UML.
