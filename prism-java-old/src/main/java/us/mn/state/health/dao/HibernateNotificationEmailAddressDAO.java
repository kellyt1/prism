package us.mn.state.health.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.NotificationEmailAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.Collection;
import java.util.List;

public class HibernateNotificationEmailAddressDAO implements NotificationEmailAddressDAO {

    public HibernateNotificationEmailAddressDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }

    public Collection findAll() throws InfrastructureException {
        Collection notificationEmailAddresses;
        try{
            Criteria criteria = HibernateUtil.getSession().createCriteria(NotificationEmailAddress.class);
            criteria.addOrder(Order.asc("emailAddress"));
            criteria.setCacheable(true);
            notificationEmailAddresses = criteria.list();
        }catch (HibernateException ex){
            throw new InfrastructureException(ex);
        }
        return notificationEmailAddresses;
    }

    public Collection findByOrgBudgetId(Long orgBudgetId) throws InfrastructureException {
        List result;
        Session session = HibernateUtil.getSession();
        try {
            result = session.getNamedQuery("findByOrgBudgetId")
                    .setLong("orgBudgetId", orgBudgetId)
                    .setCacheable(true)
                    .list();
        }catch (HibernateException ex){
            throw new InfrastructureException(ex);
        }

        return result;
    }
}
