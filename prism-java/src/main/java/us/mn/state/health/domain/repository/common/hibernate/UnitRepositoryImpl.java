package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.UnitRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.inventory.Unit;

public class UnitRepositoryImpl extends HibernateDomainRepositoryImpl implements UnitRepository {


    public UnitRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public Unit getUnitById(Long unitId) {
        Object o = super.get(Unit.class, unitId);
        return o != null ? (Unit) o : null;
    }

    public Unit loadUnitById(Long unitId) {
        return (Unit) super.load(Unit.class, unitId);
    }

    public List findAll(final boolean withUnknown) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(Unit.CODE_UNKNOWN);
                if (withUnknown) {
                    criteria.add(Restrictions.not(Restrictions.in("code", new Object[]{Unit.CODE_UNKNOWN})));
                }
                criteria.setCacheable(true);
                return criteria.list();
            }
        });
    }

    public List<Unit> findAll(final boolean withUnknown, final String[] orderBy) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(Unit.class);
                if (withUnknown) {
                    criteria.add(Restrictions.not(Restrictions.in("code", new String[]{Unit.CODE_UNKNOWN})));
                }
                for (String s : orderBy) {
                    criteria.addOrder(Order.asc(s));
                }
                criteria.setCacheable(true);
                return criteria.list();
            }
        });
    }

    public Unit findUnitByCode(String code) {
        List result = getHibernateTemplate().findByExample(new Unit(null, code));
        if (result != null && result.size() > 0) {
            return (Unit) result.iterator().next();
        }
        return null;
    }

    public void makePersistent(Unit unit) {
        super.makePersistent(unit);
    }

}
