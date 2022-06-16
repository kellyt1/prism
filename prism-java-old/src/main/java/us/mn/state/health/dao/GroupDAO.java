package us.mn.state.health.dao;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Group;

public interface GroupDAO { 
    public Collection findAll() throws InfrastructureException; 
    public Collection findByExample(Group group) throws InfrastructureException;
    public Group findGroupByName(String groupName) throws InfrastructureException;
    public Group findGroupByCode(String groupCode) throws InfrastructureException;
    public Collection findGroupsByPersonId(Long personId) throws InfrastructureException;
    public Group getGroupById(Long groupId, boolean lock) throws InfrastructureException;
    public void makePersistent(Group group) throws InfrastructureException;
    public void makeTransient(Group group) throws InfrastructureException;
}
