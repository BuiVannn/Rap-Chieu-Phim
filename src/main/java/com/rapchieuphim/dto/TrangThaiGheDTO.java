package com.rapchieuphim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Mot phan tu trong so do ghe cua suat chieu (KhachHangService.layTrangThaiGhe()).
 * trangThai: "Trong" | "Dang giu" | "Da ban" | "Da huy".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrangThaiGheDTO {

    private Long gheId;
    private String soGhe;
    private String hangGhe;
    private String loaiGhe;
    private String trangThai;
    private double giaVe;
    // Id cua ve dang chiem ghe nay (Dang giu/Da ban); null neu ghe con trong.
    // Dung de luong dat ve online lay veId sau buoc giu ghe, chuyen sang thanh toan.
    private Long veId;
}
