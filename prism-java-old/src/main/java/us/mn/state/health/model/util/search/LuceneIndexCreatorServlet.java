package us.mn.state.health.model.util.search;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.search.BooleanQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import us.mn.state.health.common.exceptions.InfrastructureException;

/**
 * this class exists simply to ensure that the lucene indexes are created right away, once the
 * application is started, because we've had some problems with Hibernate when the index has to be
 * created in the middle of some other transaction.  The index creation is done in the init() method.
 */
public class LuceneIndexCreatorServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=utf-8";
    private static ApplicationContext applicationContext = null;

    /**
     * This method simply instantiates each class that represents a lucene index.  Each of these
     * classes defines a static code block that checks for the existense of the index folder.  If the
     * folder exists, it does nothing.  If the folder does not exist, it creates it.
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        applicationContext = wac;

        Boolean newIndex = new Boolean(config.getServletContext().getInitParameter("newIndex"));
        this.buildIndexes(newIndex);
        System.getProperties().put("org.apache.lucene.commitLockTimeout", "120000");
        System.getProperties().put("org.apache.lucene.writeLockTimeout", "120000");
        BooleanQuery.setMaxClauseCount(400000);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private void buildIndexes(Boolean newIndex) {
//        OrderIndex.createIndex(newIndex.booleanValue());
//        RequestIndex.createIndex(newIndex.booleanValue());
//        StockItemIndex.createIndex(newIndex.booleanValue());
//        RequestLineItemIndex.createIndex(newIndex.booleanValue());
        PurchaseItemIndex.createIndex(newIndex.booleanValue());
        if (!PurchaseItemIndex.indexExists ) {
            try {
            PurchaseItemIndex.createIndexAtRuntime();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        AssetIndex.createIndex(newIndex.booleanValue());

    /**
     *      The following indexes are using Hibernate Search, which will create the index
     *      folder and starting index files if the folder does not exist, but will not
     *      create the starting index files if the folder is empty.  The following will
     *      add the base index files if the index folder exists, but is empty.
     *        A quartz job will run shortly after application startup(5 minutes) and build
     *      the following indexes if they do not exist.  This allows the parts of the application
     *      to be available to users without having to wait for the indexes to complete.
     */
        StockItemIndex.createIndex(newIndex.booleanValue());
        RequestIndex.createIndex(newIndex.booleanValue());
        RequestLineItemIndex.createIndex(newIndex.booleanValue());

//        OrderLineItemIndex.createIndex(newIndex.booleanValue());
    }

    private void buildHibernateSearchIndexes(Boolean newIndex) {
        StockItemIndex.createIndex(newIndex.booleanValue());
        RequestIndex.createIndex(newIndex.booleanValue());
        RequestLineItemIndex.createIndex(newIndex.booleanValue());
        try {
            StockItemIndex.createIndexAtRuntime();
            RequestIndex.createIndexAtRuntime();
        } catch (InfrastructureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>LuceneIndexCreatorServlet</title></head>");
        out.println("<body>");
        out.println("<p>LuceneIndexCreatorServlet is rebuilding the lucene indexes, assuming they've been deleted</p>");
        out.println("</body></html>");
        buildIndexes(Boolean.TRUE);
        buildHibernateSearchIndexes(Boolean.TRUE);
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}