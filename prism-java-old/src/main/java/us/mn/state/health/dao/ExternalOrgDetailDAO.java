package us.mn.state.health.dao;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.ExternalOrgDetail;

public interface ExternalOrgDetailDAO { 
    public Collection findAll() throws InfrastructureException; 
    public Collection findAll(boolean excludeVendors) throws InfrastructureException; 
    public Collection findByExample(ExternalOrgDetail externalOrgDetail) throws InfrastructureException;
    public ExternalOrgDetail findByOrgName(String name) throws InfrastructureException;
    public Collection findByNameFirstCharRange(char start, char end) throws InfrastructureException;
    public Collection findByNameFirstCharRange(char start, char end, boolean excludeVendors) throws InfrastructureException;
    public ExternalOrgDetail getExternalOrgDetailById(Long externalOrgDetailId, boolean lock) throws InfrastructureException;
    public void makePersistent(ExternalOrgDetail externalOrgDetail) throws InfrastructureException;
    public void makeTransient(ExternalOrgDetail externalOrgDetail) throws InfrastructureException;
}
