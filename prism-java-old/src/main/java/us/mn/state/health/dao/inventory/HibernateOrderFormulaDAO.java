package us.mn.state.health.dao.inventory;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.OrderFormula;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateOrderFormulaDAO implements OrderFormulaDAO {

	public HibernateOrderFormulaDAO() {
        try{
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie){}		
	}

    public OrderFormula getOrderFormulaById(Long orderFormulaId, boolean lock) throws InfrastructureException {
		Session session = HibernateUtil.getSession();
		OrderFormula orderFormula = null;
		try {
			if (lock) {
				orderFormula = (OrderFormula) session.load(OrderFormula.class, orderFormulaId, LockMode.UPGRADE);
			} 
            else {
				orderFormula = (OrderFormula) session.load(OrderFormula.class, orderFormulaId);
			}
		}  
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return orderFormula;
	}

	public Collection findAll() throws InfrastructureException {
		Collection orderFormulas;
		try {
			orderFormulas = HibernateUtil.getSession().createCriteria(OrderFormula.class).list();
		} 
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return orderFormulas;
	}

	public Collection findByExample(OrderFormula orderFormula) throws InfrastructureException {
		Collection orderFormulas;
		try {
			Criteria crit = HibernateUtil.getSession().createCriteria(OrderFormula.class);
			orderFormulas = crit.add(Example.create(orderFormula)).list();
		} 
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return orderFormulas;
	}
    
    public OrderFormula findByCategoryCode(String categoryCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
		OrderFormula orderFormula = null;
		try {
			orderFormula = (OrderFormula)session.getNamedQuery("findOrderFormulaByCategoryCode")
                                                  .setString("categoryCode", categoryCode)
                                                  .uniqueResult();
		} 
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return orderFormula;
    }

    public void makePersistent(OrderFormula orderFormula) throws InfrastructureException {
		try {
			HibernateUtil.getSession().saveOrUpdate(orderFormula);
		} 
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}

	public void makeTransient(OrderFormula orderFormula) throws InfrastructureException {
		try {
			HibernateUtil.getSession().delete(orderFormula);
		} 
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}
}
