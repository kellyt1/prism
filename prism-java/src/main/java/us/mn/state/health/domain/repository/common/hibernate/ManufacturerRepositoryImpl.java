package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.ManufacturerRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.Manufacturer;

public class ManufacturerRepositoryImpl extends HibernateDomainRepositoryImpl implements ManufacturerRepository {

    public ManufacturerRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public void makePersistent(Manufacturer entity) {
        super.makePersistent(entity);
    }

    public Manufacturer findManufacturerByCode(final String code) {
        Object m = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.getNamedQuery("findManufacturerByCode").setString("code", code).uniqueResult();
            }
        });
        return m != null ? (Manufacturer) m : null;
    }

    public List<Manufacturer> findAll(boolean withUnknown) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Manufacturer.class);
        if (!withUnknown) {
            criteria.add(Expression.not(Expression.eq("manufacturerCode", Manufacturer.CODE_UNKNOWN)));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    public Manufacturer getManufacturerById(Long id) {
        Object o = getHibernateTemplate().get(Manufacturer.class, id);
        return o != null ? (Manufacturer) o : null;
    }

}
