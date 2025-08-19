package com.literalura.literalura.repository;

import com.literalura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com a entidade Livro
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    /**
     * Busca um livro pelo título exato
     * @param titulo Título do livro
     * @return Optional contendo o livro se encontrado
     */
    Optional<Livro> findByTitulo(String titulo);

    /**
     * Busca livros cujo título contenha o texto especificado
     * @param titulo Parte do título
     * @return Lista de livros encontrados
     */
    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    /**
     * Busca livros por idioma
     * @param idioma Código do idioma (ex: pt, en, es)
     * @return Lista de livros no idioma especificado
     */
    List<Livro> findByIdioma(String idioma);

    /**
     * Busca livros por idioma ignorando maiúsculas/minúsculas
     * @param idioma Código do idioma
     * @return Lista de livros no idioma especificado
     */
    List<Livro> findByIdiomaIgnoreCase(String idioma);

    /**
     * Busca livros de um autor específico
     * @param autorId ID do autor
     * @return Lista de livros do autor
     */
    List<Livro> findByAutorId(Long autorId);

    /**
     * Busca livros de um autor pelo nome do autor
     * @param nomeAutor Nome do autor
     * @return Lista de livros do autor
     */
    List<Livro> findByAutorNome(String nomeAutor);

    /**
     * Busca livros de um autor pelo nome do autor (ignorando case)
     * @param nomeAutor Nome do autor
     * @return Lista de livros do autor
     */
    List<Livro> findByAutorNomeIgnoreCase(String nomeAutor);

    /**
     * Busca todos os livros ordenados por número de downloads (decrescente)
     * @return Lista de livros ordenada por downloads
     */
    List<Livro> findAllByOrderByNumeroDownloadsDesc();

    /**
     * Busca todos os livros ordenados por título
     * @return Lista de livros ordenada por título
     */
    List<Livro> findAllByOrderByTituloAsc();

    /**
     * Busca os livros mais baixados (top N)
     * @param limite Número máximo de livros a retornar
     * @return Lista dos livros mais baixados
     */
    @Query("SELECT l FROM Livro l ORDER BY l.numeroDownloads DESC LIMIT :limite")
    List<Livro> findTopLivrosMaisBaixados(@Param("limite") int limite);

    /**
     * Conta quantos livros existem por idioma
     * @param idioma Código do idioma
     * @return Número de livros no idioma
     */
    long countByIdioma(String idioma);

    /**
     * Busca livros com mais de X downloads
     * @param minimoDownloads Número mínimo de downloads
     * @return Lista de livros populares
     */
    List<Livro> findByNumeroDownloadsGreaterThan(Integer minimoDownloads);

    /**
     * Verifica se existe um livro com o ID especificado
     * @param id ID do livro
     * @return true se existe, false caso contrário
     */
    boolean existsById(Long id);

    /**
     * Busca todos os idiomas distintos no catálogo
     * @return Lista de códigos de idiomas únicos
     */
    @Query("SELECT DISTINCT l.idioma FROM Livro l WHERE l.idioma IS NOT NULL ORDER BY l.idioma")
    List<String> findIdiomasDistintos();
}