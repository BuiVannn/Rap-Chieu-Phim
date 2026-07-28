# 04. View — thư mục `src/main/resources/templates`

21 file `.html` (Thymeleaf), tên đổi từ PascalCase (lớp Biên trong 4.4b) sang kebab-case (quy ước đặt
tên file web thông thường). Không phải class Java, chỉ là file HTML — không tạo trong package `entity`
hay bất kỳ package Java nào.

| Lớp Biên (4.4b) | Tên file | Controller trả về View này |
|---|---|---|
| ChinhKhachHangView | `trang-chu-khach-hang.html` | (trang tĩnh, không thuộc riêng UC nào) |
| DanhSachPhimView | `danh-sach-phim.html` | TimKiemPhimController |
| ChiTietPhimView | `chi-tiet-phim.html` | TimKiemPhimController |
| ChonGheView | `chon-ghe.html` | DatVeTrucTuyenController, BanVeTaiQuayController *(dùng chung 1 file)* |
| ThanhToanView | `thanh-toan.html` | DatVeTrucTuyenController |
| KetQuaDatVeView | `ket-qua-dat-ve.html` | DatVeTrucTuyenController |
| ChonPhimNgayPhongView | `chon-phim-ngay-phong.html` | LapLichChieuController |
| ChonKhungGioView | `chon-khung-gio.html` | LapLichChieuController |
| ThietLapGiaRiengView | `thiet-lap-gia-rieng.html` | LapLichChieuController |
| TimSuatChieuQuayView | `tim-suat-chieu-quay.html` | BanVeTaiQuayController |
| TraCuuThanhVienView | `tra-cuu-thanh-vien.html` | BanVeTaiQuayController |
| ThanhToanQuayView | `thanh-toan-quay.html` | BanVeTaiQuayController |
| InHoaDonView | `in-hoa-don.html` | BanVeTaiQuayController |
| DanhSachVeCuaToiView | `danh-sach-ve-cua-toi.html` | HuyVeController |
| XacNhanHuyVeView | `xac-nhan-huy-ve.html` | HuyVeController |
| ThongBaoHuyVeView | `thong-bao-huy-ve.html` | HuyVeController |
| DangKyTheThanhVienView | `dang-ky-the-thanh-vien.html` | DangKyThanhVienController |
| KetQuaDangKyView | `ket-qua-dang-ky.html` | DangKyThanhVienController |
| DanhSachKyChuaChotView | `danh-sach-ky-chua-chot.html` | ChotDoanhThuController |
| ChiTietKyDoanhThuView | `chi-tiet-ky-doanh-thu.html` | ChotDoanhThuController |
| KetQuaChotKyView | `ket-qua-chot-ky.html` | ChotDoanhThuController |

## Lưu ý — 2 file dùng chung, đúng theo quan hệ include ở Chương 2/3

`chon-ghe.html` được cả `DatVeTrucTuyenController` và `BanVeTaiQuayController` trả về — khớp đúng
quan hệ `<<include>>` "Chọn ghế" dùng chung giữa UC "Đặt vé trực tuyến" và UC "Bán vé tại quầy" đã
xác định từ Chương 2. Chỉ khác dữ liệu Model truyền vào (có/không có thông tin nhân viên thao tác hộ).

## GDChinhQuanLy — không có trong danh sách Lớp Biên (4.4b), nhưng vẫn cần 1 trang thực tế

Trang chủ Nhân viên quản lý (`GDChinhQuanLy`) bị loại khỏi thiết kế phân tích vì chỉ là màn hình điều
hướng (không gọi xử lý gì). Khi code thật, vẫn cần tạo 1 file `trang-chu-quan-ly.html` đơn giản (menu
dẫn tới `/quan-ly/lap-lich-chieu` và `/quan-ly/chot-doanh-thu`) để ứng dụng chạy được — đây là bổ
sung thực tế cho phép chạy demo, không phải sai lệch thiết kế.
