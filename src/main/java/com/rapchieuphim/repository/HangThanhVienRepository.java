package com.rapchieuphim.repository;

import com.rapchieuphim.entity.HangThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HangThanhVienRepository extends JpaRepository<HangThanhVien, Long> {

    Optional<HangThanhVien> findByTenHang(String tenHang);
}
