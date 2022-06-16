package us.mn.state.health.dao.inventory;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.ActionRequestType;

public interface ActionRequestTypeDAO  {

    public Collection findAll() throws InfrastructureException;
    
    public ActionRequestType findByActionRequestTypeCode(String actionRequestTypeCode) throws InfrastructureException;
    
}