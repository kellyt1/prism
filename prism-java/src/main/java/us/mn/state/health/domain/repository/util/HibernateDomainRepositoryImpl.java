package us.mn.state.health.domain.repository.util;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateDomainRepositoryImpl extends HibernateDaoSupport implements DomainRepository {


    protected HibernateDomainRepositoryImpl() {
    }

    public HibernateDomainRepositoryImpl(HibernateTemplate template) {
        setHibernateTemplate(template);
    }

    public List findAll(final Class clazz, final boolean cacheable) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(clazz).setCacheable(cacheable).list();
            }
        });
    }

    public void makePersistent(Object entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    public Object get(Class entityClass, Serializable id) {
        return getHibernateTemplate().get(entityClass, id);
    }

    public Object get(Class entityClass, Serializable id, LockMode lockMode) {
        return getHibernateTemplate().get(entityClass, id, lockMode);
    }

    public Object load(Class entityClass, Serializable id) {
        return getHibernateTemplate().load(entityClass, id);
    }

    public Object load(Class entityClass, Serializable id, LockMode lockMode) {
        return getHibernateTemplate().load(entityClass, id, lockMode);
    }

}
