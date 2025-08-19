package com.literalura.literalura.repository;

import com.literalura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com a entidade Autor
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    /**
     * Busca um autor pelo nome exato
     * @param nome Nome do autor
     * @return Optional contendo o autor se encontrado
     */
    Optional<Autor> findByNome(String nome);

    /**
     * Busca um autor pelo nome ignorando maiúsculas/minúsculas
     * @param nome Nome do autor
     * @return Optional contendo o autor se encontrado
     */
    Optional<Autor> findByNomeIgnoreCase(String nome);

    /**
     * Busca autores cujo nome contenha o texto especificado
     * @param nome Parte do nome do autor
     * @return Lista de autores encontrados
     */
    List<Autor> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca autores que estavam vivos em um determinado ano
     * Um autor estava vivo se nasceu antes ou no ano especificado
     * E morreu depois do ano especificado ou ainda está vivo (ano_falecimento é null)
     *
     * @param ano Ano para verificar
     * @return Lista de autores vivos no ano especificado
     */
    @Query("SELECT a FROM Autor a WHERE " +
            "(a.anoNascimento IS NULL OR a.anoNascimento <= :ano) AND " +
            "(a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivosNoAno(@Param("ano") Integer ano);

    /**
     * Busca autores nascidos em um ano específico
     * @param ano Ano de nascimento
     * @return Lista de autores nascidos no ano
     */
    List<Autor> findByAnoNascimento(Integer ano);

    /**
     * Busca autores que faleceram em um ano específico
     * @param ano Ano de falecimento
     * @return Lista de autores que faleceram no ano
     */
    List<Autor> findByAnoFalecimento(Integer ano);

    /**
     * Busca autores nascidos em um período
     * @param anoInicio Ano inicial
     * @param anoFim Ano final
     * @return Lista de autores nascidos no período
     */
    List<Autor> findByAnoNascimentoBetween(Integer anoInicio, Integer anoFim);

    /**
     * Busca todos os autores ordenados por nome
     * @return Lista de autores ordenada por nome
     */
    List<Autor> findAllByOrderByNomeAsc();

    /**
     * Verifica se existe um autor com o nome especificado
     * @param nome Nome do autor
     * @return true se existe, false caso contrário
     */
    boolean existsByNome(String nome);
}