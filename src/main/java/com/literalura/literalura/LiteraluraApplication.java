package com.literalura.literalura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação LiterAlura
 *
 * Esta aplicação implementa o Challenge LiterAlura da Alura + Oracle ONE
 *
 * Funcionalidades principais:
 * - Busca de livros na API Gutendx
 * - Armazenamento de livros e autores em PostgreSQL
 * - Consultas por título, autor, idioma
 * - Listagem de autores vivos em determinado ano
 * - Interface via console interativo
 *
 * Tecnologias utilizadas:
 * - Java 17+
 * - Spring Boot 3.2.3
 * - Spring Data JPA
 * - PostgreSQL
 * - Jackson (para JSON)
 * - HttpClient (para requisições HTTP)
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class LiteraluraApplication {

    /**
     * Método principal que inicia a aplicação Spring Boot
     *
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        // Configura a codificação de caracteres para UTF-8
        System.setProperty("file.encoding", "UTF-8");

        // Banner customizado (opcional)
        System.setProperty("spring.banner.location", "classpath:banner.txt");

        try {
            // Inicia a aplicação Spring Boot
            SpringApplication.run(LiteraluraApplication.class, args);
        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar a aplicação: " + e.getMessage());
            System.err.println("\\n🔧 Verifique:");
            System.err.println("   • Se o PostgreSQL está rodando");
            System.err.println("   • Se as variáveis de ambiente estão configuradas");
            System.err.println("   • Se as dependências estão instaladas (mvn clean install)");
            System.err.println("   • Se a versão do Java é 17 ou superior");

            System.exit(1);
        }
    }
}