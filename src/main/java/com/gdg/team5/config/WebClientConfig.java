package com.gdg.team5.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${python.crawling.base-url}")
    private String pythonServerBaseUrl;

    @Bean
    public WebClient pythonWebClient() {
        return WebClient.builder()
            .baseUrl(pythonServerBaseUrl)
            .build();
    }

}
