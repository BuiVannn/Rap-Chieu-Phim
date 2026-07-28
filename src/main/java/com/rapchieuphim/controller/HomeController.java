package com.rapchieuphim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Cac trang chu dieu huong (khong goi xu ly nghiep vu). Bo sung thuc te de chay demo:
 * GDChinhQuanLy va ChinhKhachHangView chi la man hinh menu.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String trangChuKhachHang() {
        return "trang-chu-khach-hang";
    }

    @GetMapping("/quan-ly")
    public String trangChuQuanLy() {
        return "trang-chu-quan-ly";
    }
}
