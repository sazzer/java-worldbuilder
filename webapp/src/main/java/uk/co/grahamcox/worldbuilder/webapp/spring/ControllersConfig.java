package uk.co.grahamcox.worldbuilder.webapp.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.grahamcox.worldbuilder.webapp.DebugController;

import java.time.Clock;

/**
 * Configuration of controllers
 */
@Configuration
public class ControllersConfig {
    /** The clock to use */
    @Autowired
    private Clock clock;

    /**
     * Construct the debug controller
     * @return the debug controller
     */
    @Bean
    public DebugController debugController() {
        return new DebugController(clock);
    }
}
