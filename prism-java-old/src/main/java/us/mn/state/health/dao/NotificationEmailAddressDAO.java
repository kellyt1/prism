package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;

import java.util.Collection;
import java.util.List;

public interface NotificationEmailAddressDAO {
    Collection findAll() throws InfrastructureException;

    Collection findByOrgBudgetId(Long orgBudgetId) throws InfrastructureException;

}
