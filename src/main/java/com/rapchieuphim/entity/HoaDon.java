package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_hoa_don")
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngay_lap", nullable = false)
    private LocalDateTime ngayLap;

    @Column(name = "tong_tien", nullable = false)
    private double tongTien;

    @Column(name = "hinh_thuc_thanh_toan", length = 30)
    private String hinhThucThanhToan;

    // "Truc tuyen" | "Tai quay"
    @Column(name = "hinh_thuc_ban", nullable = false, length = 20)
    private String hinhThucBan;

    // Cho phep NULL: khach vang lai mua tai quay khong co tai khoan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id", nullable = true)
    private KhachHang khachHang;

    // Cho phep NULL: hoa don dat truc tuyen khong co nhan vien xu ly
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhan_vien_ban_hang_id", nullable = true)
    private NhanVienBanHang nhanVienBanHang;

    // Cho phep NULL: chi duoc gan khi ky doanh thu duoc chot
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ky_doanh_thu_id", nullable = true)
    private KyDoanhThu kyDoanhThu;

    // Hop thanh: 1 hoa don co nhieu ve
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL)
    private List<Ve> danhSachVe = new ArrayList<>();
}
