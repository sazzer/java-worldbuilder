package uk.co.grahamcox.worldbuilder.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;

/**
 * Core spring context containing non-webapp configuration
 */
@Configuration
@Import({
    MongoConfig.class
})
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
