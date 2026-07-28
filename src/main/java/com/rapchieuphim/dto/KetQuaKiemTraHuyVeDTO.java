package com.rapchieuphim.dto;

import com.rapchieuphim.entity.Ve;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ket qua kiem tra dieu kien huy ve (KhachHangService.kiemTraDieuKienHuyVe()).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaKiemTraHuyVeDTO {

    private Ve ve;
    private boolean duocHuy;
    private String lyDo;
    private double soTienHoanDuKien;
}
