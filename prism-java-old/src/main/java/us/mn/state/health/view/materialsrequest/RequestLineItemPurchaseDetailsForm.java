package us.mn.state.health.view.materialsrequest;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;

public class RequestLineItemPurchaseDetailsForm extends ValidatorForm {
    private String requestLineItemId;
    private RequestLineItem requestLineItem;
    private Order order;

    public RequestLineItem getRequestLineItem() {
        return requestLineItem;
    }

    public void setRequestLineItem(RequestLineItem requestLineItem) {
        this.requestLineItem = requestLineItem;
    }

    public String getRequestLineItemId() {
        return requestLineItemId;
    }

    public void setRequestLineItemId(String requestLineItemId) {
        this.requestLineItemId = requestLineItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
