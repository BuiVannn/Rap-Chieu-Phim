package com.rapchieuphim;

import com.rapchieuphim.repository.PhimRepository;
import com.rapchieuphim.repository.SuatChieuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test: kiem tra toan bo ApplicationContext nap duoc (mapping JPA, wiring bean, Thymeleaf),
 * va DataSeeder da seed du lieu mau thanh cong tren H2.
 */
@SpringBootTest
class QuanLyRapChieuPhimApplicationTests {

    @Autowired
    private PhimRepository phimRepository;

    @Autowired
    private SuatChieuRepository suatChieuRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void dataSeederNapDuLieuMau() {
        // DataSeeder chay luc khoi dong -> phai co 3 phim va 3 suat chieu
        assertThat(phimRepository.count()).isEqualTo(3);
        assertThat(suatChieuRepository.count()).isEqualTo(3);
    }
}
