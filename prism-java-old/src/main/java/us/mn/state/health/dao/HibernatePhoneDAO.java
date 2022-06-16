package us.mn.state.health.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernatePhoneDAO extends HibernateDAO implements PhoneDAO{
    public Collection findAll() throws InfrastructureException {
        return super.findAll(Phone.class);
    }

    public Collection findByExample(Phone phone) throws InfrastructureException {
        Collection phones;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(us.mn.state.health.model.common.Phone.class);
            phones = crit.add(Example.create(phone)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return phones;
    }

    public Phone getPhoneById(Long phoneId, boolean lock) throws InfrastructureException {
       Session session = HibernateUtil.getSession();
		Phone phone = null;
		try {
			if (lock) {
				phone = (Phone)session.load(us.mn.state.health.model.common.Phone.class, phoneId, LockMode.UPGRADE);
			}
            else {
				phone = (Phone)session.load(us.mn.state.health.model.common.Phone.class, phoneId);
			}
		}
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return phone;
    }

    public void makePersistent(Phone phone) throws InfrastructureException {
        super.makePersistent(phone);

    }

    public Phone getPhoneByNumber(String phoneNumber) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Phone example = new Phone();
        example.setNumber(phoneNumber);
        Collection results;
        results =  session.createCriteria(Phone.class)
                          .add(Example.create(example))
                          .setMaxResults(1)
                          .list();
        if(results == null || results.size() == 0){
            return null;
        }
        return (Phone) results.iterator().next();
    }

    public void makeTransient(Phone phone) throws InfrastructureException {
        super.makeTransient(phone);
    }
}
