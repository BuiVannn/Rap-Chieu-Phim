package com.rapchieuphim.repository;

import com.rapchieuphim.entity.Ghe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GheRepository extends JpaRepository<Ghe, Long> {

    List<Ghe> findByPhongChieuId(Long phongChieuId);
}
