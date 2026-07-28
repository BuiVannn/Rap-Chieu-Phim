package com.rapchieuphim.repository;

import com.rapchieuphim.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    Optional<KhachHang> findBySoDienThoai(String soDienThoai);

    Optional<KhachHang> findByEmail(String email);

    // MOI — thay the TheThanhVienRepository.findByMaThe() da bo (xem 00-quy-tac-chung.md)
    Optional<KhachHang> findByTheThanhVien_MaThe(String maThe);
}
