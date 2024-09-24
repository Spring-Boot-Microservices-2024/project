package org.naukma.spring.modulith.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final WebClient webClient;
    private final Duration timeout = Duration.ofSeconds(1);

    public void reportEvent(AnalyticsEventType event) {
        webClient.post().uri("/analytics")
                .bodyValue(new AnalyticsEvent(event))
                .retrieve()
                .bodyToMono(Void.class)
                .block(timeout);
    }
}
