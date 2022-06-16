
package us.mn.state.health.dao;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Manufacturer;

public interface ManufacturerDAO {
    public Manufacturer findManufacturerByCode(String code) throws InfrastructureException;
    public Collection findAll(boolean withUnknown) throws InfrastructureException; 
    public Collection findByExample(Manufacturer manufacturer) throws InfrastructureException;
    public Manufacturer getManufacturerById(Long id, boolean lock) throws InfrastructureException;
    public void makePersistent(Manufacturer manufacturer) throws InfrastructureException;
    public void makeTransient(Manufacturer manufacturer) throws InfrastructureException; 
}
