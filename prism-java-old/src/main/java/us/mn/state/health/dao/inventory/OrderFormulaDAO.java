
package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.OrderFormula;

public interface OrderFormulaDAO { 
    public Collection findAll() throws InfrastructureException; 
    public Collection findByExample(OrderFormula orderFormula) throws InfrastructureException;
    public OrderFormula findByCategoryCode(String categoryCode) throws InfrastructureException;
    public OrderFormula getOrderFormulaById(Long id, boolean lock) throws InfrastructureException;
    public void makePersistent(OrderFormula item) throws InfrastructureException;
    public void makeTransient(OrderFormula item) throws InfrastructureException;    
}
