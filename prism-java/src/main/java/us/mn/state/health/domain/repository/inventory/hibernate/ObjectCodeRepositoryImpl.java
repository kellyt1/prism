package us.mn.state.health.domain.repository.inventory.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.inventory.ObjectCodeRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.ObjectCode;

public class ObjectCodeRepositoryImpl extends HibernateDomainRepositoryImpl implements ObjectCodeRepository {

    public ObjectCodeRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public ObjectCode findByCode(final String code) {
        Object oc = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findByCode").setString("objectCode", code).uniqueResult();
            }
        });
        return oc != null ? (ObjectCode) oc : null;
    }

    public List<ObjectCode> findAll() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createQuery("from ObjectCode o order by o.objectCode asc").setCacheable(true).list();
            }
        });
    }

    public void makePersistent(Object entity) {
        super.makePersistent(entity);
    }

}
