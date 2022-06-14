package com.cht;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
		builder.headless(false).run(args);
	}

}
