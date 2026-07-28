package com.rapchieuphim.client;

import com.rapchieuphim.entity.HoaDon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Dai dien actor phu "He thong Email/SMS". Giai doan demo: mock - chi ghi log thay vi gui that.
 */
@Component
public class EmailSmsClient {

    private static final Logger log = LoggerFactory.getLogger(EmailSmsClient.class);

    public void guiVeDienTu(String email, HoaDon hoaDon) {
        log.info("[MOCK Email/SMS] Gui ve dien tu cho {} - hoa don #{}, tong {} VND, {} ve",
                email, hoaDon.getId(), hoaDon.getTongTien(), hoaDon.getDanhSachVe().size());
    }

    public void guiThongBaoHuyVe(String email, HoaDon hoaDon, double soTienHoan) {
        Long hoaDonId = hoaDon != null ? hoaDon.getId() : null;
        log.info("[MOCK Email/SMS] Gui thong bao huy ve cho {} - hoa don #{}, hoan {} VND",
                email, hoaDonId, soTienHoan);
    }
}
