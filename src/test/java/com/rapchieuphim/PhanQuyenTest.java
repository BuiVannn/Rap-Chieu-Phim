package com.rapchieuphim;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Kiem chung phan quyen theo vai tro qua Spring Security.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PhanQuyenTest {

    @Autowired
    private MockMvc mockMvc;

    // ===== Khach vang lai (chua dang nhap) =====

    @Test
    void trangCongKhai_choPhep() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/dang-nhap")).andExpect(status().isOk());
        mockMvc.perform(get("/phim/tim-kiem")).andExpect(status().isOk());
    }

    @Test
    void trangCanQuyen_chuaDangNhap_chuyenVeDangNhap() throws Exception {
        mockMvc.perform(get("/quan-ly"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/dang-nhap"));
        mockMvc.perform(get("/quay/ban-ve/tim-suat-chieu"))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/ve-cua-toi"))
                .andExpect(status().is3xxRedirection());
    }

    // ===== Sai vai tro -> 403 =====

    @Test
    @WithMockUser(roles = "KHACHHANG")
    void khachHang_khongVaoDuocKhuQuanLy() throws Exception {
        mockMvc.perform(get("/quan-ly/chot-doanh-thu")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "BANHANG")
    void banHang_khongVaoDuocKhuQuanLy() throws Exception {
        mockMvc.perform(get("/quan-ly/chot-doanh-thu")).andExpect(status().isForbidden());
    }

    // ===== Dung vai tro -> vao duoc =====

    @Test
    @WithMockUser(roles = "QUANLY")
    void quanLy_vaoDuocChotDoanhThu() throws Exception {
        mockMvc.perform(get("/quan-ly/chot-doanh-thu"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("kỳ doanh thu")));
    }

    @Test
    @WithMockUser(roles = "QUANLY")
    void quanLy_cungVaoDuocQuayVe() throws Exception {
        mockMvc.perform(get("/quay/ban-ve/tim-suat-chieu")).andExpect(status().isOk());
    }
}
