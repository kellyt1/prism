package us.mn.state.health.model.util.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rodent1
 * Date: Jun 29, 2009
 * Time: 3:10:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationUpTimeServlet  extends HttpServlet {
    private static Log log = LogFactory.getLog(ApplicationUpTimeServlet.class);
    private static final String CONTENT_TYPE = "text/html; charset=utf-8";
    private static ApplicationContext applicationContext = null;
    private static String StartUpTime;

    /**
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {

    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Display PRISM Up-Time</title></head>");
        out.println("<body>");
        out.println("<p>PRISM Startup and Shutdown times:</p>");

        try {
            String envDir = System.getProperty("PRISM_DATA") == null ? System.getenv("PRISM_DATA") : System.getProperty("PRISM_DATA");
            FileReader fstream = new FileReader(envDir+"/" + "PRISM.log");
            BufferedReader in = new BufferedReader(fstream);
            String line = null;
            while (( line = in.readLine()) != null) {
                out.println(line);
                out.println("<br>");
            }
            in.close();
        }catch (Exception e){//Catch exception if any
            log.error("   PRISM.log file exception: " + new Date());
            log.error("Error: " + e.getMessage());
        }



        out.println("</body></html>");
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

}
