package com.rapchieuphim.exception;

/**
 * Nem khi thao tac tren mot ky doanh thu da chot (khong duoc chot lai, khong duoc sua hoa don thuoc ky).
 */
public class KyDaChotException extends RuntimeException {

    public KyDaChotException(String message) {
        super(message);
    }
}
