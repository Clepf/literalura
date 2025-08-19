package com.literalura.literalura;

import com.literalura.literalura.menu.MenuPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    // Injeta a classe que contém o menu
    @Autowired
    private MenuPrincipal menuPrincipal;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Chama o metodo que exibe o menu quando a aplicação iniciar
        menuPrincipal.exibirMenu();
    }
}