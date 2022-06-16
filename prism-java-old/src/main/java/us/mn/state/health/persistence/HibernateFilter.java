package us.mn.state.health.persistence;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;

/**
 * A servlet filter that opens and closes a Hibernate Session for each request.
 * <p/>
 * This filter guarantees a sane state, committing any pending database
 * transaction once all other filters (and servlets) have executed. It also
 * guarantees that the Hibernate <tt>Session</tt> of the current thread will
 * be closed before the response is send to the client.
 * <p/>
 * Use this filter for the <b>session-per-request</b> pattern and if you are
 * using <i>Detached Objects</i>.
 *
 * @see HibernateUtil
 */
public class HibernateFilter implements Filter {
    private static Log log = LogFactory.getLog(HibernateFilter.class);
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // There is actually no explicit "opening" of a Session, the
        // first call to HibernateUtil.beginTransaction() in control
        // logic (e.g. use case controller/event handler) will get
        // a fresh Session.
        try {
//            HibernateUtil.registerInterceptor(new LuceneInterceptor());

            chain.doFilter(request, response);

            // Commit any pending database transaction.
            HibernateUtil.commitTransaction();
            
            log.debug("Total memory: " + Runtime.getRuntime().totalMemory());
            log.debug(" Free memory: " + Runtime.getRuntime().freeMemory());
        }
        catch (Exception e) {
            log.error("Hibernate Filter commit.");
            e.printStackTrace();
            try {
                HibernateUtil.rollbackTransaction();
            } catch (InfrastructureException e1) {}
           throw new ServletException(e);
        } 
        finally {
            // No matter what happens, close the Session.
            try {
                HibernateUtil.closeSession();
            } 
            catch (Exception e) {
                log.error("Hibernate Filter close.");
                e.printStackTrace();
                //Do Nothing
            }
        }
    }

    public void destroy() {
    }
}