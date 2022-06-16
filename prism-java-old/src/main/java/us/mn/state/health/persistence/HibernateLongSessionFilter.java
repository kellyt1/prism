package us.mn.state.health.persistence;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;

/**
 * A servlet filter that disconnects and reconnects a Hibernate Session for each request.
 * <p/>
 * Use this filter for the <b>session-per-application-transaction</b> pattern
 * with a <i>Long Session</i>. Don't forget to demarcate application transactions
 * in your code, as described in Hibernate in Action.
 *
 * @author Christian Bauer <christian@hibernate.org>
 * @see HibernateUtil
 */
public class HibernateLongSessionFilter
        implements Filter {

    private static final String HTTPSESSIONKEY = "HibernateSession";
    private static Log log = LogFactory.getLog(HibernateLongSessionFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Servlet filter init, now disconnecting/reconnecting a Session for each request.");
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        // Try to get a Hibernate Session from the HttpSession
        HttpSession userSession =
                ((HttpServletRequest) request).getSession();
        Session hibernateSession =
                (Session) userSession.getAttribute(HTTPSESSIONKEY);

        if (hibernateSession != null) {
            try {
                HibernateUtil.reconnect(hibernateSession);
            }
            catch(InfrastructureException ie) {
                throw new ServletException(ie);
            }
        }
            

        // If there is no Session, the first call to
        // HibernateUtil.beginTransaction in application code will open
        // a new Session for this thread.
        try {
            chain.doFilter(request, response);

            ((HttpServletResponse) response).setHeader("Pragma", "no-cache");
            ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache");
            ((HttpServletResponse) response).setHeader("Cache-Control", "no-store");
            ((HttpServletResponse) response).setDateHeader("Expires", 0);

            // Commit any pending database transaction.
            try {
                HibernateUtil.commitTransaction();
            }
            catch(InfrastructureException ie) {
                throw new ServletException(ie);
            }

        } finally {
            // TODO: The Session should be closed if a fatal exceptions occurs

            // No matter what happens, disconnect the Session.
            try {
                hibernateSession = HibernateUtil.disconnectSession();
            }
            catch(InfrastructureException ie) {
                throw new ServletException(ie);
            }
            // and store it in the users HttpSession
            userSession.setAttribute(HTTPSESSIONKEY, hibernateSession);
        }
    }

    public void destroy() {
    }

}