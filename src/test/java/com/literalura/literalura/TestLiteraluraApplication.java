package com.literalura.literalura;

import org.springframework.boot.SpringApplication;

public class TestLiteraluraApplication {

	public static void main(String[] args) {
		SpringApplication.from(LiteraluraApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
