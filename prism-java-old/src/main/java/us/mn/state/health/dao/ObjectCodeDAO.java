package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.ObjectCode;

public interface ObjectCodeDAO extends DAO{

    ObjectCode findByCode(String code) throws InfrastructureException;
}
