package org.naukma.spring.modulith.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final JmsTemplate jmsTemplate;

    public void reportEvent(AnalyticsEventType event) {
        jmsTemplate.convertAndSend("analytics", new AnalyticsEvent(event));
    }
}
