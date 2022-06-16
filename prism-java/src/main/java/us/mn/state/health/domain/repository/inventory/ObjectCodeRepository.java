package us.mn.state.health.domain.repository.inventory;

import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.common.ObjectCode;

public interface ObjectCodeRepository extends DomainRepository {
    ObjectCode findByCode(String code);

    List<ObjectCode> findAll();
}
