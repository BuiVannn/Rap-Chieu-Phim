package com.rapchieuphim.repository;

import com.rapchieuphim.entity.Ve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeRepository extends JpaRepository<Ve, Long> {

    List<Ve> findBySuatChieuId(Long suatChieuId);

    // Liet ke ve theo khach hang (qua hoa don) — phuc vu man "Ve cua toi"
    List<Ve> findByHoaDon_KhachHang_IdOrderByIdDesc(Long khachHangId);
}
