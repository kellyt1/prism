package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.ClassCode;

public interface ClassCodeDAO {
    public Collection findAll() throws InfrastructureException;
    public ClassCode getClassCodeById(Long classCodeId, boolean lock) throws InfrastructureException;

    ClassCode getClassCodeByCode(String code) throws InfrastructureException;
}