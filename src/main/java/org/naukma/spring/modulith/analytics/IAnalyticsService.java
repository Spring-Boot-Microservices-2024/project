package org.naukma.spring.modulith.analytics;

import java.time.LocalDate;
import java.util.List;

public interface IAnalyticsService {
    AnalyticsDto getToday();
    List<AnalyticsDto> getAll();
    AnalyticsDto getByDate(LocalDate date);
}
