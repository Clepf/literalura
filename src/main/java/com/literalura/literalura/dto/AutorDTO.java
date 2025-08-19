package com.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para representar um autor na resposta da API Gutendx
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDTO(
        @JsonAlias("name") String nome,
        @JsonAlias("birth_year") Integer anoNascimento,
        @JsonAlias("death_year") Integer anoFalecimento
) {

    /**
     * Converte o DTO em uma string para exibição
     */
    @Override
    public String toString() {
        return String.format("%s (Nasc: %s - Morte: %s)",
                nome,
                anoNascimento != null ? anoNascimento : "N/D",
                anoFalecimento != null ? anoFalecimento : "N/D");
    }
}