package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class DeliveryDetailForm extends ValidatorForm {
    private String deliveryDetailId;
    private String organizationId;
    private Collection organizations = new HashSet();
    private String extOrgContactName;
    private String facilityId;
    private Collection facilities = new TreeSet();              //use TreeSet cuz hopefully it will sort for us
    private String recipientId;
    private Collection recipients = new HashSet();
    private String mailingAddressId;
    private Collection mailingAddresses = new ArrayList();        //use ArrayList cuz we want it sorted and Struts <logic:iterate> won't work with a Set
    private String deliverToType = DELIVER_TO_MDH;               //indicates whether the user wants to deliver to a private citizen
    private String cmd = "";
    private String orgNameFirstCharStart = "1";      //the first letter in the grouping of organizations by name
    private String orgNameFirstCharEnd = "Z";        //the last letter in the grouping of organizations by name
    
    public static String DELIVER_TO_MDH = "mdh";
    public static String DELIVER_TO_MDH_INVENTORY = "mdh_inventory";
    public static String DELIVER_TO_CITIZEN = "citizen";
    public static String DELIVER_TO_EXT_ORG = "extOrg";

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        cmd = "";    
        recipientId = "";
        facilityId = "";
        mailingAddressId = "";
        organizationId = "";
        deliverToType = DELIVER_TO_MDH;        
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
    
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setDeliveryDetailId(String deliveryDetailId) {
        this.deliveryDetailId = deliveryDetailId;
    }


    public String getDeliveryDetailId() {
        return deliveryDetailId;
    }
    
    public String getDeliverToType() {
        return deliverToType;
    }
    
    public void setDeliverToType(String deliverToType) {
        this.deliverToType = deliverToType;
    }


    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }


    public String getOrganizationId() {
        return organizationId;
    }


    public void setOrganizations(Collection organizations) {
        this.organizations = organizations;
    }


    public Collection getOrganizations() {
        return organizations;
    }


    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }


    public String getFacilityId() {
        return facilityId;
    }


    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }


    public Collection getFacilities() {
        return facilities;
    }


    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }


    public String getRecipientId() {
        return recipientId;
    }


    public void setRecipients(Collection recipients) {
        this.recipients = recipients;
    }


    public Collection getRecipients() {
        return recipients;
    }


    public void setMailingAddressId(String mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }


    public String getMailingAddressId() {
        return mailingAddressId;
    }


    public void setMailingAddresses(Collection mailingAddresses) {
        this.mailingAddresses = mailingAddresses;
    }


    public Collection getMailingAddresses() {
        return mailingAddresses;
    }


    public void setOrgNameFirstCharStart(String orgNameFirstCharStart) {
        this.orgNameFirstCharStart = orgNameFirstCharStart;
    }


    public String getOrgNameFirstCharStart() {
        return orgNameFirstCharStart;
    }


    public void setOrgNameFirstCharEnd(String orgNameFirstCharEnd) {
        this.orgNameFirstCharEnd = orgNameFirstCharEnd;
    }


    public String getOrgNameFirstCharEnd() {
        return orgNameFirstCharEnd;
    }

    public void setExtOrgContactName(String extOrgContactName) {
        this.extOrgContactName = extOrgContactName;
    }


    public String getExtOrgContactName() {
        return extOrgContactName;
    }
}