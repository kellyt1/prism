package us.mn.state.health.util

import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

/**
 * Created with IntelliJ IDEA
 * User: rauscd1
 * Date: 10/7/2015
 * Time: 9:59 AM
 */

@WebListener
class VersionProvider implements ServletContextListener{
    static String version

    @Override
    public final void contextInitialized(final ServletContextEvent sce) {
        version = version()
    }

    @Override
    public final void contextDestroyed(final ServletContextEvent sce) {

    }

    public String version() {
        String path = "/version.prop";
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) return "UNKNOWN";
        Properties props = new Properties();
        try {
            props.load(stream);
            stream.close();
            return (String)props.get("version");
        } catch (IOException e) {
            return "UNKNOWN";
        }
    }
}
