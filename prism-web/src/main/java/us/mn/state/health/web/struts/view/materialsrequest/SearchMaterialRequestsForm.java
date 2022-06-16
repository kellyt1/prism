package us.mn.state.health.web.struts.view.materialsrequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.validator.ValidatorForm;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.DelegatingActionUtils;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;

public class SearchMaterialRequestsForm extends ValidatorForm {
    private RequestSearchCriteria requestSearchCriteria = new RequestSearchCriteria();
    private String trackingNumber;
    private List requests = new ArrayList();

    private WebApplicationContext webApplicationContext;

    public void setServlet(ActionServlet actionServlet) {
        super.setServlet(actionServlet);
        if (webApplicationContext==null && actionServlet != null) {
			this.webApplicationContext = DelegatingActionUtils.findRequiredWebApplicationContext(actionServlet, null);
		}
    }

    public RequestSearchCriteria getRequestSearchCriteria() {
        return requestSearchCriteria;
    }

    public void setRequestSearchCriteria(RequestSearchCriteria requestSearchCriteria) {
        this.requestSearchCriteria = requestSearchCriteria;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List getRequests() {
        return requests;
    }

    public void setRequests(List requests) {
        this.requests = requests;
    }

}
