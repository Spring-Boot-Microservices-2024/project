package org.naukma.spring.modulith;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebRequests {
    @Bean
    public WebClient webClient() {
        return WebClient.create("http://analytics:8081");
    }
}
