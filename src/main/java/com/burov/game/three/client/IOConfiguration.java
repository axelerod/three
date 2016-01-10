package com.burov.game.three.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.PrintStream;

@Configuration
class IOConfiguration {
    @Bean
    InputStream getUserInputStream() {
        return System.in;
    }

    @Bean
    PrintStream getUserPrintStream() {
        return System.out;
    }
}
