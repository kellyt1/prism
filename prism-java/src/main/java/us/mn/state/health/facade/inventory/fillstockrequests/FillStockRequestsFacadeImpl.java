package us.mn.state.health.facade.inventory.fillstockrequests;

import java.util.ArrayList;
import java.util.List;

import us.mn.state.health.domain.repository.common.CategoryRepository;
import us.mn.state.health.domain.repository.common.PriorityRepository;
import us.mn.state.health.domain.repository.common.StatusRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.inventory.StockItemFacilityRepository;
import us.mn.state.health.domain.repository.materialsrequest.RequestRepository;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.util.PagedQueryResultEnhanced;

public class FillStockRequestsFacadeImpl implements FillStockRequestsFacade {
    private UserRepository userRepository;
    private PriorityRepository priorityRepository;
    private CategoryRepository categoryRepository;
    private StatusRepository statusRepository;
    private StockItemFacilityRepository stockItemFacilityRepository;
    private RequestRepository requestRepository;

    public FillStockRequestsFacadeImpl(UserRepository userRepository
            , PriorityRepository priorityRepository
            , CategoryRepository categoryRepository
            , StatusRepository statusRepository
            , StockItemFacilityRepository stockItemFacilityRepository
            , RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.priorityRepository = priorityRepository;
        this.categoryRepository = categoryRepository;
        this.statusRepository = statusRepository;
        this.stockItemFacilityRepository = stockItemFacilityRepository;
        this.requestRepository = requestRepository;
    }

    public FillStockRequestsDropDownListsDTO getDropDownLists() {
        List statuses = new ArrayList();
        statuses.add(statusRepository.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_DISPERSAL));
        statuses.add(statusRepository.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.FULFILLED));
        return new FillStockRequestsDropDownListsDTO(priorityRepository.findAllPriorities(),
                statuses,
                userRepository.findAllUsers(),
                categoryRepository.findAll(false),
                stockItemFacilityRepository.findAll());
    }

    public PagedQueryResultEnhanced advancedSearchOpenRequests(RequestSearchCriteria criteria) {
        PagedQueryResultEnhanced queryResultEnhanced = requestRepository.findAllRequests(criteria);
        for (Object o : queryResultEnhanced.getPageList()) {
            //initialize properties used in toString()
            ((Request) o).getDeliverToInfoAsString();
            ((Request) o).getRequestor().getFirstAndLastName();
            ((Request) o).getPriority().getPriorityCode();
            for (Object rli : ((Request) o).getRequestLineItems()) {

                ((RequestLineItem) rli).getItemDescription();
                if (((RequestLineItem) rli).getItem() != null) {
                    ((RequestLineItem) rli).getItem().getDescription();
                }
                ((RequestLineItem) rli).getStatus().getName();
                ((RequestLineItem) rli).getNotes().size();
                ((RequestLineItem) rli).getFundingSrcSummary().toString();
            }
        }

        return queryResultEnhanced;
    }
}
