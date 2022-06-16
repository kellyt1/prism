package us.mn.state.health.model.inventory;

import org.hibernate.search.annotations.*;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.util.search.AssetIndex;
import us.mn.state.health.model.util.search.EntityIndex;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

@Entity
@Indexed(index = "assetIndex")
public class SensitiveAsset implements Serializable, Comparable {
    public static final String CONCATENATED_CONTENT = "concatenatedContent";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_CODE = "categoryCode";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String ID = "id";
    public static final String CLASSNAME = "classname";
    public static final String MODEL = "model";
    public static final String MANUFACTURER_NAME = "manufacturerName";
    public static final String MANUFACTURER_ID = "manufacturerId";
    public static final String VENDOR_NAME = "vendorName";
    public static final String VENDOR_ID = "vendorId";
    public static final String STATUS_CODE = "statusCode";
    public static final String ORG_BUDGET_IDS = "orgBudgetIds";
    public static final String FACILITY_ID = "facilityId";
    public static final String FACILITY_NAME = "facilityName";
    public static final String CONTACT_PERSON_NAME = "contactPersonName";
    public static final String CONTACT_PERSON_ID = "contactPersonId";
    public static final String SERIAL_NUMBER = "serialNumber";
    public static final String ASSET_NUMBER = "assetNumber";
    public static final String CLASS_CODE = "classCode";
    public static final String COST = "cost";
    public static final String DATE_RECEIVED = "dateReceived";
    public static final String MAINT_AGMT_EXP_DATE = "maintAgreementExpirationDate";
    public static final String MAINT_AGMT_PO_NBR = "maintAgreementPONumber";
    public static final String WARRANTY_EXP_DATE = "warrantyExpirationDate";
    public static final String ORDER_NBR = "orderNbr";
    public static final DateFormat formatter = new SimpleDateFormat("yyyyMMdd");


    private Long sensitiveAssetId;
    private Status status;  
    private String serialNumber;
    private ClassCode classCode;                //this is where we'll get the 'life' property, per Bruce Brokaw
    private Double cost;                        //THIS WILL USUALLY COME INDIRECTLY VIA ORDER_LINE_ITEM ???
    private Date dateReceived;                  //THIS WILL USUALLY COME INDIRECTLY VIA ORDER_LINE_ITEM
    private String document;
    private Vendor vendor;
    private VendorContract vendorContract;
    private Collection<OrgBudget> ownerOrgBudgets = new HashSet<OrgBudget>();
    private Date maintAgreementExpirationDate;
    private String maintAgreementPONumber;
    private Date warrantyExpirationDate;
    private Person contactPerson;
    private Facility facility;
    private Item item;                        //THIS WILL USUALLY COME INDIRECTLY VIA ORDER_LINE_ITEM
    private Date insertionDate;
    private String insertedBy;
    private Date changeDate;
    private String changedBy;
    private OrderLineItem orderLineItem;        //for reference, mostly.      
    private String notes;
    private String fund;

    public SensitiveAsset() {}

    public ClassCode getClassCode() {
        return classCode;
    }

    public void setClassCode(ClassCode classCode) {
        this.classCode = classCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        
        if (!(o instanceof SensitiveAsset)) return false;
        final SensitiveAsset that = (SensitiveAsset)o;
        
        if(this.getSensitiveAssetId() == null) {
            if(that.getSensitiveAssetId() == null) {
                //dig deeper, using comparison by value 
                if(this.getItem() != null && !this.getItem().equals(that.getItem())) {
                    return false;
                }  
                if(this.getSerialNumber() != null && !getSerialNumber().equals(that.getSerialNumber())) {
                    return false;
                }  
                if(this.getInsertionDate() != null && !getInsertionDate().equals(that.getInsertionDate())) {
                    return false;
                }
              
                return true;
            }
            else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        }
        else {  //we know we can't get a NullPointerException now... 
            return this.getSensitiveAssetId().equals(that.getSensitiveAssetId());
        }
    }

    public int hashCode() {
        int result = 13;
        result += 29 * (getItem() != null ? getItem().hashCode() : 0);
        result += 29 * (getInsertionDate() != null ? getInsertionDate().hashCode() : 0);
        return result;
    }

    public String toString() {
        return "\n" + "Sensitive Asset ID: ('" + getSensitiveAssetId() + "'), " +
                      "Serial Number: '" + getSerialNumber();
    }

    public int compareTo(Object o) {
        if(o instanceof SensitiveAsset) {
            return this.getSerialNumber().compareTo(((SensitiveAsset)o).getSerialNumber());
        }
        return 0;
    }

    public void setSensitiveAssetId(Long sensitiveAssetId) {
        this.sensitiveAssetId = sensitiveAssetId;
    }

    @Id
    @DocumentId (name = ID)
    public Long getSensitiveAssetId() {
        return sensitiveAssetId;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Field(name = SERIAL_NUMBER, index = Index.TOKENIZED, store = Store.YES)
    public String getSerialNumber() {
        return serialNumber;
    }
    

    public void setCost(Double cost) {
        this.cost = cost;
    }


    public Double getCost() {
        return cost;
    }


    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }


    public Date getDateReceived() {
        return dateReceived;
    }


    public void setDocument(String document) {
        this.document = document;
    }


    public String getDocument() {
        return document;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }


    public Vendor getVendor() {
        return vendor;
    }


    public void setVendorContract(VendorContract vendorContract) {
        this.vendorContract = vendorContract;
    }


    public VendorContract getVendorContract() {
        return vendorContract;
    }


    public void setMaintAgreementExpirationDate(Date maintAgreementExpirationDate) {
        this.maintAgreementExpirationDate = maintAgreementExpirationDate;
    }


    public Date getMaintAgreementExpirationDate() {
        return maintAgreementExpirationDate;
    }


    public void setMaintAgreementPONumber(String maintAgreementPONumber) {
        this.maintAgreementPONumber = maintAgreementPONumber;
    }


    public String getMaintAgreementPONumber() {
        return maintAgreementPONumber;
    }


    public void setWarrantyExpirationDate(Date warrantyExpirationDate) {
        this.warrantyExpirationDate = warrantyExpirationDate;
    }


    public Date getWarrantyExpirationDate() {
        return warrantyExpirationDate;
    }


    public void setContactPerson(Person contactPerson) {
        this.contactPerson = contactPerson;
    }


    public Person getContactPerson() {
        return contactPerson;
    }


    public void setFacility(Facility facility) {
        this.facility = facility;
    }


    public Facility getFacility() {
        return facility;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    public Item getItem() {
        return item;
    }


    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }


    public Date getInsertionDate() {
        return insertionDate;
    }


    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }


    public String getInsertedBy() {
        return insertedBy;
    }


    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }


    public Date getChangeDate() {
        return changeDate;
    }


    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }


    public String getChangedBy() {
        return changedBy;
    }


    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }


    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }
    
    public void addOwnerOrgBudget(OrgBudget orgBdgt) {
        if(this.ownerOrgBudgets != null) {
            this.ownerOrgBudgets.add(orgBdgt);
        }
    }

    public void setOwnerOrgBudgets(Collection<OrgBudget> ownerOrgBudgets) {
        this.ownerOrgBudgets = ownerOrgBudgets;
    }


    public Collection<OrgBudget> getOwnerOrgBudgets() {
        return ownerOrgBudgets;
    }


    public void setStatus(Status status) {
        this.status = status;
    }


    public Status getStatus() {
        return status;
    }
    
    public EntityIndex getEntityIndex() throws InfrastructureException {
        return new AssetIndex();   
    }


    public void setFund(String fund) {
        this.fund = fund;
    }


    public String getFund() {
        return fund;
    }


    @Field(name = ID, index = Index.UN_TOKENIZED, store = Store.YES)
    public String getSensitiveAssetIdAsString() {
        return this.getSensitiveAssetId().toString();
    }

    @Field(name = CLASSNAME, index = Index.TOKENIZED, store = Store.YES)
    public String getClassName(){
        return this.getClass().getName();
    }

    @Field(name = DESCRIPTION, index = Index.TOKENIZED, store = Store.YES)
    @Boost(2.3f)
    public String getItemDescription(){
        return this.getItem().getDescription();
    }

    @Field(name = CATEGORY_CODE, index = Index.TOKENIZED, store = Store.YES)
    public String getItemCategoryCode() {
        return this.getItem().getCategory().getCategoryCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Field(name = CATEGORY_NAME, index = Index.TOKENIZED, store = Store.YES)
    public String getItemCategoryName() {
        return this.getItem().getCategory().getName();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Field(name = MODEL, index = Index.TOKENIZED, store = Store.YES)
    public String getItemModel() {
        return this.getItem().getModel();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Field(name = MANUFACTURER_NAME, index = Index.TOKENIZED, store = Store.YES)
    public String getItemManufacturerName() {
        return this.getItem().getManufacturer().getExternalOrgDetail().getOrgName();
    }

    @Field(name = MANUFACTURER_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getItemManufacturerId() {
        return this.getItem().getManufacturer().getManufacturerId().toString();
    }

    @Field(name = VENDOR_NAME, index = Index.TOKENIZED, store = Store.YES)
    public String getVendorName() {
        if (this.getVendor() != null) {
            return this.getVendor().getExternalOrgDetail().getOrgName();
        }
        else {
            return null;
        }
    }

    @Field(name = VENDOR_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getVendorId() {
        if (this.getVendor() != null) {
            return this.getVendor().getVendorId().toString();
        }
        else {
            return null;
        }
    }

    @Field(name = FACILITY_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getFacilityId() {
        if (this.getFacility() != null) {
            return this.getFacility().getFacilityId().toString();
        }
        else {
            return null;
        }
    }

    @Field(name = FACILITY_NAME, index = Index.TOKENIZED, store = Store.YES)
    public String getFacilityName() {
        if (this.getFacility() != null) {
            return this.getFacility().getFacilityName();
        }
        else {
            return null;
        }
    }

    @Field(name = ORDER_NBR, index = Index.TOKENIZED, store = Store.YES)
    public String getOrderIdAsString() {
        if (this.getOrderLineItem() != null) {
            return this.getOrderLineItem().getOrder().getOrderId().toString();
        }
        else {
            return null;
        }
    }

    @Field(name = STATUS_CODE, index = Index.TOKENIZED, store = Store.YES)
    public String getStatusCode() {
        if (this.getStatus() != null) {
            return this.getStatus().getStatusCode();
        }
        else {
            return null;
        }
    }

    public String getStatusName() {
        if (this.getStatus() != null) {
            return this.getStatus().getName();
        }
        else {
            return null;
        }
    }


    @Field(name = CLASS_CODE, index = Index.TOKENIZED, store = Store.YES)
    public String getClassCodeValue() {
        if (this.getClassCode() != null) {
            return this.getClassCode().getClassCodeValue();
        }
        else {
            return null;
        }
    }

    @Field(name = CONTACT_PERSON_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getContactPersonIdAsString() {
        if (this.getContactPerson() != null) {
            return this.getContactPerson().getPersonId().toString();
        }
        else {
            return null;
        }
    }

    @Field(name = CONTACT_PERSON_NAME, index = Index.TOKENIZED, store = Store.YES)
    public String getContactPersonFirstAndLastName() {
        if (this.getContactPerson() != null) {
            return this.getContactPerson().getFirstAndLastName();
        }
        else {
            return null;
        }
    }

    @Field(name = ORG_BUDGET_IDS, index = Index.TOKENIZED, store = Store.YES)
    public String getOrgBudgetIdsAsString() {
        if (this.getOwnerOrgBudgets() != null && this.getOwnerOrgBudgets().size() > 0) {
            Iterator iter = this.getOwnerOrgBudgets().iterator();
            StringBuffer ids = new StringBuffer();
            while (iter.hasNext()) {
                OrgBudget orgBdgt = (OrgBudget) iter.next();
                ids.append(orgBdgt.getOrgBudgetId());
            }
            return ids.toString();
        }
        else {
                return null;
        }
    }

    @Field(name = DATE_RECEIVED, index = Index.TOKENIZED, store = Store.YES)
    public String getDateReceivedFormatted() {
        if (this.getDateReceived() != null) {
            return formatter.format(this.getDateReceived());
        }
        else {
            return null;
        }
    }

    @Field(name = CONCATENATED_CONTENT, index = Index.TOKENIZED, store = Store.YES)
    private String getConcatenatedContent()  {
//    public String getConcatenatedFields()  {
            StringBuffer concatenatedFields = new StringBuffer("");

                if (this.getItemDescription()  != null) {
                    concatenatedFields.append(this.getItemDescription()).append(" ");
                }
                if (this.getItemCategoryName() != null) {
                    concatenatedFields.append(this.getItemCategoryName()).append(" ");
                }
                if (this.getItemManufacturerName() != null) {
                    concatenatedFields.append(this.getItemManufacturerName()).append(" ");
                }
                if (this.getVendorName() != null) {
                    concatenatedFields.append(this.getVendorName()).append(" ");
                }
                if (this.getFacilityName() != null) {
                    concatenatedFields.append(this.getFacilityName()).append(" ");
                }
                if (this.getOrderIdAsString() != null) {
                    concatenatedFields.append(this.getOrderIdAsString()).append(" ");
                }
                if (this.getSerialNumber() != null) {
                    concatenatedFields.append(this.getSerialNumber()).append(" ");
                }
                if (this.getStatus() != null) {
                    concatenatedFields.append(this.getStatusName()).append(" ");
                }
                if (this.getClassCode() != null) {
                    concatenatedFields.append(this.getClassCodeValue()).append(" ");
                }
                if (this.getContactPerson() != null) {
                    concatenatedFields.append(this.getContactPersonFirstAndLastName()).append(" ");
                }


                return concatenatedFields.toString();
            }

}