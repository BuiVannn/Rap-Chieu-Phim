package com.rapchieuphim.exception;

/**
 * Nem khi khach yeu cau huy ve nhung khong con du thoi gian toi thieu truoc gio chieu.
 */
public class HuyVeQuaHanException extends RuntimeException {

    public HuyVeQuaHanException(String message) {
        super(message);
    }
}
