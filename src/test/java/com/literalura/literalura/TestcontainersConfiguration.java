package com.literalura.literalura;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    // Inicia o container antes do ApplicationContext e expõe as props
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
                    .withDatabaseName("literalura-test")
                    .withUsername("sa")
                    .withPassword("");

    static {
        POSTGRES.start();
        System.setProperty("DB_URL", POSTGRES.getJdbcUrl());
        System.setProperty("DB_USERNAME", POSTGRES.getUsername());
        System.setProperty("DB_PASSWORD", POSTGRES.getPassword());
    }

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        return POSTGRES;
    }
}