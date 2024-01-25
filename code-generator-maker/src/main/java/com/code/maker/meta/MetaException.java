package com.code.maker.meta;

/**
 * @author ctc
 * @date 2024/1/25
 */
public class MetaException extends RuntimeException {

    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
