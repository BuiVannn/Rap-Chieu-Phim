package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_khach_hang")
public class KhachHang extends NguoiDung {

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    // TheThanhVien duoc gop chung vao bang nay (xem lop TheThanhVien - @Embeddable)
    @Embedded
    private TheThanhVien theThanhVien;

    // Khach hang - Hang thanh vien: n-1, bat buoc, mac dinh hang "Thuong" khi tao moi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hang_thanh_vien_id", nullable = false)
    private HangThanhVien hangThanhVien;
}
