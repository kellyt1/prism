package us.mn.state.health.common.util;

import org.apache.struts.action.ActionForward;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: rodent1
 * Date: Jul 22, 2009
 * Time: 3:20:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForwardParameters {
    private Map params = null;

public ForwardParameters() {
    params = new HashMap();
}

/**
* Add a single parameter and value.
*
* @param paramName     Parameter name
* @param paramValue    Parameter value
*
* @return A reference to this object.
*/
public ForwardParameters add(String paramName, String paramValue) {
    params.put(paramName,paramValue);
    return this;
}

/**
* Add a set of parameters and values.
*
* @param paramMap  Map containing parameter names and values.
*
* @return A reference to this object.
*/
public ForwardParameters add(Map paramMap) {
    Iterator itr = paramMap.keySet().iterator();
    while (itr.hasNext()) {
        String paramName = (String) itr.next();
        params.put(paramName, paramMap.get(paramName));
    }

    return this;
}

    /**
    * Add parameters to a provided ActionForward.
    *
    * @param forward    The ActionForward object to add parameters to.
    *
    * @return ActionForward with parameters added to the URL.
    */
    public ActionForward forward(ActionForward forward) {
        StringBuffer path = new StringBuffer(forward.getPath());

        Iterator itr = params.entrySet().iterator();
        if (itr.hasNext()) {
            //add first parameter, if available
            Map.Entry entry = (Map.Entry) itr.next();
            path.append("?").append(entry.getKey()).append("=").append(entry.getValue());

            //add other parameters
            while (itr.hasNext()) {
                entry = (Map.Entry) itr.next();
                path.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        return new ActionForward(path.toString());
    }
}



