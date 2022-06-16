package us.mn.state.health.builder.reporting;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.view.reporting.GeneralReportingForm;

public class GeneralReportingFormBuilder {
    private GeneralReportingForm form;
    private DAOFactory daoFactory;

    public GeneralReportingFormBuilder(GeneralReportingForm form, DAOFactory daoFactory) {
        this.form = form;
        this.daoFactory = daoFactory;
    }

    public void buildFacilities() throws InfrastructureException {
        Collection facilities = daoFactory.getFacilityDAO().findFacilitiesByType(Facility.TYPE_BUILDING);
        form.setFacilities(facilities);
    }
}
