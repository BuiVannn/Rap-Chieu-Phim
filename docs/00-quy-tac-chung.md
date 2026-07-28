# 00. Quy tắc chung

Tài liệu này là tham chiếu bắt buộc khi code, đảm bảo code khớp đúng thiết kế đã có trong báo cáo
(Chương 3 – Phân tích, Chương 4 – Thiết kế). Đọc đủ 5 file trong thư mục `docs/` trước khi viết bất
kỳ lớp nào.

## Kiến trúc 4 tầng (mục 4.1)

```
controller  (Presentation)  ->  service  (Business)  ->  repository  (Data Access)  ->  entity  (Database)
                                    |
                                    v
                                 client   (gọi hệ thống ngoài: Cổng thanh toán, Email/SMS)
```

**Quy tắc bắt buộc:** một lớp chỉ được gọi lớp ở tầng liền kề ngay dưới nó.
- `controller` KHÔNG được tự ý gọi thẳng `repository` hay `entity`.
- `controller` KHÔNG được tự ý gọi `client`.
- Chỉ `service` được gọi `repository` và `client`.

## Quy ước đặt tên package

```
com.rapchieuphim.controller
com.rapchieuphim.service
com.rapchieuphim.client
com.rapchieuphim.repository
com.rapchieuphim.entity   (đã có sẵn trong khung project)
```

## ⚠️ Thay đổi quan trọng so với các hình đã vẽ ở 4.4b — đọc kỹ

Ở mục 4.3 (Thiết kế CSDL), bảng `tbl_the_thanh_vien` đã được **gộp vào** `tbl_khach_hang` (quan hệ
1-1 bắt buộc: mỗi khách hàng có tài khoản đều có đúng 1 thẻ thành viên). Trong code, điều này được
hiện thực bằng `@Embeddable` (xem `entity/TheThanhVien.java`) — lớp `TheThanhVien` không còn là một
`@Entity` độc lập, không có bảng riêng, không có `id` riêng.

**Hệ quả:** `TheThanhVienRepository` (từng xuất hiện ở hình UC "Đặt vé trực tuyến" và UC "Đăng ký thẻ
thành viên") **không được tạo ra trong code** — vì Spring Data JPA không cho phép tạo Repository cho
một `@Embeddable`. Toàn bộ thao tác liên quan tới thẻ thành viên chuyển sang gọi qua
`KhachHangRepository` (xem chi tiết trong `01-repository.md` và `02-service.md`).

Nếu bạn đối chiếu lại ảnh các hình đã vẽ trong Visual Paradigm và thấy khác với tài liệu này ở đúng
điểm này — đó là điều chỉnh có chủ đích, không phải sai sót, không cần vẽ lại hình.

## Quy tắc đặt tên phương thức Repository (Spring Data JPA)

Không cần viết SQL — chỉ cần đặt tên phương thức đúng cú pháp, Spring Data JPA tự sinh câu lệnh:
- `findBy<TenThuocTinh>` — tìm theo 1 thuộc tính (VD `findByTenPhong`).
- `findBy<TenThuocTinh>And<TenThuocTinh2>` — tìm theo nhiều điều kiện.
- `findBy<TenThuocTinh>ContainingIgnoreCase` — tìm gần đúng, không phân biệt hoa/thường.
- `findBy<TenThuocTinh>In` — tìm theo danh sách giá trị.
- `findBy<TenThuocTinh>Between` — tìm trong khoảng.
- `findBy<TenQuanHe>_<ThuocTinhCuaQuanHe>` — tìm qua thuộc tính của lớp liên kết/nhúng (dùng dấu `_`),
  VD `findByTheThanhVien_MaThe` nghĩa là tìm `KhachHang` có `theThanhVien.maThe` khớp giá trị truyền vào.

## Thứ tự viết code khuyến nghị

1. Repository (dễ nhất, chỉ là interface, xem `01-repository.md`)
2. Client (2 lớp, chỉ là khung gọi API ngoài, tạm thời có thể trả dữ liệu giả để test trước)
3. Service (chứa logic nghiệp vụ, xem `02-service.md`)
4. Controller (xem `03-controller.md`)
5. View — Thymeleaf template (xem `04-view.md`)
