package com.rapchieuphim.client;

import com.rapchieuphim.dto.KetQuaThanhToanDTO;
import com.rapchieuphim.dto.ThongTinThanhToanDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Dai dien actor phu "Cong thanh toan". Day la mot trong hai noi duy nhat duoc phep goi he thong ngoai.
 * Giai doan demo: mock - luon tra ve thanh cong, sinh ma giao dich gia. Khi tich hop that chi can sua o day.
 */
@Component
public class CongThanhToanClient {

    private static final Logger log = LoggerFactory.getLogger(CongThanhToanClient.class);

    public KetQuaThanhToanDTO guiYeuCauThanhToan(double soTien, ThongTinThanhToanDTO thongTinThanhToan) {
        String phuongThuc = thongTinThanhToan != null ? thongTinThanhToan.getPhuongThuc() : "Khong ro";
        String maGiaoDich = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        log.info("[MOCK Cong thanh toan] Yeu cau thanh toan {} VND qua {} -> ma giao dich {}",
                soTien, phuongThuc, maGiaoDich);
        return new KetQuaThanhToanDTO(true, maGiaoDich, "Thanh toan thanh cong (mock)");
    }

    public void hoanTien(double soTien, String thongTinGiaoDich) {
        log.info("[MOCK Cong thanh toan] Hoan {} VND cho giao dich {}", soTien, thongTinGiaoDich);
    }
}
