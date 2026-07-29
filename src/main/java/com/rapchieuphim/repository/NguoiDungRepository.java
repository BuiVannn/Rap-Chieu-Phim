package com.rapchieuphim.repository;

import com.rapchieuphim.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Truy van chung tren lop cha NguoiDung (bao gom ca KhachHang va Nhan vien) — phuc vu dang nhap.
 * Nho ke thua JOINED, findByEmail tra ve dung doi tuong lop con thuc su.
 */
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {

    Optional<NguoiDung> findByEmail(String email);
}
