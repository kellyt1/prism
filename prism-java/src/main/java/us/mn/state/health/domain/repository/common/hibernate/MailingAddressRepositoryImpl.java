package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.MailingAddressRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.MailingAddress;

public class MailingAddressRepositoryImpl extends HibernateDomainRepositoryImpl implements MailingAddressRepository {

    public MailingAddressRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public List<MailingAddress> findShippingAddresses() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findShippingAddresses").setCacheable(true).list();
            }
        });
    }
}
