package com.example.exchangeapp.config;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiConfig {
    @Value("${api.stocks}")
    private String stocksApi;

    @Value("${api.forex}")
    private String forexApi;

    @Value("${api.futures}")
    private String futuresApi;
}
