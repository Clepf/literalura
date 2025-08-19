package com.literalura.literalura.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores", indexes = {
        @Index(name = "idx_autor_nome", columnList = "nome"),
        @Index(name = "idx_autor_anos", columnList = "ano_nascimento, ano_falecimento")
})
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do autor é obrigatório")
    @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nome;

    @Min(value = -3000, message = "Ano de nascimento deve ser maior que -3000")
    @Max(value = 2100, message = "Ano de nascimento deve ser menor que 2100")
    @Column(name = "ano_nascimento")
    private Integer anoNascimento;

    @Min(value = -3000, message = "Ano de falecimento deve ser maior que -3000")
    @Max(value = 2100, message = "Ano de falecimento deve ser menor que 2100")
    @Column(name = "ano_falecimento")
    private Integer anoFalecimento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Livro> livros = new ArrayList<>();

    // Construtores
    public Autor() {}

    public Autor(@NotBlank String nome, Integer anoNascimento, Integer anoFalecimento) {
        this.nome = nome;
        this.anoNascimento = anoNascimento;
        this.anoFalecimento = anoFalecimento;
        validateAnosVida();
    }

    // Método de validação customizada
    @PrePersist
    @PreUpdate
    private void validateAnosVida() {
        if (anoNascimento != null && anoFalecimento != null && anoNascimento > anoFalecimento) {
            throw new IllegalArgumentException("Ano de nascimento não pode ser posterior ao ano de falecimento");
        }
    }

    // Getters e Setters com validações
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome != null ? nome.trim() : null;
    }

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
        validateAnosVida();
    }

    public Integer getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(Integer anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
        validateAnosVida();
    }

    public List<Livro> getLivros() {
        return new ArrayList<>(livros); // Defensive copy
    }

    public void setLivros(List<Livro> livros) {
        this.livros.clear();
        if (livros != null) {
            this.livros.addAll(livros);
        }
    }

    // Métodos utilitários melhorados
    public void adicionarLivro(@NotNull Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }
        if (!livros.contains(livro)) {
            livros.add(livro);
            livro.setAutor(this);
        }
    }

    public void removerLivro(@NotNull Livro livro) {
        if (livros.remove(livro)) {
            livro.setAutor(null);
        }
    }

    /**
     * Verifica se o autor estava vivo em um determinado ano
     * @param ano Ano a ser verificado
     * @return true se estava vivo, false caso contrário
     */
    public boolean estaVivo(int ano) {
        if (ano < 1 || ano > 3000) {
            throw new IllegalArgumentException("Ano deve estar entre 1 e 3000");
        }

        boolean nasceuAntes = anoNascimento == null || anoNascimento <= ano;
        boolean naoMorreuAinda = anoFalecimento == null || anoFalecimento >= ano;
        return nasceuAntes && naoMorreuAinda;
    }

    /**
     * Retorna a idade do autor em um determinado ano
     * @param ano Ano de referência
     * @return Idade ou null se não puder calcular
     */
    public Integer getIdadeEm(int ano) {
        if (anoNascimento == null) return null;
        if (anoFalecimento != null && ano > anoFalecimento) return null;
        return Math.max(0, ano - anoNascimento);
    }

    /**
     * Verifica se o autor continua vivo (sem data de falecimento)
     */
    public boolean isVivo() {
        return anoFalecimento == null;
    }

    @Override
    public String toString() {
        return String.format("%-40s | Nasc: %-4s | Morte: %-4s | Livros: %d",
                nome,
                anoNascimento != null ? anoNascimento.toString() : "N/D",
                anoFalecimento != null ? anoFalecimento.toString() : "N/D",
                livros.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Autor autor = (Autor) obj;
        return nome != null ? nome.equalsIgnoreCase(autor.nome) : autor.nome == null;
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.toLowerCase().hashCode() : 0;
    }
}