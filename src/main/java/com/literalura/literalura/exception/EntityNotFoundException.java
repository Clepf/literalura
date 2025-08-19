package com.literalura.literalura.exception;

/**
 * Exceção para entidades não encontradas
 */
public class EntityNotFoundException extends LiteraluraException {

    public EntityNotFoundException(String entityName, Object identifier) {
        super(String.format("%s não encontrado(a) com identificador: %s", entityName, identifier));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}