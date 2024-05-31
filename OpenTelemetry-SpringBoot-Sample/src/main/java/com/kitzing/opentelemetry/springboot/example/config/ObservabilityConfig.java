package com.kitzing.opentelemetry.springboot.example.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ObservabilityConfig {

    @Autowired
    OpenTelemetry openTelemetry;


    @Bean
    public FilterRegistrationBean<ControllerOperationListener> loggingFilter(MeterRegistry meterRegistry) {
        OpenTelemetryAppender.install(openTelemetry);
        FilterRegistrationBean<ControllerOperationListener> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ControllerOperationListener(meterRegistry));
        registrationBean.addUrlPatterns("/*", "/^actuator");
        return registrationBean;
    }

    /**
     * Changing meters globally, but careful: it applies but seems unreliable
     */
    /*@Bean
    public SdkMeterProvider spanContextSupplier() {
        return SdkMeterProvider.builder()
                .registerView(
                        // apply the view to all instruments
                        InstrumentSelector.builder().setName("http.server.request.duration").build(),
                        // only export the attribute 'environment'
                        View.builder().setAggregation(Aggregation.explicitBucketHistogram(
                                List.of(0.05, 0.1, 0.2, 0.3, 0.4, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 8.0, 10.0, 30.0)
                        )).build())
                .build();
    }*/
}
