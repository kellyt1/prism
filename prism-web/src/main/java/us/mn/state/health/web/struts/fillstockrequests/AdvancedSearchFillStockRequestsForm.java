package us.mn.state.health.web.struts.fillstockrequests;

import org.apache.struts.action.ActionForm;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;

public class AdvancedSearchFillStockRequestsForm extends ActionForm {
    private RequestSearchCriteria searchCriteria = new RequestSearchCriteria();

    public RequestSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(RequestSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
