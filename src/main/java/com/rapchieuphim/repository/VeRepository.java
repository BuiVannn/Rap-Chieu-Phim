package com.rapchieuphim.repository;

import com.rapchieuphim.entity.Ve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeRepository extends JpaRepository<Ve, Long> {

    List<Ve> findBySuatChieuId(Long suatChieuId);
}
