package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import static org.hibernate.criterion.Restrictions.eq;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.model.common.User;

public class HibernateUserRepositoryImpl extends HibernateDaoSupport implements UserRepository {

    public HibernateUserRepositoryImpl(HibernateTemplate template) {
        setHibernateTemplate(template);
    }

    public List<User> findAllUsers() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String hql =
                        "select new User(u.personId, u.firstName, u.middleName, u.lastName) from User u" +
                                " order by lower(u.lastName) asc, lower(u.firstName) asc";
                Query query = session.createQuery(hql);
                query.setCacheable(true);
                return query.list();
            }
        });
    }

    public User getUserById(final Long userId) {
        Object result = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(User.class).add(eq("personId", userId));
                return criteria.uniqueResult();
            }
        });
        return result != null ? (User) result : null;
    }

    public User getUser(final String username) {
        if (StringUtils.isEmpty(username))
            throw new IllegalArgumentException("The username parameter is an empty string");
        Object user = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(User.class).add(Restrictions.ilike("username", username.trim())).uniqueResult();
            }
        });
        return user != null ? (User) user : null;
    }

    public List<User> getUsersByGroupCode(final String groupCode) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findUsersByGroupCode").
                        setParameter("groupCode", groupCode).list();
            }
        });
    }
}
