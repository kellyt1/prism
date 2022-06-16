package us.mn.state.health.dao;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateObjectCodeDAO extends HibernateDAO implements ObjectCodeDAO{

    public ObjectCode findByCode(String code) throws InfrastructureException {
        Collection collection =
                HibernateUtil.getSession()
                .getNamedQuery("findByCode").setString("objectCode",code)
                .list();
        if(collection==null || collection.size()==0){
            return null;
        }
        else {
            return (ObjectCode) collection.iterator().next();
        }

    }
}
