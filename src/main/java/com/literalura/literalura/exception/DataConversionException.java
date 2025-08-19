package com.literalura.literalura.exception;

/**
 * Exceção para erros de conversão de dados
 */
public class DataConversionException extends LiteraluraException {

    public DataConversionException(String message) {
        super(message);
    }

    public DataConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}