package com.rapchieuphim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_phong_chieu")
public class PhongChieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_phong", nullable = false, unique = true, length = 50)
    private String tenPhong;

    @Column(name = "suc_chua", nullable = false)
    private int sucChua;

    @Column(name = "loai_phong", length = 20)
    private String loaiPhong;

    // Gan chat: 1 phong chieu co nhieu ghe
    @OneToMany(mappedBy = "phongChieu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ghe> danhSachGhe = new ArrayList<>();
}
