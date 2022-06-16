package us.mn.state.health.domain.repository.common.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.OrgBudgetRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class OrgBudgetRepositoryImpl extends HibernateDomainRepositoryImpl implements OrgBudgetRepository {


    public OrgBudgetRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public OrgBudget getOrgBudgetById(Long orgBudgetId, boolean lock) {
        Object result = null;
        if (lock) {
            result = getHibernateTemplate().get(OrgBudget.class, orgBudgetId, LockMode.WRITE);
        } else {
            result = getHibernateTemplate().get(OrgBudget.class, orgBudgetId);
        }

        return result != null ? (OrgBudget) result : null;
    }

    public OrgBudget loadOrgBudgetById(Long orgBudgetId, boolean lock) {
        Object result = null;
        if (lock) {
            result = getHibernateTemplate().load(OrgBudget.class, orgBudgetId, LockMode.WRITE);
        } else {
            result = getHibernateTemplate().load(OrgBudget.class, orgBudgetId);
        }

        return result != null ? (OrgBudget) result : null;
    }

    public List findAll() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(OrgBudget.class);
                criteria.addOrder(Order.asc("orgBudgetCode"));
                criteria.setCacheable(true);
                return criteria.list();
            }
        });
    }

    public List findAllPurchaseOrgBudgets() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findAllPurchaseOrgBudgets").setCacheable(true).list();
            }
        });
    }

    // ??  begin
    //public List findMissingOrgBudgets() {
    public List<OrgBudget> findMissingOrgBudgets() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findMissingOrgBudgets").setCacheable(true).list();
            }
        });
    }
    // ?? end

    public List findAllByBudgetManager(final User budgetManager) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findAllByBudgetManager")
                        .setEntity("budgetManager", budgetManager).setCacheable(true)
                        .list();
            }
        });
    }

    public OrgBudget findByOrgBudgetCode(final String orgBdgtCode) {
        return (OrgBudget) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findOrgBudgetByOrgBudgetCode")
                        .setString("orgBdgtCode", orgBdgtCode).setCacheable(true)
                        .uniqueResult();
            }
        });
    }

    public void makePersistent(OrgBudget orgBudget) {
        super.makePersistent(orgBudget);
    }

    public OrgBudget findByOrgBudgetCodeAndLastFiscalYear(final String orgBudgetCode) {
        Object ob = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                OrgBudget example = new OrgBudget();
                example.setOrgBudgetCode(orgBudgetCode);
                List results;
                results = session.createCriteria(OrgBudget.class)
                        .add(Example.create(example))
                        .addOrder(Order.desc("effectiveDate"))
                        .setMaxResults(1)
                        .list();
                if (results == null || results.size() == 0) {
                    return null;
                }
                return (OrgBudget) results.iterator().next();
            }
        });
        return ob != null ? (OrgBudget) ob : null;
    }
}
