package com.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para representar um livro na resposta da API Gutendx
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LivroDTO(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<AutorDTO> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDownloads
) {

    /**
     * Retorna o primeiro autor da lista (seguindo a regra do challenge)
     */
    public AutorDTO getPrimeiroAutor() {
        return autores != null && !autores.isEmpty() ? autores.get(0) : null;
    }

    /**
     * Retorna o primeiro idioma da lista (seguindo a regra do challenge)
     */
    public String getPrimeiroIdioma() {
        return idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : null;
    }

    /**
     * Converte o DTO em uma string para exibição
     */
    @Override
    public String toString() {
        AutorDTO autor = getPrimeiroAutor();
        return String.format("ID: %d | %s | %s | %s | %,d downloads",
                id,
                titulo,
                autor != null ? autor.nome() : "Autor desconhecido",
                getPrimeiroIdioma() != null ? getPrimeiroIdioma() : "Idioma desconhecido",
                numeroDownloads != null ? numeroDownloads : 0);
    }
}