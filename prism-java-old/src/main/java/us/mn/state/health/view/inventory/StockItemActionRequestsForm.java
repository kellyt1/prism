package us.mn.state.health.view.inventory;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;

public class StockItemActionRequestsForm extends ValidatorActionForm {

    private Collection stockItemActionRequests = new ArrayList();

    public void setStockItemActionRequests(Collection stockItemActionRequests) {
        this.stockItemActionRequests = stockItemActionRequests;
    }

    public Collection getStockItemActionRequests() {
        return stockItemActionRequests;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
}
