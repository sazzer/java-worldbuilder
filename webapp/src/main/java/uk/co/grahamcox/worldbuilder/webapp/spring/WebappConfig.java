package uk.co.grahamcox.worldbuilder.webapp.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Root of the webapp configuration
 */
@Configuration
@Import(value = {
    WebMvcConfig.class,
    ControllersConfig.class
})
public class WebappConfig {
}
