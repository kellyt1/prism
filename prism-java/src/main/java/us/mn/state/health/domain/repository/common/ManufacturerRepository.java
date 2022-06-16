package us.mn.state.health.domain.repository.common;

import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.common.Manufacturer;

public interface ManufacturerRepository extends DomainRepository {
    public Manufacturer findManufacturerByCode(String code);

    public List<Manufacturer> findAll(boolean withUnknown);

    public Manufacturer getManufacturerById(Long id);

    public void makePersistent(Manufacturer manufacturer);

}
