# Bối cảnh dự án: Hệ thống quản lý rạp chiếu phim

Đây là đồ án môn Phân tích thiết kế hệ thống thông tin, đã hoàn tất pha Phân tích và Thiết kế
(có đầy đủ tài liệu UML). Đang trong giai đoạn hiện thực hóa thiết kế thành code Spring Boot.
Đọc file này trước khi thực hiện bất kỳ thay đổi nào.

## 1. Tổng quan nghiệp vụ

Hệ thống quản lý hoạt động của **một rạp chiếu phim**: quản lý phim/phòng chiếu/lịch chiếu, bán vé
(trực tuyến và tại quầy), quản lý thẻ thành viên, chốt doanh thu định kỳ.

**Actor:**
- Khách hàng — tìm phim, đặt vé trực tuyến, hủy vé, đăng ký thẻ thành viên.
- Nhân viên bán hàng — bán vé tại quầy cho khách vãng lai hoặc khách thành viên.
- Nhân viên quản lý — quản lý phim/phòng/lịch chiếu, chốt doanh thu.
- Actor phụ (hệ thống ngoài): Cổng thanh toán, Hệ thống Email/SMS.

**4 module:** Quản lý phim & Lập lịch chiếu | Bán vé & Đặt vé trực tuyến | Thành viên & Ưu đãi |
Chốt doanh thu & Báo cáo thống kê.

## 2. Quy tắc nghiệp vụ quan trọng — dễ code sai nếu không biết

- **Tài khoản = thẻ thành viên.** Không có khái niệm "khách hàng có tài khoản nhưng chưa là thành
  viên". Đăng ký tài khoản và đăng ký thẻ thành viên là MỘT hành động duy nhất. Mọi `KhachHang` đều
  có đúng 1 thẻ thành viên (quan hệ 1-1 bắt buộc), mặc định hạng "Thường" khi tạo.
- **Không có cơ chế tích điểm/nâng hạng tự động.** Hạng thành viên chỉ được nâng thủ công bởi Nhân
  viên quản lý (ngoài phạm vi các UC đã thiết kế chi tiết).
- **Vé được tạo ngay lúc "giữ ghế tạm"**, TRƯỚC KHI hóa đơn tồn tại — vì vậy `Ve.hoaDon` cho phép
  NULL. Hóa đơn chỉ được gán vào Vé sau khi thanh toán thành công.
- **Khách vãng lai không cần tài khoản** để mua vé tại quầy — vì vậy `HoaDon.khachHang` và
  `HoaDon.nhanVienBanHang` đều cho phép NULL (tùy kênh bán: tại quầy hay trực tuyến).
- **Kỳ doanh thu chưa chốt thì chưa có người chốt** — `KyDoanhThu.nhanVienQuanLy` và
  `KyDoanhThu.thoiDiemChot` chỉ có giá trị sau khi `trangThai` chuyển "Đã chốt". Sau khi đã chốt,
  không được tạo/sửa/xóa Hóa đơn thuộc kỳ đó nữa (ràng buộc nghiệp vụ, chưa hiện thực bằng code,
  cần tự cài đặt kiểm tra ở Service nếu triển khai đầy đủ).
- **`TheThanhVien` không có Repository riêng** — đã gộp vào bảng `KhachHang` (`@Embeddable`). Xem
  chi tiết lý do trong `docs/00-quy-tac-chung.md`.

## 3. Kiến trúc & công nghệ

Kiến trúc phân lớp 4 tầng: `controller` (Spring MVC + Thymeleaf) → `service` (nghiệp vụ) →
`repository` (Spring Data JPA) → `entity` (JPA/MySQL). Tầng `service` còn gọi thêm `client` (đại
diện actor phụ: `CongThanhToanClient`, `EmailSmsClient`).

**Quy tắc bắt buộc:** mỗi lớp chỉ được gọi lớp ở tầng liền kề ngay dưới — `controller` không được
gọi thẳng `repository`/`entity`/`client`.

Công nghệ: Java 17, Spring Boot 3.x, Spring Web, Spring Data JPA, Thymeleaf, MySQL, Lombok, Maven.

## 4. Cấu trúc package (đã có sẵn `entity`, còn thiếu 4 package kia)

```
com.rapchieuphim
├── controller/     (chưa code — xem docs/03-controller.md)
├── service/        (chưa code — xem docs/02-service.md)
├── client/         (chưa code — xem docs/02-service.md, phần dưới)
├── repository/     (chưa code — xem docs/01-repository.md)
└── entity/         (ĐÃ CODE ĐỦ 15 lớp)
```

## 5. Trạng thái hiện tại

- ✅ 15 lớp Entity đã viết đầy đủ, đúng annotation JPA (`entity/*.java`).
- ✅ `pom.xml`, `application.properties`, lớp `Application` chính đã có.
- ✅ Database: để `spring.jpa.hibernate.ddl-auto=update` cho Hibernate tự tạo 14 bảng khớp Entity.
- ❌ Repository, Service, Client, Controller, View (Thymeleaf) — **chưa viết**, đây là việc cần làm.

## 6. Tài liệu tham chiếu bắt buộc — đọc trước khi code từng tầng

Thư mục `docs/` (đã có trong project) chứa đặc tả chi tiết, đã đối chiếu khớp với toàn bộ báo cáo
UML (Chương 3, 4):
- `docs/00-quy-tac-chung.md` — quy tắc kiến trúc, đặt tên, và các thay đổi so với bản vẽ tay.
- `docs/01-repository.md` — đủ 11 Repository interface, có sẵn code mẫu.
- `docs/02-service.md` — đủ 3 Service + 2 Client, mỗi phương thức ghi rõ gọi gì bên trong.
- `docs/03-controller.md` — đủ 7 Controller, kèm route đề xuất.
- `docs/04-view.md` — 21 file Thymeleaf cần tạo, map đúng Controller nào trả về.

**Không tự đặt tên lớp/phương thức mới khác với các file trên** — toàn bộ tên đã được thiết kế và
xác nhận khớp với báo cáo UML nộp kèm đồ án. Nếu thấy cần thêm phương thức/lớp mới ngoài danh sách,
hỏi lại người dùng trước khi tự ý thêm, vì có thể ảnh hưởng tính nhất quán với báo cáo.

## 7. Thứ tự triển khai đề xuất

1. Repository (dễ nhất, chỉ interface trống hoặc 1-2 dòng).
2. Client (khung gọi API ngoài, tạm mock dữ liệu giả để test luồng chính trước).
3. Service (logic nghiệp vụ chính).
4. Controller.
5. View (Thymeleaf) — làm sau cùng, có thể làm rất đơn giản (form HTML thô) vì trọng tâm đồ án là
   kiến trúc/logic, không phải giao diện đẹp.

## 8. Mục tiêu triển khai

Sau khi code xong luồng chính, mục tiêu là **deploy lên một nền tảng cloud** (ví dụ Railway, Render)
để có link demo trực tiếp khi bảo vệ đồ án, không chỉ chạy `mvn spring-boot:run` ở terminal.
