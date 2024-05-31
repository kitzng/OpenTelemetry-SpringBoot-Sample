package com.kitzing.opentelemetry.springboot.example.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.instrumentation.api.semconv.http.HttpServerMetrics;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;


/**
 * This is a programmatic configuration of a custom request tracker
 * (can also be used differently ofc)
 */
@Component
public class ControllerOperationListener extends OncePerRequestFilter {

    private final MeterRegistry meterRegistry;
    private final Timer requestDurationTimer;

    public ControllerOperationListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.requestDurationTimer = Timer.builder("custom.http.request.duration")
                .description("Duration of HTTP requests tracked by custom filter")
                .publishPercentileHistogram(true) // Enable percentile histograms
                .publishPercentiles(0.05, 0.1, 0.5, 0.75, 0.9, 0.95, 0.99) // Configure specific percentiles
                .sla(Duration.ofMillis(50), Duration.ofMillis(100), Duration.ofMillis(200),
                        Duration.ofMillis(300), Duration.ofMillis(400), Duration.ofMillis(500),
                        Duration.ofMillis(1000), Duration.ofMillis(1500), Duration.ofMillis(2000),
                        Duration.ofMillis(3000), Duration.ofMillis(4000), Duration.ofMillis(5000),
                        Duration.ofMillis(7500), Duration.ofMillis(10000), Duration.ofMillis(15000)) // Configure specific slas
                .minimumExpectedValue(Duration.ofMillis(5))
                .maximumExpectedValue(Duration.ofMinutes(5))
                .register(meterRegistry);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Instant start = Instant.now();
        try {
            filterChain.doFilter(request, response);
        } finally {
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toMillis();
            requestDurationTimer.record(duration, TimeUnit.MILLISECONDS);
        }
    }
}
