package com.rapchieuphim.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_nhan_vien_quan_ly")
public class NhanVienQuanLy extends NhanVien {
    // Khong co thuoc tinh rieng, ke thua toan bo tu NhanVien
}
