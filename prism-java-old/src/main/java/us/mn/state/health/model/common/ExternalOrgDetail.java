package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import us.mn.state.health.common.util.CollectionUtils;
//HashSet is a good choice for representing sets if you don't care about element ordering.            
//If you care about ordering, LinkedHashSet or TreeSet are better choices. However, LinkedHashSet or TreeSet come with an additional speed and space cost.

public class ExternalOrgDetail implements Serializable, Comparable {
    private Long orgId;
    private Date orgEndDate;
    private Date orgEffectiveDate;
    private String orgDescription;
    private String comments;
    private String webAddress;
    private String orgShortName;
    private String orgName;
    private String upperOrgName; //convenient property defined by formula to make possible unsensitive case 'order by' with Criteria API;
    private Date terminationDate;
    private String terminatedBy;
    private Date insertionDate;
    private String insertedBy;
    private String type;
    private String orgCode;
    private Collection reps = new HashSet();                //a Collection of ExternalOrgDetailRep's 
    private Collection emailAddresses = new HashSet();      //a Collection of ExtOrgDetailEmailAddressp's 
    private Collection mailingAddresses = new HashSet();    //a Collection of ExtOrgDetailMailingAddress's 
    private Collection phones = new HashSet();              //a Collection of ExternalOrgDetailPhone's 
    private Collection faxes = new HashSet();

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
    
    public Date getOrgEndDate() { return orgEndDate; }

    public void setOrgEndDate(Date orgEndDate) { this.orgEndDate = orgEndDate; }

    public Date getOrgEffectiveDate() { return orgEffectiveDate; }

    public void setOrgEffectiveDate(Date orgEffectiveDate) { this.orgEffectiveDate = orgEffectiveDate; }

    public String getOrgDescription() { return orgDescription; }

    public void setOrgDescription(String orgDescription) { this.orgDescription = orgDescription; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public String getWebAddress() { return webAddress; }

    public void setWebAddress(String webAddress) { this.webAddress = webAddress; }

    public String getOrgShortName() { return orgShortName; }

    public void setOrgShortName(String orgShortName) { this.orgShortName = orgShortName; }

    public String getOrgName() { return orgName; }

    public void setOrgName(String orgName) { this.orgName = orgName; }

    public Date getTerminationDate() { return terminationDate; }

    public void setTerminationDate(Date terminationDate) { this.terminationDate = terminationDate; }

    public String getTerminatedBy() { return terminatedBy; }

    public void setTerminatedBy(String terminatedBy) { this.terminatedBy = terminatedBy; }

    public Date getInsertionDate() { return insertionDate; }

    public void setInsertionDate(Date insertionDate) { this.insertionDate = insertionDate; }

    public String getInsertedBy() { return insertedBy; }

    public void setInsertedBy(String insertedBy) { this.insertedBy = insertedBy; }

    public Long getOrgId() { return orgId; }

    public void setOrgId(Long orgId) { this.orgId = orgId; }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalOrgDetail)) {
            return false;
        }
        final ExternalOrgDetail externalOrganization = (ExternalOrgDetail)o;
        if (orgName != null ? !orgName.equals(externalOrganization.orgName) : externalOrganization.orgName != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (orgName != null ? orgName.hashCode() : 0);
        result = 29 * result + (orgName != null ? orgName.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ExternalOrgDetail ('" + getOrgId() + "'), " + "name: '" + getOrgName() + "'"; 
    }

    public int compareTo(Object o) {
        if (o instanceof ExternalOrgDetail) {
            return this.getOrgName().compareTo(((ExternalOrgDetail)o).getOrgName());
        }
        return 0;
    }

    public void setReps(Collection reps) {
        this.reps = reps;
    }


    public Collection getReps() {
        return reps;
    }


    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }


    public String getOrgCode() {
        return orgCode;
    }


    public void setEmailAddresses(Collection emailAddresses) {
        this.emailAddresses = emailAddresses;
    }


    public Collection getEmailAddresses() {
        return emailAddresses;
    }


    public void setMailingAddresses(Collection mailingAddresses) {
        this.mailingAddresses = mailingAddresses;
    }


    public Collection getMailingAddresses() {
        return mailingAddresses;
    }
    
    public EmailAddress getPrimaryEmailAddress() {
        ExtOrgDetailEmailAddress eodem = (ExtOrgDetailEmailAddress)
            CollectionUtils.getObjectFromCollectionById(emailAddresses, new Integer(1), "rank");
        if(eodem != null) {
            return eodem.getEmailAddress();
        }
        return null;
    }
    
    public MailingAddress getPrimaryMailingAddress() {
        ExtOrgDetailMailingAddress eodma = (ExtOrgDetailMailingAddress)
            CollectionUtils.getObjectFromCollectionById(mailingAddresses, new Integer(1), "rank");
        if(eodma != null) {
            return eodma.getMailingAddress();
        }
        return null;
    }
    
    public Person getPrimaryRep() {
        ExternalOrgDetailRep orgRep = (ExternalOrgDetailRep)
            CollectionUtils.getObjectFromCollectionById(reps, new Integer(1), "rank");
        if(orgRep != null) {
            return orgRep.getRep();
        }
        return null;
    }

    public Collection getPhones() {
        return phones;
    }

    public void setPhones(Collection phones) {
        this.phones = phones;
    }

    public Phone getPrimaryPhone(){
        ExtOrgDetailPhone extOrgDetailPhone =
                (ExtOrgDetailPhone) CollectionUtils.getObjectFromCollectionById(phones,new Integer(1),"rank");
        if(extOrgDetailPhone != null){
            return extOrgDetailPhone.getPhone();
        }
        return  null;
    }

    public Collection getFaxes() {
        return faxes;
    }

    public void setFaxes(Collection faxes) {
        this.faxes = faxes;
    }

    public Phone getPrimaryFax(){
        ExtOrgDetailPhone extOrgDetailPhone =
                (ExtOrgDetailPhone) CollectionUtils.getObjectFromCollectionById(faxes,new Integer(1),"rank");
        if(extOrgDetailPhone != null){
            return extOrgDetailPhone.getPhone();
        }
        return  null;
    }

    public String getUpperOrgName() {
        return upperOrgName;
    }

    public void setUpperOrgName(String upperOrgName) {
        this.upperOrgName = upperOrgName;
    }
}
