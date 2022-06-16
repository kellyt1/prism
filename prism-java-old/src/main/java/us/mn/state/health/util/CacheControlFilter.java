package us.mn.state.health.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: rauscd1
 * Date: 5/27/2014
 * Time: 11:25 AM
 *
 * This class is used to manage the http headers that control client side caching.
 */

public class CacheControlFilter implements Filter {
    DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss");

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        Long epochTime = new Date().getTime();

        resp.setDateHeader("Last-Modified", epochTime);
        // 3600000 is one hour in milliseconds. Time isn't critical, we just want to make sure stale resources
        // get updated after a new deployment.
        resp.setHeader("Expires", df.format(new Date(epochTime + (3600000 * 29))) + " GMT");
        resp.setHeader("Cache-Control", "max-age=86400");

        chain.doFilter(request, response);
    }

    //Needed for implementing Filter
    public void destroy(){}

    //Needed for implementing Filter
    public void init(FilterConfig config){}
}