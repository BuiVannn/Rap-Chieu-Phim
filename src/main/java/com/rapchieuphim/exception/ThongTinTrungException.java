package com.rapchieuphim.exception;

/**
 * Nem khi dang ky the thanh vien voi so dien thoai hoac email da ton tai.
 */
public class ThongTinTrungException extends RuntimeException {

    public ThongTinTrungException(String message) {
        super(message);
    }
}
