package com.gdg.team5.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class webClientConfig {
    @Value("${python.crawling.base-url}")
    private String pythonBaseUrl;

}
