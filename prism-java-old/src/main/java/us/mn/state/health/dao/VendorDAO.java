package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Vendor;

import java.util.Collection;

public interface VendorDAO {
    public Collection findAll() throws InfrastructureException;

    //    public Collection findAll(boolean withUnknown) throws InfrastructureException;
    public Collection findByExample(Vendor vendor) throws InfrastructureException;

    public Collection findByNameFirstCharRange(char start, char end) throws InfrastructureException;

    public Collection findVendorByNameLike(String name) throws InfrastructureException;

    public Collection findItemVendorsById(Long itemId) throws InfrastructureException;
    public Collection findVendorWhereNotWithItemId(Long itemId, boolean withUnknown) throws InfrastructureException;

    public Vendor getVendorById(Long id, boolean lock) throws InfrastructureException;

    public Vendor getVendorByOrgCode(String orgCode, boolean lock) throws InfrastructureException;

    public Vendor getVendorByLegacyId(String id) throws InfrastructureException;

    public void makePersistent(Vendor vendor) throws InfrastructureException;

    public void makeTransient(Vendor vendor) throws InfrastructureException;

    Collection findAllAsDTO() throws InfrastructureException;
}
