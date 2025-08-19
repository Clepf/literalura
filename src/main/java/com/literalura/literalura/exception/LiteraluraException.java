package com.literalura.literalura.exception;

/**
 * Exceção base para o domínio LiterAlura
 */
public class LiteraluraException extends RuntimeException {
    public LiteraluraException(String message) { super(message); }
    public LiteraluraException(String message, Throwable cause) { super(message, cause); }
}
