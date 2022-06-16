package us.mn.state.health.builder.conversionLegacy.convertPA_ADDS;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Priority;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.legacySystem.PA_ADDS.PA_ADD;
import us.mn.state.health.model.legacySystem.inventory.Vendr;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.util.Utilities;

public class RequestBuilder {
    private static Log log = LogFactory.getLog(RequestBuilder.class);
    private Request request;
    private PA_ADD pa_add;
    private DAOFactory daoFactory;
    

    public RequestBuilder(Request request, PA_ADD pa_add, DAOFactory daoFactory) {
        this.request = request;
        this.pa_add = pa_add;
        this.daoFactory = daoFactory;
    }

    public void buildTrackingNumber() throws InfrastructureException {
        request.assignTrackingNumber();
    }

    public void buildDateRequested() {
        Timestamp timestamp = pa_add.getDateIn();
        Date dateRequested;
        if (timestamp != null) {
            dateRequested = new Date(pa_add.getDateIn().getTime());
        }
        else {
            dateRequested = new Date();
        }

        request.setDateRequested(dateRequested);
    }

    public void buildRequestor() throws InfrastructureException {
        Collection requestor = daoFactory.getUserDAO().findByExample(getPersonLike(pa_add.getMemo()));
        if (requestor.size() == 1) {
            request.setRequestor((User) requestor.iterator().next());
        }
    }

    public void buildPriority() throws InfrastructureException {
        String priorityString = pa_add.getPriority().trim();
        Priority priority;
        if(!priorityString.equalsIgnoreCase("N")){
            priority = daoFactory.getPriorityDAO().findByPriorityCode(Priority.HIGH);
        }
        else {
            priority = daoFactory.getPriorityDAO().findByPriorityCode(Priority.NORMAL);
        }
        request.setPriority(priority);
    }

    public void buildAdditionalInstructions() {
        String instructions = pa_add.getMemo() + "; " + pa_add.getMemo2();
        request.setAdditionalInstructions(instructions);
    }

    public void buildNeedByDate() {
        Date needByDate=null;
        String memo = pa_add.getMemo();
        String needByDateString = memo.substring(memo.indexOf("NB:") + 3, memo.indexOf("SV:")).trim();
        int length = needByDateString.length();
        
        try {
            if(length > 8) {
                needByDate = DateUtils.createDate(needByDateString, DateUtils.DEFAULT_DATE_FORMAT);
            }
            else {
                needByDate = DateUtils.createDate(needByDateString, DateUtils.DATE_FORMAT_2DIGITYEAR);
            }
        } 
        catch(ParseException e) {
            log.error("Couldn't get needByDate");
        }

        request.setNeedByDate(needByDate);
    }

    public void buildRequestLineItems() throws InfrastructureException {
        RequestLineItem requestLineItem = new RequestLineItem();
        //We add the unit to the description
        requestLineItem.setItemDescription(pa_add.getDescription());

        //set the unit
        Unit unit = null;
        String unitString = pa_add.getUnit();
        String decodedUnitCode = Utilities.decodeUnitCode(unitString);
        if(!decodedUnitCode.equals(Unit.CODE_UNKNOWN)){
            unit = daoFactory.getUnitDAO().findUnitByCode(decodedUnitCode);
        }
        if(unit!=null){
            requestLineItem.setUnit(unit);
        }
        else {
            requestLineItem.setItemDescription(pa_add.getDescription() + ";  Unit : " + pa_add.getUnit());
        }

//        set the total cost
        Double itemCost = new Double(pa_add.getTotalCost());
        requestLineItem.setItemCost(itemCost);
        Integer quantity = new Integer(Double.valueOf(pa_add.getQuantity()).intValue());

//        set the qty
        requestLineItem.setQuantity(quantity);

//        set the vendorCatalogId
        requestLineItem.setSuggestedVendorCatalogNumber(pa_add.getVendorCatalogId());

//        set the status of the RLI
        Status status = daoFactory
                .getStatusDAO()
                .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_PURCHASE);
        requestLineItem.setStatus(status);

//        set the ammount to dollars
        requestLineItem.setAmountInDollars(Boolean.TRUE);

//        set the RLIFundingSource
        OrgBudget orgBudget;
        orgBudget = daoFactory.getOrgBudgetDAO().findByOrgBudgetCodeAndLastFiscalYear(pa_add.getOrgBudgetCode());

        if (orgBudget!=null) {
            RequestLineItemFundingSource fundingSource =
                    new RequestLineItemFundingSource(orgBudget
                            , new Double(itemCost.doubleValue()*quantity.doubleValue())
                            , requestLineItem);
            requestLineItem.addFundingSource(fundingSource);
        }

//        set the suggested vendor
        buildVendor(requestLineItem);
        requestLineItem.setRequest(request);
        request.addRequestLineItem(requestLineItem);

    }

    private Person getPersonLike(String memo) {
        String beginContact = memo.substring(memo.indexOf("C:") + 2).trim();
        Person result = new Person();
        StringTokenizer tokenizer = new StringTokenizer(beginContact);
        result.setFirstName(tokenizer.nextToken());
        result.setLastName(tokenizer.nextToken());
        return result;
    }

    private void buildVendor(RequestLineItem requestLineItem) throws InfrastructureException {
        String vendorId = pa_add.getVIDNBR();
        String memo = pa_add.getMemo();
        if (Vendr.UNKNOWN_VENDR_ID.equals(vendorId)) {
            String suggestedVendorName = getVendorName(memo);
            requestLineItem.setSuggestedVendorName(suggestedVendorName);
        }
        /*
        else {
            Vendor vendor = daoFactory.getVendorDAO().getVendorByLegacyId(vendorId);
            if (vendor != null) {
                requestLineItem.setSuggestedVendor(vendor);
            }
            else {
                requestLineItem.setSuggestedVendorName(getVendorName(memo));
            }
        }
        */
    }

    private String getVendorName(String memo) {
        String suggestedVendorName = memo.substring(memo.indexOf("SV:") + 3, memo.indexOf("C:")).trim();
        return suggestedVendorName;
    }

}
