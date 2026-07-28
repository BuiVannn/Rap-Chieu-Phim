package com.rapchieuphim.repository;

import com.rapchieuphim.entity.GiaVeTheoLoaiGhe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiaVeTheoLoaiGheRepository extends JpaRepository<GiaVeTheoLoaiGhe, Long> {
    // Khong can them phuong thuc nao
}
