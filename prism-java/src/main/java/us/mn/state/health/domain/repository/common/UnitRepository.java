package us.mn.state.health.domain.repository.common;

import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.inventory.Unit;

public interface UnitRepository extends DomainRepository {
    public static final String ORDER_BY_CODE = "code";
    public static final String ORDER_BY_NAME = "name";

    public Unit getUnitById(Long unitId);

    public Unit loadUnitById(Long unitId);

    public List findAll(boolean withUnknown);

    public Unit findUnitByCode(String code);

    void makePersistent(Unit unit);

    List<Unit> findAll(boolean withUnknown, String[] orderBy);
}
