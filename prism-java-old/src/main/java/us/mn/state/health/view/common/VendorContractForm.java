package us.mn.state.health.view.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class VendorContractForm  {
    private String vendorContractId;
    private String contractNumber;
    private String startDate;
    private String insertionDate;
    private String endDate;
    private String insertedBy;
    private VendorForm vendorForm;
    private String comments;
    private String deliveryTerms;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getDeliveryTerms() {
        return deliveryTerms;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(String insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public String getEndDate() {
        return endDate;
    }


    public void setVendorContractId(String vendorContractId) {
        this.vendorContractId = vendorContractId;
    }


    public String getVendorContractId() {
        return vendorContractId;
    }


    public void setVendorForm(VendorForm vendorForm) {
        this.vendorForm = vendorForm;
    }


    public VendorForm getVendorForm() {
        return vendorForm;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }

    public String getKey(){
        return this.toString();
    }
}