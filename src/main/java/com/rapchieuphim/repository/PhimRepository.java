package com.rapchieuphim.repository;

import com.rapchieuphim.entity.Phim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhimRepository extends JpaRepository<Phim, Long> {

    List<Phim> findByTrangThaiIn(List<String> danhSachTrangThai);

    List<Phim> findByTenPhimContainingIgnoreCase(String tuKhoa);
}
