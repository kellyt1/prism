package us.mn.state.health.dao;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateVendorContractDAO implements VendorContractDAO {

    public HibernateVendorContractDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie) {
        }
    }

    public VendorContract getVendorContractById(Long vendorContractId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        VendorContract vendorContract = null;
        try {
            if(lock) {
                vendorContract = (VendorContract)session.load(VendorContract.class, vendorContractId, LockMode.UPGRADE);
            } 
            else {
                vendorContract = (VendorContract)session.load(VendorContract.class, vendorContractId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return vendorContract;
    }

    public void makePersistent(VendorContract vendorContract) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(vendorContract);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(VendorContract vendorContract) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(vendorContract);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
