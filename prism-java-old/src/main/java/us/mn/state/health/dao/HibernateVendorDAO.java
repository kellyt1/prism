package us.mn.state.health.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.AliasToBeanResultTransformer;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HibernateVendorDAO implements VendorDAO {

    public HibernateVendorDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }

    public Vendor getVendorById(Long vendorId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Vendor vendor = null;
        try {
            if (lock) {
                vendor = (Vendor) session.load(Vendor.class, vendorId, LockMode.UPGRADE);
            } else {
                vendor = (Vendor) session.load(Vendor.class, vendorId);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return vendor;
    }

    public Vendor getVendorByOrgCode(String orgCode, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Vendor vendor = null;

        Query query = HibernateUtil.getSession().getNamedQuery("findVendorByOrgCode");
        query.setString("orgCode", orgCode);
        List<Vendor>vlist = query.list();
        for (int i = 0; i < vlist.size(); i++) {
            vendor =  vlist.get(i);

        }
        return vendor;
    }

    public Vendor getVendorByLegacyId(String id) throws InfrastructureException {
        if (StringUtils.nullOrBlank(id)) {
            return null;
        }
        Vendor result = (Vendor) HibernateUtil.getSession()
                .createCriteria(Vendor.class)
                .add(Expression.eq("legacyId", id))
                .uniqueResult();
        return result;
    }

    public Collection findAll() throws InfrastructureException {
//        return findAll(true);
        try {
            Collection vendors;
            Session session = HibernateUtil.getSession();
            Criteria criteria = session.createCriteria(Vendor.class);
            criteria.createAlias("externalOrgDetail", "extOrg");
            criteria.addOrder(Order.asc("extOrg.upperOrgName"));

            vendors = criteria.list();
            return vendors;
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public Collection findAllAsDTO() throws InfrastructureException {
//        return findAll(true);
        try {
            Collection vendors;
            Session session = HibernateUtil.getSession();
            Criteria criteria = session.createCriteria(Vendor.class);
            criteria.createAlias("externalOrgDetail", "extOrg");
            criteria.addOrder(Order.asc("extOrg.upperOrgName"));
            criteria.setProjection(
                    Projections.projectionList()
                            .add(Projections.id().as("vendorId"))
                            .add(Projections.property("extOrg.orgName").as("vendorName"))
            ).setResultTransformer(new AliasToBeanResultTransformer(Vendor.class));
            criteria.setCacheable(false);

            vendors = criteria.list();
            return vendors;
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

//    public Collection findAll(boolean withUnknown) throws InfrastructureException {
//        try {
//            Collection vendors;
//            Session session = HibernateUtil.getSession();
//            Criteria criteria = session.createCriteria(Vendor.class);
//            criteria.createAlias("externalOrgDetail", "extOrg");
//            criteria.addOrder(Order.asc("extOrg.upperOrgName"));
//
//            if(!withUnknown) {
//                criteria.add(Expression.ne("extOrg.orgCode", Vendor.UNKNOWN));
//            }
//            vendors = criteria.list();
//            return vendors;
//        }
//        catch(HibernateException ex) {
//            throw new InfrastructureException(ex);
//        }
//    }

    public Collection findByExample(Vendor vendor) throws InfrastructureException {
        Collection vendors;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(Vendor.class);
            vendors = crit.add(Example.create(vendor)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return vendors;
    }

    /**
     * Find vendors whose first name starts in a given alphabetical range.
     * Includes vendors whose name starts with the character represented by the end char
     *
     * @param end   - the first letter in the range
     * @param start - the last letter (inclusive) in the range
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection findByNameFirstCharRange(char start, char end) throws InfrastructureException {
        Collection vendors;
        try {
            String startStr = "" + start + "%";
            String endStr = "" + end + "%";

            Session session = HibernateUtil.getSession();
            Criteria criteria = session.createCriteria(Vendor.class);
            criteria.createAlias("externalOrgDetail", "extOrg");
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Expression.between("extOrg.upperOrgName", startStr, endStr));        //between excludes endStr
            disjunction.add(Expression.like("extOrg.upperOrgName", endStr));                    //so we have to add a like statement here

            criteria.add(disjunction);
            // Added by JMP only show active Vendors
//            criteria.add(Expression.isNull("extOrg.orgEndDate"));
            
            //Added by MTR to only show active Vendors
            // (orgEffectiveDate is NULL or <= today) AND (orgEndDate is NULL or >= today)
            disjunction = Restrictions.disjunction();
            disjunction.add(Expression.isNull("extOrg.orgEndDate"));
            disjunction.add(Expression.ge("extOrg.orgEndDate", new Date()));
            criteria.add(disjunction);

            disjunction = Restrictions.disjunction();
            disjunction.add(Expression.isNull("extOrg.orgEffectiveDate"));
            disjunction.add(Expression.le("extOrg.orgEffectiveDate", new Date()));
            criteria.add(disjunction);

            criteria.addOrder(Order.asc("extOrg.orgName"));
            vendors = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return vendors;
    }

    public Collection findVendorByNameLike(String name) throws InfrastructureException {
        Collection vendors = new ArrayList();
        if (StringUtils.nullOrBlank(name)) {
            return vendors;
        }
        try {
            Session session = HibernateUtil.getSession();
            Criteria criteria = session.createCriteria(Vendor.class);
            criteria.createAlias("externalOrgDetail", "extOrg");
//            criteria.add(Restrictions.ilike("extOrg.upperOrgName", "%"+name.toUpperCase()+"%"));                    //so we have to add a like statement here
            criteria.add(Restrictions.or(Restrictions.ilike("extOrg.upperOrgName", name + "%")
                    , Restrictions.ilike("extOrg.upperOrgName", " " + name + "%")));                    //so we have to add a like statement here
            criteria.add(Expression.isNull("extOrg.orgEndDate"));
            criteria.addOrder(Order.asc("extOrg.orgName"));
            vendors = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return vendors;
    }

    public Collection findItemVendorsById(Long itemId) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Query vendorItemQuery = session
                .createQuery("from ItemVendor as itemVendor where itemVendor.item.itemId = :itemId")
                .setLong("itemId", itemId.longValue());

        //Criteria vendorCriteria = session.createCriteria(Vendor.class);
        //vendorCriteria.createAlias("externalOrgDetail", "extOrg");
        //vendorCriteria.addOrder(Order.asc("extOrg.upperOrgName"));
        return vendorItemQuery.list();
    }

    public Collection findVendorWhereNotWithItemId(Long itemId, boolean withUnknown) throws InfrastructureException {
        try {
//            Query query = HibernateUtil.getSession().getNamedQuery("findVendorWhereNotWithItemId");
//            query.setLong("itemId", itemId.longValue());
//            vendors = query.list();
//            return vendors;

            Session session = HibernateUtil.getSession();
            Query vendorItemQuery = session
                    .createQuery("select itemVendor.vendor.vendorId from ItemVendor as itemVendor where itemVendor.item.itemId = :itemId")
                    .setLong("itemId", itemId.longValue());

            Criteria vendorCriteria = session.createCriteria(Vendor.class);
            vendorCriteria.createAlias("externalOrgDetail", "extOrg");
            vendorCriteria.addOrder(Order.asc("extOrg.upperOrgName"));
            if (!withUnknown) {
                vendorCriteria.add(Expression.ne("extOrg.orgCode", Vendor.UNKNOWN));
            }
            List vendorIds = vendorItemQuery.list();
            if (vendorIds.size() > 0) {
                vendorCriteria.add(Restrictions.not(Restrictions.in("vendorId", vendorIds)));
            }
            vendorCriteria.add(Expression.isNull("extOrg.orgEndDate"));
            return vendorCriteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makePersistent(Vendor vendor) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(vendor);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Vendor vendor) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(vendor);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
