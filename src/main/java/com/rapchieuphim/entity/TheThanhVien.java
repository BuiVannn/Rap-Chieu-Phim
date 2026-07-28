package com.rapchieuphim.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Theo thiet ke CSDL (muc 4.3), TheThanhVien duoc gop vao bang tbl_khach_hang
 * (quan he 1-1 bat buoc) nen dung @Embeddable, khong phai @Entity rieng.
 */
@Getter
@Setter
@Embeddable
public class TheThanhVien {

    @Column(name = "ma_the", nullable = false, unique = true, length = 20)
    private String maThe;

    @Column(name = "ngay_dang_ky", nullable = false)
    private LocalDate ngayDangKy;
}
