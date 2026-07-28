package com.rapchieuphim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ket qua cong thanh toan tra ve (CongThanhToanClient.guiYeuCauThanhToan()).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaThanhToanDTO {

    private boolean thanhCong;
    private String maGiaoDich;
    private String thongBao;
}
