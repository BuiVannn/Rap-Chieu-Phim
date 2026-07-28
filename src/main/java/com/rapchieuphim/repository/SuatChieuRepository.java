package com.rapchieuphim.repository;

import com.rapchieuphim.entity.SuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SuatChieuRepository extends JpaRepository<SuatChieu, Long> {

    List<SuatChieu> findByPhongChieuIdAndNgayChieu(Long phongChieuId, LocalDate ngayChieu);

    List<SuatChieu> findByPhimId(Long phimId);
}
