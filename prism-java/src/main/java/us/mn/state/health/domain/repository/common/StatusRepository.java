package us.mn.state.health.domain.repository.common;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.common.Status;

import java.util.List;

public interface StatusRepository extends DomainRepository {
    List<Status> findAllByStatusTypeCode(String statusTypeCode);

    Status findByStatusTypeCodeAndStatusCode(String statusTypeCode, String statusCode);

    Status getById(Long statusId);

    Status loadById(Long statusId);

    //Status getStatusByStatusCode(String statusCode);
}
