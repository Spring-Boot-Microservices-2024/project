package org.naukma.spring.modulith.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final WebClient webClient;

    public void reportEvent(AnalyticsEventType event) {
        webClient.post().uri("/analytics");
    }
}
