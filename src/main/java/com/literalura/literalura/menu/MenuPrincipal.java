package com.literalura.literalura.menu;

import com.literalura.literalura.service.LiteraluraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Menu principal da aplicação LiterAlura
 * Implementa CommandLineRunner para execução via console
 */
@Component
public class MenuPrincipal implements CommandLineRunner {

    @Autowired
    private LiteraluraService literaluraService;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        exibirBoasVindas();

        while (true) {
            try {
                exibirMenu();
                String opcao = lerOpcao();

                if ("0".equals(opcao)) {
                    exibirDespedida();
                    break;
                }

                processarOpcao(opcao);

            } catch (Exception e) {
                System.err.println("❌ Erro inesperado: " + e.getMessage());
                System.out.println("\n⏳ Pressione ENTER para continuar...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Exibe a tela de boas-vindas
     */
    private void exibirBoasVindas() {
        limparTela();
        System.out.println("""
                ╔══════════════════════════════════════════════════════════════╗
                ║                         📚 LITERALURA 📚                    ║
                ║                                                              ║
                ║              Catálogo Digital de Livros Clássicos            ║
                ║                     Challenge Alura + ONE                    ║
                ║                                                              ║
                ║                    Bem-vindo ao LiterAlura!                  ║
                ║         Explore mais de 70.000 livros do Project Gutenberg   ║
                ╚══════════════════════════════════════════════════════════════╝
                """);

        System.out.println("⏳ Carregando aplicação...");
        try {
            Thread.sleep(2000); // Pausa dramática
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Exibe o menu principal
     */
    public void exibirMenu() {
        limparTela();
        System.out.println("""
                ╔══════════════════════════════════════════════════════════════╗
                ║                         📚 LITERALURA 📚                     ║
                ╠══════════════════════════════════════════════════════════════╣
                ║                                                              ║
                ║  🔍 1 - Buscar livro por título                              ║
                ║  📚 2 - Listar todos os livros cadastrados                   ║
                ║  👤 3 - Listar todos os autores cadastrados                  ║
                ║  📅 4 - Listar autores vivos em determinado ano              ║
                ║  🌍 5 - Listar livros por idioma                             ║
                ║  📊 6 - Exibir estatísticas do catálogo                      ║
                ║  ❌ 0 - Sair                                                 ║
                ║                                                              ║
                ╚══════════════════════════════════════════════════════════════╝
                """);
    }

    /**
     * Lê a opção escolhida pelo usuário
     */
    private String lerOpcao() {
        System.out.print("🎯 Digite sua opção: ");
        String opcao = scanner.nextLine().trim();
        System.out.println(); // Linha em branco para separação
        return opcao;
    }

    /**
     * Processa a opção escolhida pelo usuário
     */
    private void processarOpcao(String opcao) {
        switch (opcao) {
            case "1" -> {
                System.out.println("🔍 BUSCAR LIVRO POR TÍTULO");
                System.out.println("=" .repeat(60));
                literaluraService.buscarLivrosPorTitulo();
            }
            case "2" -> {
                System.out.println("📚 LISTAR TODOS OS LIVROS");
                System.out.println("=" .repeat(60));
                literaluraService.listarTodosOsLivros();
            }
            case "3" -> {
                System.out.println("👤 LISTAR TODOS OS AUTORES");
                System.out.println("=" .repeat(60));
                literaluraService.listarTodosOsAutores();
            }
            case "4" -> {
                System.out.println("📅 LISTAR AUTORES VIVOS EM DETERMINADO ANO");
                System.out.println("=" .repeat(60));
                literaluraService.listarAutoresVivosEmAno();
            }
            case "5" -> {
                System.out.println("🌍 LISTAR LIVROS POR IDIOMA");
                System.out.println("=" .repeat(60));
                literaluraService.listarLivrosPorIdioma();
            }
            case "6" -> {
                System.out.println("📊 ESTATÍSTICAS DO CATÁLOGO");
                System.out.println("=" .repeat(60));
                literaluraService.exibirEstatisticas();
            }
            case "clear", "cls" -> {
                limparTela();
                return; // Não mostra "pressione ENTER"
            }
            default -> {
                System.out.println("❌ Opção inválida! Digite um número de 0 a 6.");
            }
        }

        pausar();
    }

    /**
     * Exibe mensagem de despedida
     */
    private void exibirDespedida() {
        limparTela();
        System.out.println("""
                ╔══════════════════════════════════════════════════════════════╗
                ║                    👋 ATÉ LOGO! 👋                          ║
                ║                                                              ║
                ║            Obrigado por usar o LiterAlura!                   ║
                ║                                                              ║
                ║        📚 Continue explorando o mundo da literatura! 📚      ║
                ║                                                              ║
                ║              Developed with ❤️ for Alura + ONE               ║
                ╚══════════════════════════════════════════════════════════════╝
                """);
    }

    /**
     * Pausa a execução até o usuário pressionar ENTER
     */
    private void pausar() {
        System.out.println("\n⏳ Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Limpa a tela do console (funciona na maioria dos terminais)
     */
    private void limparTela() {
        try {
            // Tenta limpar usando códigos ANSI (funciona na maioria dos terminais modernos)
            System.out.print("\033[2J\033[H");
            System.out.flush();
        } catch (Exception e) {
            // Se não funcionar, adiciona várias linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}