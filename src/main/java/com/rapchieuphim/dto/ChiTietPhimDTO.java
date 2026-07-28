package com.rapchieuphim.dto;

import com.rapchieuphim.entity.Phim;
import com.rapchieuphim.entity.SuatChieu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Ket qua cua KhachHangService.layChiTietPhim(): thong tin phim + cac suat chieu cua phim.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhimDTO {

    private Phim phim;
    private List<SuatChieu> danhSachSuatChieu;
}
