package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_phim")
public class Phim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_phim", nullable = false, length = 200)
    private String tenPhim;

    @Column(name = "the_loai", length = 50)
    private String theLoai;

    @Column(name = "thoi_luong", nullable = false)
    private int thoiLuong;

    @Column(name = "dao_dien", length = 100)
    private String daoDien;

    @Column(name = "dien_vien", length = 255)
    private String dienVien;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "poster", length = 255)
    private String poster;

    @Column(name = "ngay_khoi_chieu", nullable = false)
    private LocalDate ngayKhoiChieu;

    // "Sap chieu" | "Dang chieu" | "Ngung chieu"
    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai;
}
