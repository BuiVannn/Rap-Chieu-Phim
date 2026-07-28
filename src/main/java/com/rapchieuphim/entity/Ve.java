package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_ve")
public class Ve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Trong" | "Dang giu" | "Da ban" | "Da huy"
    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai;

    @Column(name = "gia_ve_ap_dung", nullable = false)
    private double giaVeApDung;

    @Column(name = "ma_qr", unique = true, length = 100)
    private String maQR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suat_chieu_id", nullable = false)
    private SuatChieu suatChieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ghe_id", nullable = false)
    private Ghe ghe;

    // Cho phep NULL: ve duoc tao luc giu ghe tam, truoc khi hoa don duoc lap
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = true)
    private HoaDon hoaDon;
}
