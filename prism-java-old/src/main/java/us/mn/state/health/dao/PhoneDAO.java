package us.mn.state.health.dao;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Phone;

public interface PhoneDAO {
     public Collection findAll() throws InfrastructureException;
    public Collection findByExample(Phone phone) throws InfrastructureException;
    public Phone getPhoneById(Long phoneId, boolean lock) throws InfrastructureException;
    public void makePersistent(Phone phone) throws InfrastructureException;
    public Phone getPhoneByNumber(String phoneNumber) throws InfrastructureException;
    public void makeTransient(Phone phone) throws InfrastructureException;

}
