package com.literalura.literalura.exception;

/**
 * Exceção para duplicação de dados
 */
public class DuplicateEntityException extends LiteraluraException {

    public DuplicateEntityException(String entityName, Object identifier) {
        super(String.format("%s já existe com identificador: %s", entityName, identifier));
    }

    public DuplicateEntityException(String message) {
        super(message);
    }
}