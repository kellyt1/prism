package us.mn.state.health.model.purchasing;

import java.text.SimpleDateFormat;

import groovy.util.logging.Log4j;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.FiscalYears;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.ModelMember;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorAccount;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.model.util.search.EntityIndex;
import us.mn.state.health.model.util.search.OrderIndex;

import javax.persistence.Transient;

@Log4j
public class Order extends ModelMember implements Comparable {
    private Long orderId;
    private Collection orderLineItems = new ArrayList();
    private Person purchaser;
    private String remitToAddress;
    private String vendorInstructions;
    private MailingAddress billToAddress;
    private MailingAddress vendorAddress;
    private String otherBillToAddress;
    private MailingAddress shipToAddress;
    private String otherShipToAddress;
    private int version;
    private Vendor vendor;
    private String purchaseOrderNumber;
    private String purchaseOrderNumberType;
    private VendorContract vendorContract;
    private VendorAccount vendorAccount;
    private Collection requestLineItems = new HashSet();
    private Collection orderNotes = new HashSet();
    private Date suspenseDate;
//    private Date lastUpdated = new Date();

    public static final String PO_NBR_TYPE_MAPS = "MAPS";
    public static final String PO_NBR_TYPE_CREDIT = "CREDIT";
    public static final String PO_NBR_TYPE_NOCHARGE = "NO_CHARGE";
    public static final String PO_NBR_TYPE_SWIFT = "SWIFT";


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Collection getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(Collection orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Person getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(Person purchaser) {
        this.purchaser = purchaser;
    }

    public String getRemitToAddress() {
        return remitToAddress;
    }

    public void setRemitToAddress(String remitToAddress) {
        this.remitToAddress = remitToAddress;
    }

    public String getVendorInstructions() {
        return vendorInstructions;
    }

    public void setVendorInstructions(String vendorInstructions) {
        this.vendorInstructions = vendorInstructions;
    }

    public String getOtherBillToAddress() {
        return otherBillToAddress;
    }

    public void setOtherBillToAddress(String otherBillToAddress) {
        this.otherBillToAddress = otherBillToAddress;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setBillToAddress(MailingAddress billToAddress) {
        this.billToAddress = billToAddress;
    }

    public MailingAddress getBillToAddress() {
        return billToAddress;
    }

    public void setVendorContract(VendorContract vendorContract) {
        this.vendorContract = vendorContract;
    }

    public VendorContract getVendorContract() {
        return vendorContract;
    }

    public void setRequestLineItems(Collection requestLineItems) {
        this.requestLineItems = requestLineItems;
    }

    public Collection getRequestLineItems() {
        return requestLineItems;
    }

    public EntityIndex getEntityIndex() throws InfrastructureException {
        return new OrderIndex();
    }

    public void setVendorAddress(MailingAddress vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public MailingAddress getVendorAddress() {
        return vendorAddress;
    }

    public void setOrderNotes(Collection orderNotes) {
        this.orderNotes = orderNotes;
    }

    public Collection getOrderNotes() {
        return orderNotes;
    }

    public void setSuspenseDate(Date suspenseDate) {
        this.suspenseDate = suspenseDate;
    }

    public Date getSuspenseDate() {
        return suspenseDate;
    }

    public void setShipToAddress(MailingAddress shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public MailingAddress getShipToAddress() {
        return shipToAddress;
    }

    public void setOtherShipToAddress(String otherShipToAddress) {
        this.otherShipToAddress = otherShipToAddress;
    }

    public String getOtherShipToAddress() {
        return otherShipToAddress;
    }

    public void setPurchaseOrderNumberType(String purchaseOrderNumberType) {
        this.purchaseOrderNumberType = purchaseOrderNumberType;
    }

    public String getPurchaseOrderNumberType() {
        return purchaseOrderNumberType;
    }

    public void setVendorAccount(VendorAccount vendorAccount) {
        this.vendorAccount = vendorAccount;
    }

    public VendorAccount getVendorAccount() {
        return vendorAccount;
    }

//    Date getLastUpdated() {
//        return this.lastUpdated;
//    }
//
//    void setLastUpdated(Date lastUpdated) {
//        this.lastUpdated = new Date()
//    }

    @Transient
    public Double getBuyUnitsTotalCost() {
        double total = 0d;
        for (Object orderLineItem : orderLineItems) {
            OrderLineItem oli = (OrderLineItem) orderLineItem;
            if (oli != null) {
                total += oli.getBuyUnitTotalCost();
            }
        }
        return total;
    }

    @Transient
    public String getOrderFiscalYear_2Digits() {
        String outFiscalYear = getOrderFiscalYear().substring(2, 4);
        if (orderLineItems != null && orderLineItems.size() > 0) {
            Iterator rlit = orderLineItems.iterator();
            OrderLineItem oli = (OrderLineItem) rlit.next();
            if (oli != null) {
                RequestLineItem rqi = oli.getRequestLineItem();
                if (rqi != null) {
                    for (RequestLineItemFundingSource aa : rqi.getFundingSources()) {
                        if (aa.getOrgBudget().getEndDate() != null) {
                            outFiscalYear = new SimpleDateFormat("yy").format(aa.getOrgBudget().getEndDate());
                        }
                    }
                }
            }
        }
        return outFiscalYear;
    }

    @Transient
    public String getOrderFiscalYear() {
        String outYear = "";
        FiscalYears fiscalYearById;
        try {
            fiscalYearById = DAOFactory?.getDAOFactory(DAOFactory.DEFAULT)?.getFiscalYearsDAO()?.getFiscalYearById(0l, false);

            if (fiscalYearById != null) {
                outYear = fiscalYearById.getFiscalyear().toString();
            }
        } catch (InfrastructureException ignore) {
            outYear = "????";
        }
        return outYear;
    }

    public void removeOrderLineItem(OrderLineItem oli) {
        orderLineItems.remove(oli);
        oli.setOrder(null);
    }

    /**
     * 03/07/2006 - Lucian Ochian & Shawn Flahave - reimplemented this method
     * properly (we hope!)
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) return false;
        final Order that = (Order) o;

        if (this.getOrderId() == null) {
            if (that.getOrderId() == null) {
                //we'll probably never get in here...
                //dig deeper, using comparison by value
                if (this.getPurchaseOrderNumber() != null && !this.getPurchaseOrderNumber().equals(that.getPurchaseOrderNumber())) {
                    return false;
                }
                if (this.getVendor() != null && !this.getVendor().equals(that.getVendor())) {
                    return false;
                }
                if (this.getSuspenseDate() != null && !this.getSuspenseDate().equals(that.getSuspenseDate())) {
                    return false;
                }
                return true;
            } else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        } else {  //we know we can't get a NullPointerException now...
            return this.getOrderId().equals(that.getOrderId());
        }
    }

    public int hashCode() {
        return 13 + (29 * (getInsertionDate() != null ? getInsertionDate().hashCode() : 0));
    }

    public int compareTo(Object o) {
        return (o instanceof Order) ? this.getOrderId().compareTo(((Order) o).getOrderId()) : 0;
    }
}
