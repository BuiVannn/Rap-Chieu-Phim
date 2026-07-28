package com.rapchieuphim.exception;

/**
 * Nem khi khong tim thay mot doi tuong theo id/khoa (phim, suat chieu, ve, ky doanh thu...).
 */
public class KhongTimThayException extends RuntimeException {

    public KhongTimThayException(String message) {
        super(message);
    }
}
