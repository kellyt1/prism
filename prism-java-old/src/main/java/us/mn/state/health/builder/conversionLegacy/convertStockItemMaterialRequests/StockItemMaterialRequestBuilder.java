package us.mn.state.health.builder.conversionLegacy.convertStockItemMaterialRequests;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Priority;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.legacySystem.stockItemMaterialRequests.OrdId;
import us.mn.state.health.model.legacySystem.stockItemMaterialRequests.OrdItem;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;

public class StockItemMaterialRequestBuilder {
    private OrdId ordId;
    private DAOFactory factory;
    private Request request;

    public StockItemMaterialRequestBuilder(DAOFactory factory, OrdId ordId, Request request) {
        this.factory = factory;
        this.ordId = ordId;
        this.request = request;
    }

    public void buildDateRequested() {
        Date dateRequested = new Date(ordId.getDateIn().getTime());
        request.setDateRequested(dateRequested);
    }

    public void buildPriority() throws InfrastructureException {
        Priority priority = factory.getPriorityDAO().findByPriorityCode(Priority.NORMAL);
        request.setPriority(priority);
    }

    public void buildTrackingNumber() throws InfrastructureException {
        request.assignTrackingNumber();
    }

    public void buildAdditionalInstructions() {
        /*
        Add Special
        Add BNBR
        if(lit=f){
            Add Contact+Room+Initial
        }
        else {
            Add contact data
        }
          */

        StringBuffer additionalInstructions = new StringBuffer();
        additionalInstructions.append("Special: " + ordId.getSpecial());
        additionalInstructions.append("\n");
        additionalInstructions.append("BNBR: " + ordId.getBnbr());
        additionalInstructions.append("\n");

        if (ordId.getLit()) {
            additionalInstructions.append("Client: " + ordId.getClient());
        }
        else {
            additionalInstructions
                    .append("Contact:" + ordId.getContact()
                    + " Room:" + ordId.getRoom()
                    + " Initial:" + ordId.getInitial());
        }
        request.setAdditionalInstructions(additionalInstructions.toString());
    }

    public void buildRequestor() {
        //empty?
    }

    public void buildRequestLineItems() throws InfrastructureException {
        Collection ordItems = ordId.getRequestLineItems();
        Set requestLineItems = new HashSet();

        for (Iterator iterator = ordItems.iterator(); iterator.hasNext();) {
            OrdItem ordItem = (OrdItem) iterator.next();
            RequestLineItem requestLineItem = new RequestLineItem();
            requestLineItem.setItem(getStockItemByLegacyICNBR(ordItem.getIcnbr()));
            requestLineItem.setRequest(request);
            requestLineItem.setQuantity(new Integer(Double.valueOf(ordItem.getQuantity()).intValue()));
            Status status = factory.getStatusDAO()
                    .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,Status.WAITING_FOR_DISPERSAL);
            requestLineItem.setStatus(status);
            requestLineItem.setApprovalRequired(false);
        }
        request.setRequestLineItems(requestLineItems);
    }

    /**
     * method that retrieves the stock item using the legacy format of icnbr(xxx-xxxx)
     * @param icnbr
     * @return
     */
    private StockItem getStockItemByLegacyICNBR(String icnbr) throws InfrastructureException {
        StockItem stockItem = null;
        String categoryCode = icnbr.substring(0,icnbr.indexOf("-"));
        String itemCode = icnbr.substring(icnbr.indexOf("-")+1);
        stockItem = factory.getStockItemDAO().findStockItemByCategoryCodeAndItemCode(categoryCode, itemCode);
        return stockItem;
    }
}
