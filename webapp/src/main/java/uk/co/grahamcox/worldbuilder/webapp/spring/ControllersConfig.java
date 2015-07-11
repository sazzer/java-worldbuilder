package uk.co.grahamcox.worldbuilder.webapp.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.grahamcox.worldbuilder.webapp.DebugController;

import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.util.Properties;

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
     * @throws IOException if there's an error loading the version information
     */
    @Bean
    public DebugController debugController() throws IOException {
        Properties versionInfo = new Properties();
        try (InputStream buildInfo = ControllersConfig.class.getResourceAsStream("/build.info")) {
            versionInfo.load(buildInfo);
        }
        return new DebugController(clock, versionInfo);
    }
}
