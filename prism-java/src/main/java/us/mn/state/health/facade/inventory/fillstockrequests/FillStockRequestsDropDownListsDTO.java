package us.mn.state.health.facade.inventory.fillstockrequests;

import java.util.List;

import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Priority;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.StockItemFacility;

public class FillStockRequestsDropDownListsDTO {
    List<Priority> priorities;
    List<Status> statuses;
    List<User> requestors;
    List<Category> categories;
    List<StockItemFacility> stockItemFacilities;

    public FillStockRequestsDropDownListsDTO(List<Priority> priorities, List<Status> statuses, List<User> requestors, List<Category> categories, List<StockItemFacility> stockItemFacilities) {
        this.priorities = priorities;
        this.statuses = statuses;
        this.requestors = requestors;
        this.categories = categories;
        this.stockItemFacilities = stockItemFacilities;
    }

    public List<Priority> getPriorities() {
        return priorities;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public List<User> getRequestors() {
        return requestors;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<StockItemFacility> getStockItemFacilities() {
        return stockItemFacilities;
    }
}
