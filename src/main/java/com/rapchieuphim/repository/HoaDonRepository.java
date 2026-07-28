package com.rapchieuphim.repository;

import com.rapchieuphim.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    List<HoaDon> findByNgayLapBetween(LocalDateTime tuNgay, LocalDateTime denNgay);
}
