package us.mn.state.health.model.util.configuration;

import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Create the xml file for the rules engine rules4j so that is has the correct filesystem
 * directory paths for the current deployment as specified in the tomcat startup.sh script
 */
public class BuildRulesXMLFileServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=utf-8";
    private static ApplicationContext applicationContext = null;

    /**
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        BuildRulesXMLFile  brfile = new  BuildRulesXMLFile();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>BuildRulesXMLFile</title></head>");
        out.println("<body>");
        out.println("<p>Business Rules XML file is being generated.</p>");
        out.println("</body></html>");
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
