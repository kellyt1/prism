package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.HazardousRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.Hazardous;

public class HazardousRepositoryImpl extends HibernateDomainRepositoryImpl implements HazardousRepository {

    public HazardousRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public List<Hazardous> findAll() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createQuery("from Hazardous h order by h.description asc").setCacheable(true).list();
            }
        });
    }

    public Hazardous getById(Long hazardousId) {
        Object o = super.get(Hazardous.class, hazardousId);
        return o != null ? (Hazardous) o : null;
    }

    public Hazardous loadById(Long hazardousId) {
        return (Hazardous) super.load(Hazardous.class, hazardousId);
    }
}
