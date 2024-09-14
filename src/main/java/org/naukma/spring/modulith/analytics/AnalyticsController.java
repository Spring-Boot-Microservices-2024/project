package org.naukma.spring.modulith.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping
    public List<AnalyticsDto> getAll() {
        return analyticsService.getAll();
    }

    @GetMapping("/today")
    public AnalyticsDto getToday() {
        return analyticsService.getToday();
    }

    @GetMapping("/{date}")
    public AnalyticsDto getByDate(@PathVariable Date date) {
        return analyticsService.getByDate(date);
    }
}
