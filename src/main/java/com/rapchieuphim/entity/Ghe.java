package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_ghe")
public class Ghe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "so_ghe", length = 10)
    private String soGhe;

    @Column(name = "hang_ghe", length = 10)
    private String hangGhe;

    @Column(name = "loai_ghe", length = 20)
    private String loaiGhe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phong_chieu_id", nullable = false)
    private PhongChieu phongChieu;
}
