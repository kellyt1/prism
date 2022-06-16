package us.mn.state.health.dao;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;

public interface DAO  {

    public Object loadById(Long id, Class clazz, boolean lock) throws InfrastructureException;

    public Object getById(Long id, Class clazz) throws InfrastructureException;
    
    public Collection findAll(Class clazz) throws InfrastructureException;

    public void makePersistent(Object entity) throws InfrastructureException;
    
    public void makeTransient(Object entity) throws InfrastructureException;
    
}