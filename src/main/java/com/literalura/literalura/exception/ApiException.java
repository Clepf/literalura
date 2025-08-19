package com.literalura.literalura.exception;

/**
 * Exceção para erros de comunicação com APIs externas
 */
public class ApiException extends LiteraluraException {

    private final int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}