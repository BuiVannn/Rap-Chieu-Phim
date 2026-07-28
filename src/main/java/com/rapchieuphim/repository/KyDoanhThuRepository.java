package com.rapchieuphim.repository;

import com.rapchieuphim.entity.KyDoanhThu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KyDoanhThuRepository extends JpaRepository<KyDoanhThu, Long> {

    List<KyDoanhThu> findByTrangThai(String trangThai);
}
