package com.rapchieuphim.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_nhan_vien")
public abstract class NhanVien extends NguoiDung {

    @Column(name = "chuc_vu", nullable = false, length = 50)
    private String chucVu;
}
