package us.mn.state.health.dao.configuration;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateApplicationConfigDAO extends HibernateDAO implements DAO {
    public HibernateApplicationConfigDAO() {
        try{
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie){}	
    }

}
