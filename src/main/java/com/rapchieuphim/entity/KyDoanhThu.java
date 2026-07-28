package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_ky_doanh_thu")
public class KyDoanhThu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate ngayKetThuc;

    // "Chua chot" | "Da chot"
    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai;

    @Column(name = "thoi_diem_chot")
    private LocalDateTime thoiDiemChot;

    // Cho phep NULL: chi co gia tri khi da chot
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhan_vien_quan_ly_id", nullable = true)
    private NhanVienQuanLy nhanVienQuanLy;
}
