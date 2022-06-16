package us.mn.state.health.dao.inventory;

import java.util.Collection;

import org.hibernate.HibernateException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateActionRequestTypeDAO implements ActionRequestTypeDAO {

    public HibernateActionRequestTypeDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch (InfrastructureException e) {
            e.printStackTrace();
        }
    }

    public Collection findAll() throws InfrastructureException {
        Collection actionRequestTypes;
        try {
            actionRequestTypes = HibernateUtil.getSession().createCriteria(ActionRequestType.class).list();
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return actionRequestTypes;
    }

    public ActionRequestType findByActionRequestTypeCode(String actionRequestTypeCode) throws InfrastructureException {
        ActionRequestType actionRequestType;
        try {
            actionRequestType = (ActionRequestType) HibernateUtil.getSession().
                    getNamedQuery("findActionRequestTypeByCode").
                    setString("code", actionRequestTypeCode).
                    uniqueResult();
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return actionRequestType;
    }

    public void makePersistent(ActionRequestType actionRequestType) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(actionRequestType);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(ActionRequestType actionRequestType) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(actionRequestType);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}