package us.mn.state.health.dao;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Facility;

public interface FacilityDAO  {
    public Facility getFacilityById(Long facilityId, boolean lock) throws InfrastructureException;
    public Collection findAllFacilities() throws InfrastructureException;
    public Collection findFacilitiesByType(String facilityType) throws InfrastructureException;
}