package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_hang_thanh_vien")
public class HangThanhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_hang", nullable = false, unique = true, length = 50)
    private String tenHang;

    @Column(name = "phan_tram_uu_dai", nullable = false)
    private double phanTramUuDai;
}
