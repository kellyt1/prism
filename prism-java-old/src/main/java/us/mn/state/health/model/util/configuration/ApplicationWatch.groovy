package us.mn.state.health.model.util.configuration

import groovy.util.logging.Log4j;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

@Log4j
public class ApplicationWatch implements ServletContextListener	{
	/* Application Startup Event */
	public void	contextInitialized(ServletContextEvent ce) {
        log.info("PRISM Starting Up")
    }

	/* Application Shutdown	Event */
	public void	contextDestroyed(ServletContextEvent ce) {
        log.info("PRISM Shutting Down")
    }
}