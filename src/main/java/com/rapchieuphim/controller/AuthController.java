package com.rapchieuphim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Hien thi trang dang nhap. Viec xu ly POST /dang-nhap va /dang-xuat do Spring Security dam nhan.
 */
@Controller
public class AuthController {

    @GetMapping("/dang-nhap")
    public String hienThiDangNhap() {
        return "dang-nhap";
    }
}
