package com.literalura.literalura.model;

import jakarta.persistence.*;

/**
 * Entidade Livro - representa um livro do catálogo
 * Relacionamento: Cada livro pertence a um autor (ManyToOne)
 */
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    private Long id; // Usando o ID da API Gutendx como chave primária

    @Column(nullable = false, length = 500)
    private String titulo;

    @Column(length = 10)
    private String idioma;

    @Column(name = "numero_downloads")
    private Integer numeroDownloads;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    // Construtores
    public Livro() {}

    public Livro(Long id, String titulo, String idioma, Integer numeroDownloads, Autor autor) {
        this.id = id;
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
        this.autor = autor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Integer numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    /**
     * Retorna o nome do idioma por extenso baseado no código ISO
     * @return Nome do idioma
     */
    public String getNomeIdioma() {
        return switch (idioma != null ? idioma.toLowerCase() : "") {
            case "pt" -> "Português";
            case "en" -> "Inglês";
            case "es" -> "Espanhol";
            case "fr" -> "Francês";
            case "de" -> "Alemão";
            case "it" -> "Italiano";
            case "la" -> "Latim";
            case "fi" -> "Finlandês";
            case "nl" -> "Holandês";
            case "ru" -> "Russo";
            default -> idioma != null ? idioma.toUpperCase() : "Desconhecido";
        };
    }

    @Override
    public String toString() {
        return String.format("%-50s | %-30s | %-10s | %,8d downloads",
                titulo.length() > 50 ? titulo.substring(0, 47) + "..." : titulo,
                autor != null ? (autor.getNome().length() > 30 ?
                        autor.getNome().substring(0, 27) + "..." : autor.getNome()) : "Autor desconhecido",
                getNomeIdioma(),
                numeroDownloads != null ? numeroDownloads : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livro livro = (Livro) obj;
        return id != null ? id.equals(livro.id) : livro.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}