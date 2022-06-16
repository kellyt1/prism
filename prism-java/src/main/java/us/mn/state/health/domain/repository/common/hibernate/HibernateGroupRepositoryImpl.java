package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.model.common.Group;

public class HibernateGroupRepositoryImpl extends HibernateDaoSupport implements GroupRepository {


    public HibernateGroupRepositoryImpl(HibernateTemplate hibernateTemplate) {
        setHibernateTemplate(hibernateTemplate);
    }

    public Group findGroupByCode(final String groupCode) {
        Object group = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.getNamedQuery("findGroupByCode");
                query.setString("groupCode", groupCode);
                return query.uniqueResult();
            }
        });
        return group != null ? (Group) group : null;
    }
}
