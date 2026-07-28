package com.rapchieuphim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Ket qua tong hop doanh thu cua mot ky (NhanVienQuanLyService.tongHopDoanhThuTheoKy()).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoDoanhThuDTO {

    private Long kyId;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String trangThai;
    private int tongSoVe;
    private int soHoaDon;
    private double tongDoanhThu;
    private double doanhThuTrucTuyen;
    private double doanhThuTaiQuay;
}
