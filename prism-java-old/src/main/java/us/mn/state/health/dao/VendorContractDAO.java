package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.VendorContract;

public interface VendorContractDAO {
    public VendorContract getVendorContractById(Long id, boolean lock) throws InfrastructureException;
    public void makePersistent(VendorContract vendor) throws InfrastructureException;
    public void makeTransient(VendorContract vendor) throws InfrastructureException;
}
