package com.rapchieuphim.exception;

/**
 * Nem khi tao suat chieu bi trung khung gio voi mot suat chieu da co trong cung phong, cung ngay.
 */
public class TrungLichChieuException extends RuntimeException {

    public TrungLichChieuException(String message) {
        super(message);
    }
}
