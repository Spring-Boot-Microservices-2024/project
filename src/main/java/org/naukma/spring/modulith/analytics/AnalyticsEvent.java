package org.naukma.spring.modulith.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalyticsEvent {
    private AnalyticsEventType type;
}
