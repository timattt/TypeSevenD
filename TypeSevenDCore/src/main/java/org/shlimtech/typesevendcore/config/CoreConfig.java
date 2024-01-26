package org.shlimtech.typesevendcore.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    @Qualifier("task_counter")
    public Counter taskCounter(MeterRegistry meterRegistry) {
        return meterRegistry.counter("type7dTaskCounter");
    }

}
