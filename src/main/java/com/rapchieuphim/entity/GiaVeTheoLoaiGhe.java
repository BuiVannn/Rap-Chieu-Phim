package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_gia_ve_theo_loai_ghe")
public class GiaVeTheoLoaiGhe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loai_ghe", nullable = false, length = 20)
    private String loaiGhe;

    @Column(name = "gia_ve", nullable = false)
    private double giaVe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suat_chieu_id", nullable = false)
    private SuatChieu suatChieu;
}
