package com.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para representar a resposta completa da API Gutendx
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexResponseDTO(
        @JsonAlias("count") Integer total,
        @JsonAlias("next") String proximaPagina,
        @JsonAlias("previous") String paginaAnterior,
        @JsonAlias("results") List<LivroDTO> livros
) {

    /**
     * Verifica se existem resultados
     */
    public boolean temResultados() {
        return livros != null && !livros.isEmpty();
    }

    /**
     * Retorna o primeiro livro da lista (usado para busca por título)
     */
    public LivroDTO getPrimeiroLivro() {
        return temResultados() ? livros.get(0) : null;
    }

    /**
     * Converte o DTO em uma string para exibição
     */
    @Override
    public String toString() {
        return String.format("Total de livros encontrados: %d | Livros nesta página: %d",
                total != null ? total : 0,
                livros != null ? livros.size() : 0);
    }
}