package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.VendorRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.Vendor;

public class VendorRepositoryImpl extends HibernateDomainRepositoryImpl implements VendorRepository {

    public VendorRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public void makePersistent(Object entity) {
        super.makePersistent(entity);
    }

    public List<Vendor> findAll() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String hql = "select new Vendor(v.vendorId,v.externalOrgDetail.orgName) from Vendor v" +
                        " order by lower(v.externalOrgDetail.orgName) asc";
                return session.createQuery(hql).setCacheable(true).list();
            }
        });
    }

    public Vendor getById(Long vendorId) {
        Object vendor = super.get(Vendor.class, vendorId);
        return vendor != null ? (Vendor) vendor : null;
    }
}
