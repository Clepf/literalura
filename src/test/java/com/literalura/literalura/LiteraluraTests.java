package com.literalura.literalura;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import com.literalura.literalura.model.Autor;
import com.literalura.literalura.model.Livro;
import com.literalura.literalura.service.ConversaoService;
import com.literalura.literalura.dto.AutorDTO;
import com.literalura.literalura.dto.LivroDTO;
import com.literalura.literalura.dto.GutendexResponseDTO;

/**
 * Testes unitários para o projeto LiterAlura
 */
@SpringBootTest
@ActiveProfiles("test")
public class LiteraluraTests {

    private ConversaoService conversaoService;
    private Autor autorTeste;
    private Livro livroTeste;

    @BeforeEach
    void setUp() {
        conversaoService = new ConversaoService();

        autorTeste = new Autor();
        autorTeste.setNome("Machado de Assis");
        autorTeste.setAnoNascimento(1839);
        autorTeste.setAnoFalecimento(1908);

        livroTeste = new Livro();
        livroTeste.setId(55752L);
        livroTeste.setTitulo("Dom Casmurro");
        livroTeste.setIdioma("pt");
        livroTeste.setNumeroDownloads(1234);
        livroTeste.setAutor(autorTeste);
    }

    @Test
    @DisplayName("Deve criar autor com dados válidos")
    void testCriarAutorValido() {
        Autor autor = new Autor("Jorge Amado", 1912, 2001);

        assertEquals("Jorge Amado", autor.getNome());
        assertEquals(1912, autor.getAnoNascimento());
        assertEquals(2001, autor.getAnoFalecimento());
        assertTrue(autor.getLivros().isEmpty());
    }

    @Test
    @DisplayName("Deve verificar se autor estava vivo em determinado ano")
    void testAutorVivoEm() {
        // Machado de Assis (1839-1908)
        assertTrue(autorTeste.estaVivo(1850)); // Estava vivo
        assertTrue(autorTeste.estaVivo(1900)); // Estava vivo
        assertFalse(autorTeste.estaVivo(1820)); // Não havia nascido
        assertFalse(autorTeste.estaVivo(1950)); // Já havia morrido
    }

    @Test
    @DisplayName("Deve calcular idade do autor corretamente")
    void testIdadeAutor() {
        assertEquals(61, autorTeste.getIdadeEm(1900)); // 1900 - 1839
        assertEquals(0, autorTeste.getIdadeEm(1839)); // Ano de nascimento
        assertNull(autorTeste.getIdadeEm(1820)); // Antes do nascimento
    }

    @Test
    @DisplayName("Deve adicionar livro ao autor")
    void testAdicionarLivroAoAutor() {
        autorTeste.adicionarLivro(livroTeste);

        assertEquals(1, autorTeste.getLivros().size());
        assertTrue(autorTeste.getLivros().contains(livroTeste));
        assertEquals(autorTeste, livroTeste.getAutor());
    }

    @Test
    @DisplayName("Deve remover livro do autor")
    void testRemoverLivroDoAutor() {
        autorTeste.adicionarLivro(livroTeste);
        autorTeste.removerLivro(livroTeste);

        assertTrue(autorTeste.getLivros().isEmpty());
        assertNull(livroTeste.getAutor());
    }

    @Test
    @DisplayName("Deve retornar nome do idioma correto")
    void testNomeIdioma() {
        livroTeste.setIdioma("pt");
        assertEquals("Português", livroTeste.getNomeIdioma());

        livroTeste.setIdioma("en");
        assertEquals("Inglês", livroTeste.getNomeIdioma());

        livroTeste.setIdioma("xyz");
        assertEquals("XYZ", livroTeste.getNomeIdioma());
    }

    @Test
    @DisplayName("Deve converter JSON para DTO corretamente")
    void testConversaoJSON() {
        String jsonAutor = """
            {
                "name": "William Shakespeare",
                "birth_year": 1564,
                "death_year": 1616
            }
            """;

        AutorDTO autor = conversaoService.jsonParaObjeto(jsonAutor, AutorDTO.class);

        assertNotNull(autor);
        assertEquals("William Shakespeare", autor.nome());
        assertEquals(1564, autor.anoNascimento());
        assertEquals(1616, autor.anoFalecimento());
    }

    @Test
    @DisplayName("Deve processar resposta da API Gutendx")
    void testGutendxResponse() {
        String jsonResponse = """
            {
                "count": 1,
                "next": null,
                "previous": null,
                "results": [
                    {
                        "id": 55752,
                        "title": "Dom Casmurro",
                        "authors": [
                            {
                                "name": "Machado de Assis",
                                "birth_year": 1839,
                                "death_year": 1908
                            }
                        ],
                        "languages": ["pt"],
                        "download_count": 1234
                    }
                ]
            }
            """;

        GutendexResponseDTO response = conversaoService.jsonParaObjeto(jsonResponse, GutendexResponseDTO.class);

        assertNotNull(response);
        assertEquals(1, response.total());
        assertTrue(response.temResultados());

        LivroDTO livro = response.getPrimeiroLivro();
        assertNotNull(livro);
        assertEquals(55752L, livro.id());
        assertEquals("Dom Casmurro", livro.titulo());
        assertEquals("pt", livro.getPrimeiroIdioma());
        assertEquals(1234, livro.numeroDownloads());

        AutorDTO autor = livro.getPrimeiroAutor();
        assertNotNull(autor);
        assertEquals("Machado de Assis", autor.nome());
    }

    @Test
    @DisplayName("Deve validar dados obrigatórios do livro")
    void testValidacaoLivro() {
        Livro livro = new Livro();

        // Teste com ID nulo - deve falhar
        assertThrows(Exception.class, () -> {
            livro.setId(null);
            // Validação seria executada pelo Bean Validation
        });
    }

    @Test
    @DisplayName("Deve tratar anos de vida inválidos")
    void testValidacaoAnosVida() {
        // Ano de nascimento posterior ao falecimento deve gerar erro
        assertThrows(IllegalArgumentException.class, () -> {
            new Autor("Autor Inválido", 2000, 1990);
        });
    }

    @Test
    @DisplayName("Deve formatar toString dos objetos corretamente")
    void testToString() {
        String autorString = autorTeste.toString();

        assertTrue(autorString.contains("Machado de Assis"));
        assertTrue(autorString.contains("1839"));
        assertTrue(autorString.contains("1908"));

        String livroString = livroTeste.toString();
        assertTrue(livroString.contains("Dom Casmurro"));
        assertTrue(livroString.contains("Português"));
        assertTrue(livroString.contains("1,234")); // Formatação de números
    }

    @Test
    @DisplayName("Deve testar equals e hashCode")
    void testEqualsHashCode() {
        Autor autor1 = new Autor("Jorge Amado", 1912, 2001);
        Autor autor2 = new Autor("Jorge Amado", 1912, 2001);
        Autor autor3 = new Autor("Clarice Lispector", 1920, 1977);

        assertEquals(autor1, autor2);
        assertEquals(autor1.hashCode(), autor2.hashCode());
        assertNotEquals(autor1, autor3);
    }

    @Test
    @DisplayName("Teste de integração básica")
    void testIntegracaoBasica() {
        // Simula fluxo completo: criar autor, adicionar livro, verificar relacionamento
        Autor autor = new Autor("Clarice Lispector", 1920, 1977);
        Livro livro = new Livro();
        livro.setId(12345L);
        livro.setTitulo("A Hora da Estrela");
        livro.setIdioma("pt");
        livro.setNumeroDownloads(5678);

        autor.adicionarLivro(livro);

        // Verificações
        assertEquals(1, autor.getLivros().size());
        assertEquals(autor, livro.getAutor());
        assertEquals("Clarice Lispector", livro.getAutor().getNome());
        assertTrue(autor.estaVivo(1950));
        assertFalse(autor.estaVivo(2000));
    }
}