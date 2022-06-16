package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.Unit;

public interface UnitDAO {
    public Unit getUnitById(Long unitId, boolean lock) throws InfrastructureException;

    public Collection findAll(boolean withUnknown) throws InfrastructureException;

    public Unit findUnitByCode(String code) throws InfrastructureException;

    void makePersistent(Unit unit) throws InfrastructureException;

    void makeTransient(Unit unit) throws InfrastructureException;
}