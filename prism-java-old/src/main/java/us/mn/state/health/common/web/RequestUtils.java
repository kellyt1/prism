package us.mn.state.health.common.web;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;

/**
 * Contains utilities used to help with the use of HTTPServletRequests
 * @author Jason Stull
 */
public class RequestUtils  {

  /**
   * Populates a JavaBean from corresponding matching Request parameters.
   * Does not currently support File Uploads (Multipart Requests).
   * @param toBean Bean whose setter properties will be invoked with
   * values from matching Request parameters
   * @param from Request whose parameters will be used to set matching
   * properties in "toBean"
   * @throws javax.servlet.ServletException
   */
    public static void populate(Object toBean, HttpServletRequest from) 
            throws ServletException {
    
        try {
          Enumeration e = from.getParameterNames();
          while(e.hasMoreElements()) {
            String paramName = (String)e.nextElement();
            Object paramValue = from.getParameter(paramName);
            PropertyUtils.invokeWriteMethodWithPropertyName(toBean, paramName, paramValue);
          }
        }
        catch(ReflectivePropertyException rpe) {
          throw new ServletException("Failed populating bean from Request: ", rpe);
        }
    }
}