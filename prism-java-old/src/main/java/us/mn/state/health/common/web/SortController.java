package us.mn.state.health.common.web;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import us.mn.state.health.common.util.CollectionUtils;

public class SortController extends HttpServlet  {
    private static final String CONTENT_TYPE = "text/html; charset=utf-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionKey = request.getParameter("sortKey");
        HashMap sortDef = (HashMap)request.getSession().getAttribute(sessionKey);
        sort(request, sortDef);
        
        //Set up return trip
        String page = (String)sortDef.get("input");
        RequestDispatcher dispatcher =
            request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    
    private synchronized void sort(HttpServletRequest request, HashMap sortDef) throws ServletException {
        try {
            String beanName = (String)sortDef.get("name");
            String propertyName = (String)sortDef.get("property");
            String value = (String)sortDef.get("value");
            HttpSession session = request.getSession();
            Object bean = session.getAttribute(beanName);
            List list = (List)PropertyUtils.getNestedProperty(bean, propertyName);
            CollectionUtils.sort(list, value, true);
        }
        catch(Exception e) {
            throw new ServletException(e);
        }
    }
}