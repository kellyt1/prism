package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.BusinessRulesORCL;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;

public class HibernateBusinessRulesORCLDAO extends HibernateDAO implements BusinessRulesORCLDAO{
    public Collection findAll() throws InfrastructureException {
        return super.findAll(BusinessRulesORCL.class);
    }

    public List<BusinessRulesORCL> findByExample(String brType) throws InfrastructureException {
        List<BusinessRulesORCL> businessRuless;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(BusinessRulesORCL.class);

            crit.add(Expression.eq("objectType",brType));
            crit.add(Expression.isNull("endDate"));

            businessRuless = crit.list();

            //businessRuless = crit.add(Example.create(brType)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return businessRuless;
    }


    public void makePersistent(BusinessRulesORCL businessRules) throws InfrastructureException {
        super.makePersistent(businessRules);

    }


    public void makeTransient(BusinessRulesORCL businessRules) throws InfrastructureException {
        super.makeTransient(businessRules);
    }
}
