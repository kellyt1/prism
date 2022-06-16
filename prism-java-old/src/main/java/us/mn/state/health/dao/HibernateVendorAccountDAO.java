package us.mn.state.health.dao;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.VendorAccount;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateVendorAccountDAO implements VendorAccountDAO {

    public HibernateVendorAccountDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie) {
        }
    }

    public VendorAccount getVendorAccountById(Long vendorAccountId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        VendorAccount vendorAccount = null;
        try {
            if(lock) {
                vendorAccount = (VendorAccount)session.load(VendorAccount.class, vendorAccountId, LockMode.UPGRADE);
            } 
            else {
                vendorAccount = (VendorAccount)session.load(VendorAccount.class, vendorAccountId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return vendorAccount;
    }

    public void makePersistent(VendorAccount vendorAccount) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(vendorAccount);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(VendorAccount vendorAccount) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(vendorAccount);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
