package uk.co.grahamcox.worldbuilder.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for handling debug information
 */
@Controller
@RequestMapping("/api/debug")
public class DebugController {
    /** The clock to use */
    private final Clock clock;

    /**
     * Construct the controller
     * @param clock the clock to use
     */
    public DebugController(final Clock clock) {
        this.clock = clock;
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
}
