package org.naukma.spring.modulith.analytics;

import lombok.RequiredArgsConstructor;
import org.naukma.spring.modulith.authentication.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final WebClient webClient;
    private final Duration timeout = Duration.ofSeconds(1);
    private final AuthenticationService authenticationService;

    public void reportEvent(AnalyticsEventType event) {
        webClient.post().uri("/analytics")
                .header(authenticationService.getAuthHeader(), authenticationService.getAuthKey())
                .bodyValue(new AnalyticsEvent(event))
                .retrieve()
                .bodyToMono(Void.class)
                .block(timeout);
    }
}
