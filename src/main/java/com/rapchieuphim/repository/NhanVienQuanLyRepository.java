package com.rapchieuphim.repository;

import com.rapchieuphim.entity.NhanVienQuanLy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienQuanLyRepository extends JpaRepository<NhanVienQuanLy, Long> {
    // Khong can them phuong thuc nao
}
