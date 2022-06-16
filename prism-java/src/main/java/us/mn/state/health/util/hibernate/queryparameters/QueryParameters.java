package us.mn.state.health.util.hibernate.queryparameters;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Contains the data for executing a query
 *
 * @author Lucian Ochian
 */
abstract public class QueryParameters {

    private Map map = new HashMap();

    private BaseQueryParameters baseQueryParameters;


    public boolean equals(Object x) {
        if (!(x instanceof QueryParameters))
            return false;
        QueryParameters other = (QueryParameters) x;
        return map.equals(other.map);
    }

    public int hashCode() {
        return map.hashCode();
    }

    public void setString(String name, String value) {
        map.put(name, value);
    }

    public void setInteger(String name, int value) {
        map.put(name, new Integer(value));
    }

    public Iterator iterator() {
        return map.entrySet().iterator();
    }

    public Map getParamsAsMap() {
        return map;
    }

    public void setDate(String name, Date value) {
        map.put(name, value);
    }


    public BaseQueryParameters getHibernateBaseQueryParameters() {
        return baseQueryParameters;
    }

    public void setHibernateQueryParameters(BaseQueryParameters baseQueryParameters) {
        this.baseQueryParameters = baseQueryParameters;
    }
}