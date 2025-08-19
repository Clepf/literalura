package com.literalura.literalura.service;

import com.literalura.literalura.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Serviço melhorado para requisições HTTP com tratamento robusto de erros
 */
@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private final HttpClient httpClient;
    private static final String BASE_URL = "https://gutendex.com/books/";
    private static final Duration TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_RETRIES = 3;

    public ApiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        logger.info("ApiService inicializado com timeout de {}s", TIMEOUT.getSeconds());
    }

    /**
     * Faz uma requisição GET com retry automático
     */
    public String fazerRequisicao(String url) {
        logger.debug("Iniciando requisição para: {}", url);

        for (int tentativa = 1; tentativa <= MAX_RETRIES; tentativa++) {
            try {
                return executarRequisicao(url, tentativa);
            } catch (ApiException e) {
                if (tentativa == MAX_RETRIES || e.getStatusCode() < 500) {
                    throw e;
                }
                logger.warn("Tentativa {} falhou, tentando novamente em 2s: {}",
                        tentativa, e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ApiException("Requisição interrompida", 0, ie);
                }
            }
        }

        throw new ApiException("Máximo de tentativas excedido", 0);
    }

    private String executarRequisicao(String url, int tentativa) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(TIMEOUT)
                    .header("Accept", "application/json")
                    .header("User-Agent", "LiterAlura/2.0 (Challenge Alura)")
                    .header("Accept-Encoding", "gzip, deflate")
                    .GET()
                    .build();

            long startTime = System.currentTimeMillis();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            long duration = System.currentTimeMillis() - startTime;
            logger.debug("Requisição concluída em {}ms (tentativa {})", duration, tentativa);

            return processarResposta(response, url);

        } catch (IOException e) {
            String mensagem = String.format("Erro de conexão na tentativa %d: %s", tentativa, e.getMessage());
            logger.error(mensagem, e);
            throw new ApiException(mensagem, 0, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Requisição interrompida", 0, e);
        } catch (Exception e) {
            logger.error("Erro inesperado na requisição: {}", e.getMessage(), e);
            throw new ApiException("Erro inesperado: " + e.getMessage(), 0, e);
        }
    }

    private String processarResposta(HttpResponse<String> response, String url) {
        int statusCode = response.statusCode();
        String body = response.body();

        logger.debug("Resposta recebida: status={}, tamanho={}bytes",
                statusCode, body != null ? body.length() : 0);

        if (statusCode >= 200 && statusCode < 300) {
            if (body == null || body.trim().isEmpty()) {
                throw new ApiException("Resposta vazia da API", statusCode);
            }
            return body;
        }

        // Tratamento específico por código de status
        String mensagem = switch (statusCode) {
            case 400 -> "Requisição inválida - verifique os parâmetros";
            case 401 -> "Não autorizado - credenciais inválidas";
            case 403 -> "Acesso negado";
            case 404 -> "Endpoint não encontrado";
            case 429 -> "Muitas requisições - aguarde antes de tentar novamente";
            case 500 -> "Erro interno do servidor da API";
            case 502 -> "Bad Gateway - servidor temporariamente indisponível";
            case 503 -> "Serviço indisponível - tente novamente mais tarde";
            case 504 -> "Timeout do gateway - servidor demorou para responder";
            default -> "Erro HTTP " + statusCode;
        };

        logger.error("Erro na API: {} | URL: {} | Resposta: {}", mensagem, url, body);
        throw new ApiException(mensagem, statusCode);
    }

    /**
     * Busca livros por título com validação de entrada
     */
    public String buscarLivrosPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidationException("Título não pode estar vazio");
        }

        if (titulo.length() > 500) {
            throw new ValidationException("Título muito longo (máximo 500 caracteres)");
        }

        String tituloLimpo = titulo.trim();
        String tituloEncoded = URLEncoder.encode(tituloLimpo, StandardCharsets.UTF_8);
        String url = BASE_URL + "?search=" + tituloEncoded;

        logger.info("Buscando livros por título: '{}'", tituloLimpo);
        return fazerRequisicao(url);
    }

    /**
     * Busca livros por autor com validação
     */
    public String buscarLivrosPorAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new ValidationException("Nome do autor não pode estar vazio");
        }

        String autorLimpo = autor.trim();
        String autorEncoded = URLEncoder.encode(autorLimpo, StandardCharsets.UTF_8);
        String url = BASE_URL + "?search=" + autorEncoded;

        logger.info("Buscando livros por autor: '{}'", autorLimpo);
        return fazerRequisicao(url);
    }

    /**
     * Busca livros por idioma com validação de código ISO
     */
    public String buscarLivrosPorIdioma(String idioma) {
        if (idioma == null || idioma.trim().isEmpty()) {
            throw new ValidationException("Código do idioma não pode estar vazio");
        }

        String idiomaLimpo = idioma.trim().toLowerCase();

        // Validação básica de código de idioma
        if (!idiomaLimpo.matches("^[a-z]{2,3}$")) {
            throw new ValidationException("Código de idioma inválido. Use códigos como 'pt', 'en', 'es'");
        }

        String url = BASE_URL + "?languages=" + idiomaLimpo;

        logger.info("Buscando livros por idioma: '{}'", idiomaLimpo);
        return fazerRequisicao(url);
    }

    /**
     * Testa conectividade com retry e timeout reduzido
     */
    public boolean testarConectividade() {
        logger.info("Testando conectividade com a API Gutendx...");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .timeout(Duration.ofSeconds(5))
                    .header("User-Agent", "LiterAlura/2.0 (Teste Conectividade)")
                    .GET()
                    .build();

            // Ajuste aqui para HttpResponse<Void>
            HttpResponse<Void> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.discarding());

            boolean sucesso = response.statusCode() < 400;

            if (sucesso) {
                logger.info("✅ Conectividade OK - Status: {}", response.statusCode());
            } else {
                logger.warn("⚠️ API respondeu com erro - Status: {}", response.statusCode());
            }

            return sucesso;

        } catch (Exception e) {
            logger.error("❌ Falha no teste de conectividade: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Retorna estatísticas básicas da API
     */
    public void exibirEstatisticasConexao() {
        logger.info("📊 Configurações da API:");
        logger.info("   • URL Base: {}", BASE_URL);
        logger.info("   • Timeout: {}s", TIMEOUT.getSeconds());
        logger.info("   • Max Retries: {}", MAX_RETRIES);
        logger.info("   • Redirect Policy: NORMAL");
    }
}