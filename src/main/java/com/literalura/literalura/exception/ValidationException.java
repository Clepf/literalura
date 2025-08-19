package com.literalura.literalura.exception;

/**
 * Exceção para erros de validação de dados
 */
public class ValidationException extends LiteraluraException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}