package uk.co.grahamcox.worldbuilder.webapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.co.grahamcox.worldbuilder.spring.CoreConfig;
import uk.co.grahamcox.worldbuilder.webapp.spring.WebappConfig;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Integration Test for the Debug Controller
 */
@ContextConfiguration(classes = {
    DebugControllerIntegrationTest.Context.class
})
public class DebugControllerIntegrationTest extends IntegrationTestBase {
    /**
     * Spring Context to use for the Debug Controller tests
     */
    public static class Context extends TestContext {
        /** The current time to use */
        public static final ZonedDateTime NOW = ZonedDateTime.of(2015, 7, 11, 22, 20, 0, 0, ZoneId.of("UTC"));

        /**
         * Create the clock to use
         * @return the clock to use
         */
        @Bean(name = "clock")
        public Clock getClock() {
            return Clock.fixed(NOW.toInstant(), ZoneId.of("UTC"));
        }
    }

    /**
     * Test getting the current server time
     * @throws Exception never
     */
    @Test
    public void testNow() throws Exception {
        request(MockMvcRequestBuilders.get("/api/debug/now"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(MockMvcResultMatchers.content().string("2015-07-11T22:20:00Z[UTC]"));
    }

    /**
     * Test getting the version information
     * @throws Exception never
     */
    @Test
    public void testVersion() throws Exception {
        request(MockMvcRequestBuilders.get("/api/debug/version"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.['project.name']").value("Worldbuilder - Webapp"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.['project.version']").value("1.0.0-SNAPSHOT"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.['build.time']").value("11 July 2015, 23:06:27 +01:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.['git.revision']").value("90c60f214ba8215e03c312abae24015148015036"));
    }
}
