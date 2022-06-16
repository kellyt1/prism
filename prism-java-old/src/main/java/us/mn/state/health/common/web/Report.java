package us.mn.state.health.common.web;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.PropertyUtils;

public class Report  {

    public String name;

    public Object data;
    
    public Collection subReports = new ArrayList();
    
    
    public static Report create(String name, Object data) throws InfrastructureException {
        try {
            Report report = null;
            if(data == null) {
                return null;
            }
            else if(PropertyUtils.isWrapperType(data.getClass())) {
                return null;
            }
            else {
                PropertyDescriptor[] props = 
                    org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(data);
                for(int i = 0; i < props.length; i++) {
                    Object subData = 
                        props[i].getReadMethod().invoke(data, new Object[]{});
                    
                }
            }
            return null;
        }
        catch(Exception e) {
            throw new InfrastructureException(e);
        }
       
        
    }


    public void setData(Object data) {
        this.data = data;
    }


    public Object getData() {
        return data;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setSubReports(Collection subReports) {
        this.subReports = subReports;
    }


    public Collection getSubReports() {
        return subReports;
    }
}