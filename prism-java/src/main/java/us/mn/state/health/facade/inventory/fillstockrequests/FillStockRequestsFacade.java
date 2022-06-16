package us.mn.state.health.facade.inventory.fillstockrequests;

import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.util.PagedQueryResultEnhanced;

public interface FillStockRequestsFacade {
    FillStockRequestsDropDownListsDTO getDropDownLists();

    PagedQueryResultEnhanced advancedSearchOpenRequests(RequestSearchCriteria criteria);
}
