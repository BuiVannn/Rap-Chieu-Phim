package com.rapchieuphim.exception;

/**
 * Nem khi giu ghe tam ma mot trong cac ghe da bi giu/ban boi giao dich khac.
 */
public class GheKhongCoSanException extends RuntimeException {

    public GheKhongCoSanException(String message) {
        super(message);
    }
}
