package uk.co.grahamcox.worldbuilder.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Controller for handling debug information
 */
@Controller
@RequestMapping("/api/debug")
public class DebugController {
    /** The clock to use */
    private final Clock clock;

    /** The version information to export */
    private final Properties versionInfo;

    /**
     * Construct the controller
     * @param clock the clock to use
     * @param versionInfo the version information
     */
    public DebugController(final Clock clock, final Properties versionInfo) {
        this.clock = clock;
        this.versionInfo = versionInfo;
    }

    /**
     * Get the current date and time
     * @return the current date and time
     */
    @RequestMapping("/now")
    @ResponseBody
    public String now() {
        return ZonedDateTime.now(clock).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Get the version information
     * @return the version information
     */
    @RequestMapping("/version")
    @ResponseBody
    public Properties version() {
        return versionInfo;
    }
}
