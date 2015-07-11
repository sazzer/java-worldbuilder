package uk.co.grahamcox.worldbuilder.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Core spring context containing non-webapp configuration
 */
@Configuration
public class CoreConfig {
    /**
     * Create the clock to use
     * @return the clock to use
     */
    @Bean(name = "clock")
    public Clock getClock() {
        return Clock.systemUTC();
    }
}
