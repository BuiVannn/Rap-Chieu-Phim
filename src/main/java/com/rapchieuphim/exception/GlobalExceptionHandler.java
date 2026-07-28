package com.rapchieuphim.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Bat cac exception nghiep vu va hien thi trang loi (templates/loi.html) thay vi tra ve stack trace.
 * View la tang tren cung, nen day la noi tap trung xu ly loi cho toan bo Controller.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            KhongTimThayException.class,
            TrungLichChieuException.class,
            GheKhongCoSanException.class,
            HuyVeQuaHanException.class,
            ThongTinTrungException.class,
            KyDaChotException.class,
            ThanhToanThatBaiException.class,
            SuatChieuDaQuaException.class
    })
    public String xuLyLoiNghiepVu(RuntimeException ex, Model model) {
        model.addAttribute("thongBaoLoi", ex.getMessage());
        return "loi";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String xuLyLoiThamSo(IllegalArgumentException ex, Model model) {
        model.addAttribute("thongBaoLoi", "Du lieu khong hop le: " + ex.getMessage());
        return "loi";
    }
}
