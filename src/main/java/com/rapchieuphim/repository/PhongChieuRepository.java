package com.rapchieuphim.repository;

import com.rapchieuphim.entity.PhongChieu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhongChieuRepository extends JpaRepository<PhongChieu, Long> {
    // Khong can them phuong thuc nao
}
