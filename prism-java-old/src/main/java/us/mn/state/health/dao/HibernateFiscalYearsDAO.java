package us.mn.state.health.dao;

import us.mn.state.health.persistence.HibernateUtil;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.FiscalYears;
import java.util.Collection;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;

public class HibernateFiscalYearsDAO implements FiscalYearsDAO {

    public HibernateFiscalYearsDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }


    public FiscalYears getFiscalYearById(Long inID, boolean lock) throws InfrastructureException {
        Collection fys;
        FiscalYears fy = null;
        try {
//            if (lock) {
//                fy = (FiscalYears) session.load(FiscalYears.class, inID, LockMode.UPGRADE);
//            }
//            else {
                Criteria crit = null;
                crit = HibernateUtil.getSession().createCriteria(FiscalYears.class);
                //crit.setCacheable(false);
                crit.add(org.hibernate.criterion.Restrictions.eq("yearsago",inID));            
                fys = crit.list();
                if(fys==null || fys.size() == 0){
                      fy = null;
                  }
                  else {
                      fy = (FiscalYears) fys.iterator().next();
                  }
//            }
        }
        catch (HibernateException ex) {
            fy = null;
            //throw new InfrastructureException(ex);
        }
        return fy;
    }

    public Collection findAll() throws InfrastructureException {
        Collection fys;
        try {
                 Criteria crit = null;
                 crit = HibernateUtil.getSession().createCriteria(FiscalYears.class);
                 crit.setCacheRegion("findAllFiscalYears");
                 crit.setCacheable(true);
                 crit.add(org.hibernate.criterion.Restrictions.le("yearsago",new Long(10)));

                 crit.addOrder(org.hibernate.criterion.Order.desc("fiscalyear"));
                 fys = crit.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return fys;
    }


    public void makePersistent(FiscalYears fiscalYear) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(fiscalYear);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(FiscalYears fiscalYear) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(fiscalYear);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

}
