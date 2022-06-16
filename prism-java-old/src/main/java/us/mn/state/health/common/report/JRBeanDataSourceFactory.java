package us.mn.state.health.common.report;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.hibernate.Query;
import us.mn.state.health.persistence.HibernateUtil;

public class JRBeanDataSourceFactory  {

    private Collection beans = new ArrayList();
    
    public JRBeanDataSourceFactory() {
    }
    
    public JRBeanDataSourceFactory(Object bean) {
        beans.add(bean);
    }
    
    public JRBeanDataSourceFactory(Collection beans) {
        this.beans = beans;
    }
    
    public JRBeanDataSourceFactory(String hql) {
        try {
            beans = executeQuery(hql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public JRDataSource createBeanCollectionDatasource() {
        return new JRBeanCollectionDataSource(beans);
    }
    
    public void setBeans(Collection beans) {
        this.beans = beans;
    }
    
    public Collection getBeans() {
        return this.beans;
    }
    
    protected Collection executeQuery(String hql) throws Exception {
        Query query = HibernateUtil.getSession().createQuery(hql);
        return query.list();
    }
}