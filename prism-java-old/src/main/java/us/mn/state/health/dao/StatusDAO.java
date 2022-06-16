package us.mn.state.health.dao;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Status;

import java.util.Collection;

public interface StatusDAO  {
    public Status getStatusById(Long statusId, boolean lock) throws InfrastructureException;
    public Collection findAllByStatusTypeCode(String statusTypeCode) throws InfrastructureException;            
    public Status findByStatusTypeCodeAndStatusCode(String statusTypeCode, String statusCode) throws InfrastructureException;
    public Collection findAllByStatusTypeAndStatusCodes(String statusTypeCode, String[] statusCodes) throws InfrastructureException;
    public Status getStatusByStatusCode(String statusCode) throws InfrastructureException;
}