package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Lop cha truu tuong cho moi nguoi dung cua he thong.
 * Dung chien luoc JOINED: KhachHang/NhanVien se co bang rieng,
 * khoa chinh dong thoi la khoa ngoai tham chieu ve bang nguoi_dung.
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_nguoi_dung")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "so_dien_thoai", nullable = false, unique = true, length = 15)
    private String soDienThoai;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;
}
