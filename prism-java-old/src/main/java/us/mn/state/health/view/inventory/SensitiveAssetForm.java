package us.mn.state.health.view.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.model.purchasing.OrderLineItem;

public class SensitiveAssetForm extends ValidatorForm {
    private String sensitiveAssetId;
    private Item item;
    private String serialNumber;
    private String classCodeId;
    private String cost;
    private String dateReceived;
    private String document;
    private String mapsFund;
    private String vendorId;
    private String MN_ID;
    private String salvageValueCode;
    private String maintAgreementExpirationDate;
    private String maintAgreementPONumber;
    private String warrantyExpirationDate;
    private String contactPersonId;    
    private String facilityId;
    private String orgBudgetId;
    private OrderLineItem orderLineItem;    
    private SensitiveAsset sensitiveAsset;
    private String statusId;
    private String fund;
    private String notes;
    
    private Collection vendors = new HashSet();  
    private Collection facilities = new HashSet();   
    private Collection classCodes = new HashSet();
    private Collection contactPersons = new HashSet();
    private Collection allOrgBudgetsList = new HashSet();
    private Collection ownerOrgBudgets = new ArrayList();
    private Collection statuses = new HashSet();
    private String cmd = "";
    private boolean reset = false;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (reset) {
            sensitiveAssetId=null;
            item = null;
            serialNumber = "";
            classCodeId = "";
            cost = "";
            dateReceived = "";
            document = "";
            mapsFund = "";
            vendorId = "";
            MN_ID = "";
            salvageValueCode = "";
            maintAgreementExpirationDate = "";
            maintAgreementPONumber = "";
            warrantyExpirationDate = "";
            contactPersonId = "";
            facilityId = "";
            orgBudgetId = "";
            statusId = "";
            reset = false;
        }
        cmd="";
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }


    public void setSensitiveAssetId(String sensitiveAssetId) {
        this.sensitiveAssetId = sensitiveAssetId;
    }


    public String getSensitiveAssetId() {
        return sensitiveAssetId;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    public Item getItem() {
        return item;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public String getSerialNumber() {
        return serialNumber;
    }


    public void setClassCodeId(String classCodeId) {
        this.classCodeId = classCodeId;
    }


    public String getClassCodeId() {
        return classCodeId;
    }


    public void setCost(String cost) {
        this.cost = StringUtils.trim(cost);
    }


    public String getCost() {
        return cost;
    }


    public void setDateReceived(String dateReceived) {
        this.dateReceived = StringUtils.trim(dateReceived);
    }


    public String getDateReceived() {
        return dateReceived;
    }


    public void setDocument(String document) {
        this.document = document;
    }


    public String getDocument() {
        return document;
    }


    public void setMapsFund(String mapsFund) {
        this.mapsFund = mapsFund;
    }


    public String getMapsFund() {
        return mapsFund;
    }


    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public String getVendorId() {
        return vendorId;
    }


    public void setMN_ID(String MN_ID) {
        this.MN_ID = MN_ID;
    }


    public String getMN_ID() {
        return MN_ID;
    }


    public void setSalvageValueCode(String salvageValueCode) {
        this.salvageValueCode = salvageValueCode;
    }


    public String getSalvageValueCode() {
        return salvageValueCode;
    }


    public void setMaintAgreementExpirationDate(String maintAgreementExpirationDate) {
        this.maintAgreementExpirationDate = StringUtils.trim(maintAgreementExpirationDate);
    }


    public String getMaintAgreementExpirationDate() {
        return maintAgreementExpirationDate;
    }


    public void setMaintAgreementPONumber(String maintAgreementPONumber) {
        this.maintAgreementPONumber = maintAgreementPONumber;
    }


    public String getMaintAgreementPONumber() {
        return maintAgreementPONumber;
    }


    public void setWarrantyExpirationDate(String warrantyExpirationDate) {
        this.warrantyExpirationDate = StringUtils.trim(warrantyExpirationDate);
    }


    public String getWarrantyExpirationDate() {
        return warrantyExpirationDate;
    }


    public void setContactPersonId(String contactPersonId) {
        this.contactPersonId = contactPersonId;
    }


    public String getContactPersonId() {
        return contactPersonId;
    }


    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }


    public String getFacilityId() {
        return facilityId;
    }


    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }


    public Collection getVendors() {
        return vendors;
    }


    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }


    public Collection getFacilities() {
        return facilities;
    }


    public void setClassCodes(Collection classCodes) {
        this.classCodes = classCodes;
    }


    public Collection getClassCodes() {
        return classCodes;
    }


    public void setContactPersons(Collection contactPersons) {
        this.contactPersons = contactPersons;
    }


    public Collection getContactPersons() {
        return contactPersons;
    }


    public void setAllOrgBudgetsList(Collection allOrgBudgets) {
        this.allOrgBudgetsList = allOrgBudgets;
    }


    public Collection getAllOrgBudgetsList() {
        return allOrgBudgetsList;
    }


    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }


    public void setOrgBudgetId(String orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }


    public String getOrgBudgetId() {
        return orgBudgetId;
    }


    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }


    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }


    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }


    public String getStatusId() {
        return statusId;
    }


    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }


    public Collection getStatuses() {
        return statuses;
    }


    public void setSensitiveAsset(SensitiveAsset sensitiveAsset) {
        this.sensitiveAsset = sensitiveAsset;
    }


    public SensitiveAsset getSensitiveAsset() {
        return sensitiveAsset;
    }
    

    public void addOwnerOrgBudget(OrgBudget orgBdgt) {
        if(this.ownerOrgBudgets != null) {
            this.ownerOrgBudgets.add(orgBdgt);
        }
    }
    
    public void removeOwnerOrgBudget(OrgBudget orgBdgt) {
        if(this.ownerOrgBudgets != null && this.ownerOrgBudgets.contains(orgBdgt)) {
            this.ownerOrgBudgets.remove(orgBdgt);
        }
    }

    public void setOwnerOrgBudgets(Collection ownerOrgBudgets) {
        this.ownerOrgBudgets = ownerOrgBudgets;
    }


    public Collection getOwnerOrgBudgets() {
        return ownerOrgBudgets;
    }


    public void setFund(String fund) {
        this.fund = fund;
    }


    public String getFund() {
        return fund;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}