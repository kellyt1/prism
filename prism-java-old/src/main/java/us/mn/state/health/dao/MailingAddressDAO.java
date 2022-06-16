package us.mn.state.health.dao;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.MailingAddress;

public interface MailingAddressDAO  {
    public MailingAddress getMailingAddressById(Long mailingAddressId, boolean lock) throws InfrastructureException;
    public Collection findAll() throws InfrastructureException;
    public Collection findBillingAddresses() throws InfrastructureException;
    public Collection findShippingAddresses() throws InfrastructureException;
    public void makePersistent(MailingAddress category) throws InfrastructureException;
    public void makeTransient(MailingAddress category) throws InfrastructureException;
}