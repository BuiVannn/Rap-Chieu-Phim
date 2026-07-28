package com.rapchieuphim.exception;

/**
 * Nem khi cong thanh toan tra ve ket qua that bai.
 */
public class ThanhToanThatBaiException extends RuntimeException {

    public ThanhToanThatBaiException(String message) {
        super(message);
    }
}
