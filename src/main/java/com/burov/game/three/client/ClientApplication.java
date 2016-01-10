package com.burov.game.three.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        SpringApplication clientApplication = new SpringApplication(ClientApplication.class);
        clientApplication.setWebEnvironment(false);
        clientApplication.run(args);
    }
}
