package us.mn.state.health.domain.repository.common;

import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.common.Hazardous;

public interface HazardousRepository extends DomainRepository {
    List<Hazardous> findAll();

    Hazardous getById(Long hazardousId);

    Hazardous loadById(Long hazardousId);
}
