package us.mn.state.health.web.struts.searchorders;

import us.mn.state.health.domain.repository.purchasing.OrderSearchCriteria;
import org.apache.struts.action.ActionForm;

public class SearchOrdersForm extends ActionForm {
    private OrderSearchCriteria searchCriteria = new OrderSearchCriteria();


    public OrderSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(OrderSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
