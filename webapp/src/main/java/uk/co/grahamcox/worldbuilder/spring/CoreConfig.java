package uk.co.grahamcox.worldbuilder.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.time.Clock;

/**
 * Core spring context containing non-webapp configuration
 */
@Configuration
@PropertySource(ignoreResourceNotFound = true, value = {
    "classpath:/application.properties"
})
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

    /**
     * Create a Property Source Configurer bean
     * @return Property Source Configurer bean
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
