package com.literalura.literalura.service;

import com.literalura.literalura.dto.AutorDTO;
import com.literalura.literalura.dto.GutendexResponseDTO;
import com.literalura.literalura.dto.LivroDTO;
import com.literalura.literalura.model.Autor;
import com.literalura.literalura.model.Livro;
import com.literalura.literalura.repository.AutorRepository;
import com.literalura.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Serviço principal para gerenciamento de livros e autores
 */
@Service
public class LiteraluraService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ConversaoService conversaoService;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Busca um livro por título na API e oferece opção de salvar
     */
    @Transactional
    public void buscarLivrosPorTitulo() {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine().trim();

        if (titulo.isEmpty()) {
            System.out.println("❌ Título não pode estar vazio!");
            return;
        }

        try {
            System.out.println("🔍 Buscando na API Gutendx...");
            String jsonResponse = apiService.buscarLivrosPorTitulo(titulo);

            GutendexResponseDTO response = conversaoService.jsonParaObjeto(jsonResponse, GutendexResponseDTO.class);

            if (!response.temResultados()) {
                System.out.println("❌ Nenhum livro encontrado com o título: " + titulo);
                return;
            }

            LivroDTO livroDTO = response.getPrimeiroLivro();
            System.out.println("\\n📚 Livro encontrado:");
            System.out.println("=" .repeat(80));
            System.out.println(livroDTO);
            System.out.println("=" .repeat(80));

            // Verifica se já existe no banco
            if (livroRepository.existsById(livroDTO.id())) {
                System.out.println("⚠️  Este livro já está cadastrado no banco de dados!");
                return;
            }

            System.out.print("\\n💾 Deseja salvar este livro no banco de dados? (s/N): ");
            String resposta = scanner.nextLine().trim().toLowerCase();

            if ("s".equals(resposta) || "sim".equals(resposta)) {
                salvarLivroNoBanco(livroDTO);
                System.out.println("✅ Livro salvo com sucesso!");
            } else {
                System.out.println("ℹ️  Livro não foi salvo.");
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar livro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os livros cadastrados no banco
     */
    public void listarTodosOsLivros() {
        List<Livro> livros = livroRepository.findAllByOrderByTituloAsc();

        if (livros.isEmpty()) {
            System.out.println("📚 Nenhum livro cadastrado ainda.");
            return;
        }

        System.out.println("\\n📚 LIVROS CADASTRADOS (" + livros.size() + " livro(s)):");
        System.out.println("=" .repeat(120));
        System.out.printf("%-50s | %-30s | %-10s | %12s%n",
                "TÍTULO", "AUTOR", "IDIOMA", "DOWNLOADS");
        System.out.println("-" .repeat(120));

        livros.forEach(System.out::println);
        System.out.println("=" .repeat(120));
    }

    /**
     * Lista todos os autores cadastrados no banco
     */
    public void listarTodosOsAutores() {
        List<Autor> autores = autorRepository.findAllByOrderByNomeAsc();

        if (autores.isEmpty()) {
            System.out.println("👤 Nenhum autor cadastrado ainda.");
            return;
        }

        System.out.println("\\n👤 AUTORES CADASTRADOS (" + autores.size() + " autor(es)):");
        System.out.println("=" .repeat(120));
        System.out.printf("%-40s | %-6s | %-6s | %8s%n",
                "NOME", "NASC.", "MORTE", "LIVROS");
        System.out.println("-" .repeat(120));

        autores.forEach(System.out::println);
        System.out.println("=" .repeat(120));
    }

    /**
     * Lista autores vivos em um determinado ano
     */
    public void listarAutoresVivosEmAno() {
        System.out.print("Digite o ano para consulta: ");
        String anoTexto = scanner.nextLine().trim();

        try {
            int ano = Integer.parseInt(anoTexto);
            List<Autor> autoresVivos = autorRepository.findAutoresVivosNoAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("👤 Nenhum autor estava vivo em " + ano + ".");
                return;
            }

            System.out.println("\\n👤 AUTORES VIVOS EM " + ano + " (" + autoresVivos.size() + " autor(es)):");
            System.out.println("=" .repeat(120));
            System.out.printf("%-40s | %-6s | %-6s | %8s%n",
                    "NOME", "NASC.", "MORTE", "LIVROS");
            System.out.println("-" .repeat(120));

            autoresVivos.forEach(System.out::println);
            System.out.println("=" .repeat(120));

        } catch (NumberFormatException e) {
            System.out.println("❌ Ano inválido! Digite apenas números.");
        }
    }

    /**
     * Lista livros por idioma
     */
    public void listarLivrosPorIdioma() {
        // Mostra idiomas disponíveis
        List<String> idiomas = livroRepository.findIdiomasDistintos();
        if (!idiomas.isEmpty()) {
            System.out.println("\\n📋 Idiomas disponíveis: " + String.join(", ", idiomas));
        }

        System.out.println("ℹ️  Códigos comuns: pt (Português), en (Inglês), es (Espanhol), fr (Francês)");
        System.out.print("Digite o código do idioma (ex: pt, en, es): ");
        String idioma = scanner.nextLine().trim().toLowerCase();

        if (idioma.isEmpty()) {
            System.out.println("❌ Código de idioma não pode estar vazio!");
            return;
        }

        List<Livro> livros = livroRepository.findByIdiomaIgnoreCase(idioma);

        if (livros.isEmpty()) {
            System.out.println("📚 Nenhum livro encontrado no idioma: " + idioma.toUpperCase());
            return;
        }

        System.out.println("\\n📚 LIVROS EM " + idioma.toUpperCase() + " (" + livros.size() + " livro(s)):");
        System.out.println("=" .repeat(120));
        System.out.printf("%-50s | %-30s | %-10s | %12s%n",
                "TÍTULO", "AUTOR", "IDIOMA", "DOWNLOADS");
        System.out.println("-" .repeat(120));

        livros.forEach(System.out::println);
        System.out.println("=" .repeat(120));
    }

    /**
     * Exibe estatísticas do catálogo
     */
    public void exibirEstatisticas() {
        long totalLivros = livroRepository.count();
        long totalAutores = autorRepository.count();
        List<String> idiomas = livroRepository.findIdiomasDistintos();

        System.out.println("\\n📊 ESTATÍSTICAS DO CATÁLOGO:");
        System.out.println("=" .repeat(50));
        System.out.println("📚 Total de livros: " + totalLivros);
        System.out.println("👤 Total de autores: " + totalAutores);
        System.out.println("🌍 Idiomas disponíveis: " + idiomas.size());
        System.out.println("📋 Idiomas: " + String.join(", ", idiomas));

        if (totalLivros > 0) {
            List<Livro> topLivros = livroRepository.findTopLivrosMaisBaixados(3);
            System.out.println("\\n🏆 TOP 3 MAIS BAIXADOS:");
            topLivros.forEach(livro ->
                    System.out.println("   • " + livro.getTitulo() + " - " +
                            String.format("%,d", livro.getNumeroDownloads()) + " downloads"));
        }
        System.out.println("=" .repeat(50));
    }

    /**
     * Salva um livro e seu autor no banco de dados
     */
    @Transactional
    private void salvarLivroNoBanco(LivroDTO livroDTO) {
        // Buscar ou criar autor
        AutorDTO autorDTO = livroDTO.getPrimeiroAutor();
        if (autorDTO == null) {
            throw new RuntimeException("Livro deve ter pelo menos um autor");
        }

        Optional<Autor> autorExistente = autorRepository.findByNomeIgnoreCase(autorDTO.nome());
        Autor autor;

        if (autorExistente.isPresent()) {
            autor = autorExistente.get();
        } else {
            autor = new Autor(autorDTO.nome(), autorDTO.anoNascimento(), autorDTO.anoFalecimento());
            autor = autorRepository.save(autor);
        }

        // Criar e salvar livro
        Livro livro = new Livro(
                livroDTO.id(),
                livroDTO.titulo(),
                livroDTO.getPrimeiroIdioma(),
                livroDTO.numeroDownloads(),
                autor
        );

        livroRepository.save(livro);
    }
}