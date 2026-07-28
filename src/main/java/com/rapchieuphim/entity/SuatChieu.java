package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_suat_chieu")
public class SuatChieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngay_chieu", nullable = false)
    private LocalDate ngayChieu;

    @Column(name = "gio_bat_dau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "gio_ket_thuc", nullable = false)
    private LocalTime gioKetThuc;

    @Column(name = "gia_ve_mac_dinh", nullable = false)
    private double giaVeMacDinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phim_id", nullable = false)
    private Phim phim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phong_chieu_id", nullable = false)
    private PhongChieu phongChieu;

    // Gan chat: 1 suat chieu co 0..n gia ve rieng theo loai ghe (tuy chon)
    @OneToMany(mappedBy = "suatChieu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GiaVeTheoLoaiGhe> danhSachGiaTheoLoaiGhe = new ArrayList<>();
}
