package us.mn.state.health.model.util.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

//import org.apache.commons.logging.LogFactory;



public class SetLogLevel extends HttpServlet {
    private static Log log = LogFactory.getLog(SetLogLevel.class);

    private void displayLoggerLevel(Logger logger, PrintWriter out) {
        out.println("<tr>");
        out.println("<td>" + logger.getName().toString() + "</td><td>"+ logger.getLevel().toString() + "</td><td></td>");
        out.println("</tr>");
    }
    private void changeLoggerLevel(Logger logger, Level level, PrintWriter out) {
        Level OldLevel;
        OldLevel = logger.getLevel();
        logger.setLevel(level);
        out.println("<tr>");
        out.println("<td>" + logger.getName().toString() + "</td><td>"+ OldLevel.toString() + "</td><td>" + logger.getLevel().toString() + "</td>");
        out.println("</tr>");
    }

  private static void copyFile(String srFile, String dtFile){
    try{
      File f1 = new File(srFile);
      File f2 = new File(dtFile);
      InputStream in = new FileInputStream(f1);

      //For Append the file.
//      OutputStream out = new FileOutputStream(f2,true);

      //For Overwrite the file.
      OutputStream out = new FileOutputStream(f2);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0){
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
      log.info("PRISM.log File copied.");
    }
    catch(FileNotFoundException ex){
      log.error(ex.getMessage() + " in the specified directory.");
      System.exit(0);
    }
    catch(IOException e){
      log.error(e.getMessage());
    }
  }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        String newLevel = request.getParameter("level").toUpperCase();
        PrintWriter out = response.getWriter();
        if  (newLevel.equalsIgnoreCase("SHOWLOG")) {
           copyFile((System.getProperty("catalina.base") + "/logs/PRISM.log"),
                    (System.getProperty("catalina.base") + "/webapps/PRISM.log"));
            response.sendRedirect("PRISM.log");
        } else {
            response.setContentType("text/html");
            out.println("<html><body>");
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("<th>Logger Name</th><th>OLD Level</th><th>NEW Level</th>");
            out.println("</tr>");
            Level setNewLevel;

            if (newLevel.equalsIgnoreCase("DISPLAY")) {
                displayLoggerLevel(Logger.getRootLogger(),out);
                displayLoggerLevel(Logger.getLogger("us"),out);
                displayLoggerLevel(Logger.getLogger("us.mn.state.health"),out);
                displayLoggerLevel(Logger.getLogger("us.mn.state.health.persistence"),out);
                displayLoggerLevel(Logger.getLogger("us.mn.state.health.model.util"),out);
                displayLoggerLevel(Logger.getLogger("us.mn.state.health.matmgmt"),out);
                displayLoggerLevel(Logger.getLogger("us.mn.state.health.model.util.search.LuceneInterceptor"),out);
                displayLoggerLevel(Logger.getLogger("net.sf.hibernate"),out);
                displayLoggerLevel(Logger.getLogger("net.sf.hibernate.tool.hbm2ddl"),out);
                displayLoggerLevel(Logger.getLogger("net.sf.hibernate.cache"),out);
                displayLoggerLevel(Logger.getLogger("net.sf.hibernate.type"),out);
                displayLoggerLevel(Logger.getLogger("org.apache.commons.validator"),out);
                displayLoggerLevel(Logger.getLogger("org.apache.struts.validator"),out);
                displayLoggerLevel(Logger.getLogger("org.springframework"),out);
                displayLoggerLevel(Logger.getLogger("us.mn.state.health.web.struts.searchorders.SearchOrdersAction"),out);

            } else {
                if (newLevel.equalsIgnoreCase("DEBUG")) {
                    setNewLevel = Level.DEBUG;
                }  else if (newLevel.equalsIgnoreCase("ERROR")) {
                    setNewLevel = Level.ERROR;
                }  else if (newLevel.equalsIgnoreCase("WARN")) {
                    setNewLevel = Level.WARN;
                }  else if (newLevel.equalsIgnoreCase("INFO")) {
                    setNewLevel = Level.INFO;
                }  else {  //set to default values from log4j.properties
                    setNewLevel = Level.INFO;
//                    setNewLevel = Level.ERROR;
                }
                if (newLevel.equalsIgnoreCase("DEFAULT")) {
                    changeLoggerLevel(Logger.getRootLogger(),Level.ERROR,out);
                    changeLoggerLevel(Logger.getLogger("us"),Level.INFO,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health"),Level.INFO,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.persistence"),Level.INFO,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.model.util"),Level.INFO,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.matmgmt"),Level.INFO,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.model.util.search.LuceneInterceptor"),Level.INFO,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate"),Level.ERROR,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate.tool.hbm2ddl"),Level.ERROR,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate.cache"),Level.ERROR,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate.type"),Level.ERROR,out);
                    changeLoggerLevel(Logger.getLogger("org.apache.commons.validator"),Level.ERROR,out);
                    changeLoggerLevel(Logger.getLogger("org.apache.struts.validator"),Level.ERROR,out);
                    changeLoggerLevel(Logger.getLogger("org.springframework"),Level.WARN,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.web.struts.searchorders.SearchOrdersAction"),Level.INFO,out);
                } else {
                    changeLoggerLevel(Logger.getRootLogger(),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("us"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.persistence"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.model.util"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.matmgmt"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.model.util.search.LuceneInterceptor"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate.tool.hbm2ddl"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate.cache"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("net.sf.hibernate.type"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("org.apache.commons.validator"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("org.apache.struts.validator"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("org.springframework"),setNewLevel,out);
                    changeLoggerLevel(Logger.getLogger("us.mn.state.health.web.struts.searchorders.SearchOrdersAction"),setNewLevel,out);
                }
            }
            out.println("</table>");
        }
        out.println("</body></html>");
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}
