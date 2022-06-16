package us.mn.state.health.builder.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.view.inventory.StockItemActionRequestsForm;

public class StockItemActionRequestsFormBuilder {
    private StockItemActionRequestsForm form;
    private DAOFactory daoFactory;
    private String[] statusCodes;
    private Person evaluator;

    public StockItemActionRequestsFormBuilder(StockItemActionRequestsForm form,
                                              DAOFactory daoFactory, 
                                              String[] statusCodes,
                                              Person evaluator) {
        this.form = form;
        this.daoFactory = daoFactory;
        this.statusCodes = statusCodes;
        this.evaluator = evaluator;
    }

    public void buildStockItemActionRequests(final String orderBy) throws InfrastructureException {
        ArrayList stockItemActionRequests = new ArrayList();
        if(orderBy == null || orderBy.equals("")) {
            for(int i = 0; i < statusCodes.length; i++) {
                String statusCode = statusCodes[i];
                stockItemActionRequests.addAll(daoFactory.getStockItemActionRequestDAO()
                                                         .findByEvaluatorAndStatusCode(evaluator, statusCode));
            }            
        }
        else {
            stockItemActionRequests = new ArrayList(form.getStockItemActionRequests());
        }
        
        Collections.sort((List) stockItemActionRequests, new Comparator() {
            public int compare(Object o1, Object o2) {
                StockItemActionRequest siar1 = (StockItemActionRequest) o1;
                StockItemActionRequest siar2 = (StockItemActionRequest) o2;
                if(StockItem.ICNBR.equals(orderBy)) {
                    if(siar1.getPotentialStockItem().getIcnbr() == null && siar2.getPotentialStockItem().getIcnbr() == null)
                        return 0;
                    if(siar1.getPotentialStockItem().getIcnbr() != null && siar2.getPotentialStockItem().getIcnbr() == null)
                        return 1;
                    if(siar1.getPotentialStockItem().getIcnbr() == null && siar2.getPotentialStockItem().getIcnbr() != null)
                        return -1;
                    return siar1.getPotentialStockItem().getIcnbr().compareTo(siar2.getPotentialStockItem().getIcnbr());
                }

                if(StockItem.DESCRIPTION.equals(orderBy)) return siar1.getPotentialStockItem().getDescription().compareTo(siar2.getPotentialStockItem().getDescription());
                if(StockItemActionRequest.REQUESTED_BY.equals(orderBy)) return siar1.getRequestor().getFirstAndLastName().compareTo(siar2.getRequestor().getFirstAndLastName());
                if(StockItemActionRequest.REQUEST_DATE.equals(orderBy)) return (-1) * siar1.getRequestedDate().compareTo(siar2.getRequestedDate());
                if(ActionRequestType.ACTION_TYPE.equals(orderBy)) return siar1.getActionRequestType().getName().compareTo(siar2.getActionRequestType().getName());
                return 0;
            }
        });
        
        form.setStockItemActionRequests(stockItemActionRequests);
    }
}