package us.mn.state.health.domain.repository.common.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.StatusRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.Status;

import java.sql.SQLException;
import java.util.List;

public class HibernateStatusRepositoryImpl extends HibernateDomainRepositoryImpl implements StatusRepository {

    public HibernateStatusRepositoryImpl(HibernateTemplate hibernateTemplate) {
        setHibernateTemplate(hibernateTemplate);
    }

    public List<Status> findAllByStatusTypeCode(final String statusTypeCode) {

        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.getNamedQuery("findAllByStatusTypeCode");
                query.setString("statusTypeCode", statusTypeCode);
                query.setCacheable(true);
                return query.list();
            }

        });
    }

    public Status findByStatusTypeCodeAndStatusCode(final String statusTypeCode, final String statusCode) {
        Object status = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.getNamedQuery("findByStatusTypeCodeAndStatusCode");
                query.setString("statusTypeCode", statusTypeCode);
                query.setString("statusCode", statusCode);
                query.setCacheable(true);
                return query.uniqueResult();
            }
        });
        return status != null ? (Status) status : null;
    }

    public Status getStatusByStatusCode(final String statusCode) {
        Object status = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.getNamedQuery("getStatusByStatusCode");
                query.setString("statusCode", statusCode);
                query.setCacheable(true);
                return query.uniqueResult();
            }
        });
        return status != null ? (Status) status : null;
    }

    public Status getById(Long statusId) {
        Object o = getHibernateTemplate().get(Status.class, statusId);
        return o != null ? (Status) o : null;
    }

    public Status loadById(Long statusId) {
        return (Status) getHibernateTemplate().load(Status.class, statusId);
    }
}
