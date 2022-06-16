package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.VendorAccount;

public interface VendorAccountDAO {
    public VendorAccount getVendorAccountById(Long id, boolean lock) throws InfrastructureException;
    public void makePersistent(VendorAccount vendorAccount) throws InfrastructureException;
    public void makeTransient(VendorAccount vendorAccount) throws InfrastructureException;
}
