package uk.co.grahamcox.worldbuilder.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import uk.co.grahamcox.worldbuilder.webapp.spring.WebappConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Application Initializer
 */
public class AppInitializer implements WebApplicationInitializer {
    /** The logger to use */
    private static final Logger LOG = LoggerFactory.getLogger(AppInitializer.class);

    /**
     * Handler the startup of the application
     * @param servletContext the servlet context to configure
     * @throws ServletException if any error occurs
     */
    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        LOG.info("Configuring the application");
        // Create ApplicationContext. I'm using the
        // AnnotationConfigWebApplicationContext to avoid using beans xml files.
        AnnotationConfigWebApplicationContext ctx =
            new AnnotationConfigWebApplicationContext();
        ctx.register(WebappConfig.class);

        // Add the servlet mapping manually and make it initialize automatically
        ServletRegistration.Dynamic servlet =
            servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }
}
