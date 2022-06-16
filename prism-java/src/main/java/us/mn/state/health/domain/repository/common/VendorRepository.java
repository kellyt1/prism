package us.mn.state.health.domain.repository.common;

import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.common.Vendor;

public interface VendorRepository extends DomainRepository {
    List<Vendor> findAll();

    Vendor getById(Long vendorId);
}
