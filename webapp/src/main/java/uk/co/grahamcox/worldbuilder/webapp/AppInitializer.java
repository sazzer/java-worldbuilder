package uk.co.grahamcox.worldbuilder.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import uk.co.grahamcox.worldbuilder.spring.CoreConfig;
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
        AnnotationConfigWebApplicationContext coreContext = new AnnotationConfigWebApplicationContext();
        coreContext.register(CoreConfig.class);
        coreContext.refresh();

        AnnotationConfigWebApplicationContext webappContext = new AnnotationConfigWebApplicationContext();
        webappContext.setParent(coreContext);
        webappContext.register(WebappConfig.class);

        // Add the servlet mapping manually and make it initialize automatically
        ServletRegistration.Dynamic servlet =
            servletContext.addServlet("dispatcher", new DispatcherServlet(webappContext));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }
}
