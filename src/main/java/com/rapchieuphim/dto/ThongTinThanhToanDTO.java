package com.rapchieuphim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Thong tin thanh toan do nguoi dung nhap, truyen vao KhachHangService.xuLyThanhToan()
 * va tiep tuc toi CongThanhToanClient.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongTinThanhToanDTO {

    // "The tin dung" | "Vi dien tu" | "Chuyen khoan"...
    private String phuongThuc;
    private String soThe;
    private String tenChuThe;
}
