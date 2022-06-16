package us.mn.state.health.domain.repository.inventory.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.inventory.CycleCountPriorityRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.inventory.CycleCountPriority;

public class CycleCountPriorityRepositoryImpl extends HibernateDomainRepositoryImpl implements CycleCountPriorityRepository {

    public CycleCountPriorityRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public List<CycleCountPriority> findAll() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createQuery("from CycleCountPriority c order by c.code asc").setCacheable(true).list();
            }
        });
    }

    public CycleCountPriority getById(Long id) {
        Object o = getHibernateTemplate().get(CycleCountPriority.class, id);
        return o != null ? (CycleCountPriority) o : null;
    }
}
