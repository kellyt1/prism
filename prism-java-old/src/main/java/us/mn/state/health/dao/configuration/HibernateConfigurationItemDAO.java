package us.mn.state.health.dao.configuration;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.util.configuration.ConfigurationItem;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateConfigurationItemDAO {
    public HibernateConfigurationItemDAO() {
        try{
             HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie){
            ie.printStackTrace();
        }
    }

    //called from NotifyEvaluatorsFacadeImpl whose methods are intercepted by
    //spring which manages the transactions, so do not call HibernateUtil.beginTransaction()
    public HibernateConfigurationItemDAO(String str) {
    }

    public ConfigurationItem getConfigurationItem(String applicationCode, String configurationItemCode) throws InfrastructureException {
        ConfigurationItem configurationItem = null;
        try {
            Session session = HibernateUtil.getSession();
            Query query = session.getNamedQuery("findByApplicationCodeAndConfigurationItemCode");
            query.setString("appCode",applicationCode );
            query.setString("itemCode", configurationItemCode);
            query.setCacheable(true);
            Collection items = query.list();
            configurationItem = (ConfigurationItem)items.iterator().next();

        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new InfrastructureException(e);
        }
        catch(Throwable throwable){
            throwable.printStackTrace();
        }
        return configurationItem;
    }
}
