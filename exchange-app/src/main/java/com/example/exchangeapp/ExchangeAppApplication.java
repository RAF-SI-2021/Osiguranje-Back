package com.example.exchangeapp;

import com.example.exchangeapp.config.ApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExchangeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeAppApplication.class, args);
    }

    @Bean
    public ApiConfig apiConfig() {
        return new ApiConfig();
    }
}
