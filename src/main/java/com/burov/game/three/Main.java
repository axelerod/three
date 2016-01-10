package com.burov.game.three;

import com.burov.game.three.client.ClientApplication;
import com.burov.game.three.server.ServerApplication;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        String mode = "client";
        if (args.length == 1) {
            mode = args[0];
        }

        switch (mode) {
            case "client":
                System.setProperty("spring.config.name", "client");
                SpringApplication clientApplication = new SpringApplication(ClientApplication.class);
                clientApplication.setWebEnvironment(false);
                clientApplication.run(args);
                break;
            case "server":
                SpringApplication.run(ServerApplication.class, args);
                break;
            default:
                System.out.println("Usage: java -jar three.jar [<mode>]");
                System.out.println("where <mode>: 'client' or 'server'");
                System.out.println("if mode is skipped, client by default is started");
                break;
        }

    }
}

