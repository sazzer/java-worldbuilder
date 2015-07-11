package uk.co.grahamcox.worldbuilder.webapp;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.co.grahamcox.worldbuilder.spring.CoreConfig;
import uk.co.grahamcox.worldbuilder.webapp.spring.WebappConfig;

/**
 * Created by graham on 11/07/15.
 */
@WebAppConfiguration
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    IntegrationTestBase.TestContext.class
})
public class IntegrationTestBase {
    /**
     * Spring Context to use for the Debug Controller tests
     */
    @Configuration
    @Import({
        CoreConfig.class,
        WebappConfig.class})
    public static class TestContext {

    }

    /** The Web Application Context to use */
    @Autowired
    private WebApplicationContext webApplicationContext;

    /** The Mock MVC to use */
    private MockMvc mockMvc;

    /**
     * Set up the Mock MVC
     */
    @Before
    public final void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();
    }

    /**
     * Perform the given request
     * @param request the request
     * @return the response
     * @throws Exception if an error occurred
     */
    public ResultActions request(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }
}
