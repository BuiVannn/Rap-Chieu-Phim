package com.rapchieuphim.exception;

/**
 * Nem khi thao tac dat/giu ghe cho mot suat chieu da bat dau (khong the ban ve nua).
 */
public class SuatChieuDaQuaException extends RuntimeException {

    public SuatChieuDaQuaException(String message) {
        super(message);
    }
}
