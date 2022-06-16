package us.mn.state.health.model.inventory;

//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Blob;
//import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.search.annotations.*;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.inventory.OrderFormulaDAO;
import us.mn.state.health.dao.inventory.StockItemDAO;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.util.search.EntityIndex;
import us.mn.state.health.model.util.search.StockItemIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Entity
@Indexed(index = "stockItemIndex")
public class StockItem extends Item implements Comparable {
    private Integer icnbr = new Integer(0);
    private Integer qtyOnHand;
    private Set<StockItemLocation> locations = new LinkedHashSet<StockItemLocation>();
    private Integer reorderPoint = new Integer(0);
    private Integer reorderQty = new Integer(0);
    /**
     * @deprecated use cycleCountPriorityObject instead
     */
    private String cycleCountPriority = "Unknown";
    private CycleCountPriority cycleCountPriorityObject;
    private Boolean fillUntilDepleted;
    /**
     * @deprecated this property is useless  - use reviewDate instead
     */
    private Date reorderDate;
    private Date holdUntilDate;
    /**
     * @deprecated use dispenseUnitperBuyUnit in ItemVendor
     */
    private Integer packQty = new Integer(0);
    private Boolean staggeredDelivery;              //We need to clarify where this really belongs
    private Boolean seasonal;
    private String printFileURL;
    private Status status;
    private Person secondaryContact;
    private Person primaryContact;
    private OrgBudget orgBudget;
//
//  The attached file uploading functionality has been moved to the
//  parent class (Item)
//
//    private String printSpecFileName;
//    private byte[] printSpecFileBinaryValue;

    private Boolean potentialIndicator; //if Y, this is not a 'real' stock item; its a potential that needs to be approved
    private Person asstDivDirector;
    private String instructions;
    private Collection<StockQtyAdjustmentHistory> siQtyAdjustments = new HashSet<StockQtyAdjustmentHistory>();    //collection of StockQtyAdjustmentHistory objects; historical info
    private Boolean isOnOrder;
    private Integer safetyStock;
    private Date reviewDate;
    private Set<StockItemLot> lots = new LinkedHashSet<StockItemLot>();

    public static final String ICNBR = "icnbr";
    public static final String UNIT = "unit";
    public static final String DESCRIPTION = "desc";
    public static final String ORGANIZATION = "org";
    public static final String PRIMARY_CONTACT = "pcontact";
    public static final String SECONDARY_CONTACT = "scontact";
    public static final String USAGE = "usage";

//    public static final String ICNBR = "icnbr";
    public static final String FULL_ICNBR = "fullIcnbr";
    public static final String STATUS = "status";
    public static final String STATUS_NAME = "statusName";
    public static final String SEASONAL = "seasonal";
    public static final String PRIMARY_CONTACT_IND = "primaryContact";
    public static final String PRIMARY_CONTACT_ID = "primaryContactId";
    public static final String SECONDARY_CONTACT_IND = "secondaryContact";
    public static final String SECONDARY_CONTACT_ID = "secondaryContactID";
    public static final String ORG_BUDGET = "orgBudget";
    public static final String ORG_BUDGET_ENDDATE = "orgBudgetEndDate";
    public static final String ORG_BUDGET_ID = "orgBudgetId";
    public static final String CONCATENATED_CONTENT = "concatenatedContent";
    public static final String DESCRIPTION_IND = "description";
    public static final String CATEGORY_CODE = "categoryCode";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String ID = "id";
    public static final String CLASSNAME = "classname";
    public static final String HAZARDOUS = "hazardous";
    public static final String DISPENSE_UNIT = "dispenseUnit";
    public static final String MODEL = "model";
    public static final String MANUFACTURER = "manufacturer";
    public static final String MANUFACTURER_ID = "manufacturerId";
    public static final String VENDOR = "vendor";
    public static final String VENDOR_ID = "vendorId";
    public static final String ANNUAL_USAGE = "annualUsage";

    private static Log log = LogFactory.getLog(StockItem.class);

    /**
     * No-arg constructor for JavaBean tools.
     */
    public StockItem() {
    }

    /**
     * This method overwrites this stock item's properties with the ones passed in.
     * It does NOT, however, overwrite this stock item's ID.
     * It also does not handle properties that are persistent collections, such as
     * ItemVendor, Location, etc..., because those are relationships and we don't yet
     * have a good strategy for dealing with that.
     * <p/>
     * TODO: figure out how to get the locations
     *
     * @param copyFromSI
     */
    public void copyProperties(StockItem copyFromSI) throws InfrastructureException {
//        Long id = this.getItemId(); //store the id cuz PropertyUtils is possibly gonna try to change it
//        try{
//            PropertyUtils.copyProperties(this, copyFromSI);
//        }
//        catch(ReflectivePropertyException e){
//        //Hibernate definitely throws an exception here because we're changing
//        //the ID of this stock item via copyProperties().  throwing the exception
//        //creates too much overhead, so we'll just consume it  here and do nothing.
////            e.printStackTrace();
////            throw new InfrastructureException(e);
//        }

//        this.setItemId(id);     //make sure the id was not changed
        this.setAnnualUsage(copyFromSI.getAnnualUsage());
        this.setAsstDivDirector(copyFromSI.getAsstDivDirector());
        this.setCategory(copyFromSI.getCategory());
        this.setCycleCountPriority(copyFromSI.getCycleCountPriority());
        this.setDescription(copyFromSI.getDescription());
        this.setDispenseUnit(copyFromSI.getDispenseUnit());
        this.setDispenseUnitCost(copyFromSI.getDispenseUnitCost());
        this.setEconomicOrderQty(copyFromSI.getEconomicOrderQty());
        this.setEstimatedAnnualUsage(copyFromSI.getEstimatedAnnualUsage());
        this.setFillUntilDepleted(copyFromSI.getFillUntilDepleted());
        this.setHazardous(copyFromSI.getHazardous());
        this.setHoldUntilDate(copyFromSI.getHoldUntilDate());
        this.setIcnbr(copyFromSI.getIcnbr());
        this.setInstructions(copyFromSI.getInstructions());
        this.setManufacturer(copyFromSI.getManufacturer());
        this.setModel(copyFromSI.getModel());
        this.setObjectCode(copyFromSI.getObjectCode());
        this.setOrgBudget(copyFromSI.getOrgBudget());
        this.setPackQty(copyFromSI.getPackQty());
        this.setPrimaryContact(copyFromSI.getPrimaryContact());
        this.setPrintFileURL(copyFromSI.getPrintFileURL());
//        this.setPrintSpecFileBinaryValue(copyFromSI.getPrintSpecFileBinaryValue());
//        this.setPrintSpecFileName(copyFromSI.getPrintSpecFileName());
        this.setQtyOnHand(copyFromSI.getQtyOnHand());
        this.setReorderDate(copyFromSI.getReorderDate());
        this.setReorderPoint(copyFromSI.getReorderPoint());
        this.setReorderQty(copyFromSI.getReorderQty());
        this.setSeasonal(copyFromSI.getSeasonal());
        this.setSecondaryContact(copyFromSI.getSecondaryContact());
        this.setStaggeredDelivery(copyFromSI.getStaggeredDelivery());
        this.setStatus(copyFromSI.getStatus());
        this.setVersion(copyFromSI.getVersion());
//        copyLocations(copyFromSI.getLocations());
//        this.setAttachedFiles(copyFromSI.getAttachedFiles());
    }

    public void addLocation(StockItemLocation loc) {
        loc.setStockItem(this);
        locations.add(loc);
    }

    /**
     * calculates the suggested Reorder Point for this stock item.
     * SuggestedROQ = the lesser of suggested EOQ and annual usage
     *
     * @return suggestedROQ
     */
    public Integer calculateSuggestedROQ() throws InfrastructureException {
        int eoq = this.calculateSuggestedEOQ().intValue();
        int usage = 0;

        if (getAnnualUsage() != null) {
            usage = getAnnualUsage().intValue();
        }

        return new Integer(Math.min(eoq, usage));
    }

    public Integer getSuggestedROQ() {
        try {
            return this.calculateSuggestedROQ();
        }
        catch (InfrastructureException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * calculates the suggested Reorder Point for this stock item, in terms of dispenseUnits.
     * SuggestedROP = (lead time in days for items in this category) * (this item's annual usage)
     *
     * @return suggestedROP
     */
    public Integer calculateSuggestedROP() throws InfrastructureException {
        OrderFormulaDAO orderFormulaDAO = daoFactory.getOrderFormulaDAO();
        Integer result;
        try {
            OrderFormula orderFormula = orderFormulaDAO.findByCategoryCode(this.getCategory().getCategoryCode());

            double leadTimeDays = orderFormula.getLeadTimeDays().intValue() / 365;
            int usage = 0;

            if (getAnnualUsage() != null) {
                usage = getAnnualUsage().intValue();
            }

            Long calculation = new Long(Math.round(leadTimeDays * usage));


            result = new Integer(calculation.intValue());
        } catch (Exception e) {
            result = new Integer(-1);
        }
        return result;
    }

    public Integer getSuggestedROP() {
        try {
            return this.calculateSuggestedROP();
        }
        catch (InfrastructureException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reorder() {
    }

    /**
     * Assign an ICNBR to a (hopefully new only) stock item
     * An ICNBR should be unique among all other stock items
     *
     * @throws us.mn.state.health.common.exceptions.BusinessException
     *
     */
    public void assignICNBR() throws BusinessException, InfrastructureException {
        Integer nextICNBR = null;
        if (this.getCategory() != null) {
            nextICNBR = daoFactory.getStockItemDAO().findNextICNBR();
            this.setIcnbr(nextICNBR);
        } else {
            throw new BusinessException("Tried to assign an ICNBR to an un-categorized StockItem.");
        }
    }

    public void delete() throws InfrastructureException {
        StockItemDAO stockItemDAO = daoFactory.getStockItemDAO();
        if (this.getItemId() != null) {  //can't delete an already transient instance
            stockItemDAO.makeTransient(this);
        }
    }

    public void save() throws InfrastructureException {
        StockItemDAO stockItemDAO = daoFactory.getStockItemDAO();
        stockItemDAO.makePersistent(this);
    }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Item)) return false;

        final Item that = (Item) o;

        if (this.getItemId() == null) {
            if (that.getItemId() == null) {
                //dig deeper, using comparison by value
                if (!that.getItemType().equals(Item.STOCK_ITEM)) {
                    return false;
                }
                if (this.getDescription() != null && !this.getDescription().equals(that.getDescription())) {
                    return false;
                }

                return true;
            } else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        } else {  //we know we can't get a NullPointerException now...
            return this.getItemId().equals(that.getItemId());
        }
    }

    /**
     * We're a little concerned with this implementation, but we think its better
     * than doing something like return 0; because of the potential performance hit that way.
     * None of these properties we are hashing are immutable or unique, but together they
     * should be strong enough for our needs.
     *
     * @return the hashCode
     */
    public int hashCode() {
        int result;
        result = (getDescription() != null ? getDescription().hashCode() : 0);
        result = 29 * result + (getInsertionDate() != null ? getInsertionDate().hashCode() : 0);
        result = 29 * result + (this.icnbr != null ? this.icnbr.intValue() : 0);
        return result;
    }

    public int compareTo(Object o) {
        if (o instanceof StockItem) {
            return this.getDescription().compareTo(((StockItem) o).getDescription());
        }
        return 0;
    }

    public EntityIndex getEntityIndex() throws InfrastructureException {
        return new StockItemIndex();
    }

    // *********************************** Trivial Getters/Setters **************************************//
    /**
     * @return
     * @deprecated
     */
    public String getCycleCountPriority() {
        return cycleCountPriority;
    }

    /**
     * @deprecated
     */
    public void setCycleCountPriority(String cycleCountPriority) {
        this.cycleCountPriority = cycleCountPriority;
    }


    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(Integer safetyStock) {
        this.safetyStock = safetyStock;
    }

    public CycleCountPriority getCycleCountPriorityObject() {
        return cycleCountPriorityObject;
    }

    public void setCycleCountPriorityObject(CycleCountPriority cycleCountPriorityObject) {
        this.cycleCountPriorityObject = cycleCountPriorityObject;
    }

    public Boolean getFillUntilDepleted() {
        return fillUntilDepleted;
    }

    public void setFillUntilDepleted(Boolean fillUntilDepleted) {
        this.fillUntilDepleted = fillUntilDepleted;
    }

    public Date getHoldUntilDate() {
        return holdUntilDate;
    }

    public void setHoldUntilDate(Date holdUntilDate) {
        this.holdUntilDate = holdUntilDate;
    }

    public Integer getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(Integer icnbr) {
        this.icnbr = icnbr;
    }

    public OrgBudget getOrgBudget() {
        return orgBudget;
    }

    public void setOrgBudget(OrgBudget orgBudget) {
        this.orgBudget = orgBudget;
    }

    /**
     * @return
     * @deprecated
     */
    public Integer getPackQty() {
        return packQty;
    }

    /**
     * @param packQty
     * @deprecated
     */
    public void setPackQty(Integer packQty) {
        this.packQty = packQty;
    }

    public Boolean getPotentialIndicator() {
        return potentialIndicator;
    }

    public void setPotentialIndicator(Boolean potentialIndicator) {
        this.potentialIndicator = potentialIndicator;
    }

    public Person getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(Person primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getPrintFileURL() {
        return printFileURL;
    }

    public void setPrintFileURL(String printFileURL) {
        this.printFileURL = printFileURL;
    }

//
//  The attached file uploading functionality has been moved to the
//  parent class (Item)
//

//
//    public byte[] getPrintSpecFileBinaryValue() {
//        return printSpecFileBinaryValue;
//    }
//
//    public void setPrintSpecFileBinaryValue(byte[] printSpecFileBinaryValue) {
//        this.printSpecFileBinaryValue = printSpecFileBinaryValue;
//    }
//
//    public String getPrintSpecFileName() {
//        return printSpecFileName;
//    }
//
//    public void setPrintSpecFileName(String printSpecFileName) {
//        this.printSpecFileName = printSpecFileName;
//    }

    public Integer getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(Integer qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    /**
     * @return
     * @deprecated
     */
    public Date getReorderDate() {
        return reorderDate;
    }

    /**
     * @param reorderDate
     * @deprecated
     */
    public void setReorderDate(Date reorderDate) {
        this.reorderDate = reorderDate;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Integer getReorderQty() {
        return reorderQty;
    }

    public void setReorderQty(Integer reorderQty) {
        this.reorderQty = reorderQty;
    }

    public Boolean getSeasonal() {
        return seasonal;
    }

    public void setSeasonal(Boolean seasonal) {
        this.seasonal = seasonal;
    }

    public Person getSecondaryContact() {
        return secondaryContact;
    }

    public void setSecondaryContact(Person secondaryContact) {
        this.secondaryContact = secondaryContact;
    }

    public Boolean getStaggeredDelivery() {
        return staggeredDelivery;
    }

    public void setStaggeredDelivery(Boolean staggeredDelivery) {
        this.staggeredDelivery = staggeredDelivery;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getAsstDivDirector() {
        return asstDivDirector;
    }

    public void setAsstDivDirector(Person asstDivDirector) {
        this.asstDivDirector = asstDivDirector;
    }

    public void setLocations(Set<StockItemLocation> locations) {
        this.locations = locations;
    }

    public Set<StockItemLocation> getLocations() {
        Set result = new LinkedHashSet();
        for (StockItemLocation location : locations) {
            if (location.getEndDate() != null && new Date().after(location.getEndDate())) {
                continue;
            }
            result.add(location);
        }
        return Collections.unmodifiableSet(result);
    }

    public String getLocationCodesAsString() {
        StringBuffer val = new StringBuffer();
        String lineBreak = "";
        for (Iterator<StockItemLocation> iter = locations.iterator(); iter.hasNext();) {
            StockItemLocation loc = iter.next();
            if (loc.getLocationCode() != null) {
                val.append(lineBreak);
               val.append(loc.getLocationCode());
                lineBreak = "\n";
            }
        }
        return val.toString();
    }

    public String getFacilityAndLocationCodesAsString() {
        String locations = "";
        for (Iterator<StockItemLocation> iterator1 = getLocations().iterator(); iterator1.hasNext();) {
            StockItemLocation stockItemLocation = iterator1.next();
            locations += stockItemLocation.getFacility().getFacilityName() + "-" + stockItemLocation.getLocationCode();
            if (iterator1.hasNext()) {
                locations += ";";
            }
        }
        return locations;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * This is just a helper method that returns the
     * ICNBR in this format: CategoryCode-ICNBR
     * It's used to display the ICNBR in the jsp's
     *
     * @return a string that is the full ICNBR (xxx-xxxx)
     */
    @Field(name = FULL_ICNBR, index = Index.TOKENIZED, store = Store.YES)
    public String getFullIcnbr() {
        if (getIcnbr() != null && getCategory() != null) {
            NumberFormat numberFormat = new DecimalFormat("0000");
            String icnbrFormat = numberFormat.format(icnbr);
            return getCategory().getCategoryCode() + "-" + icnbrFormat;
        } else {
            return "n/a";
        }
    }

    public void setSiQtyAdjustments(Collection<StockQtyAdjustmentHistory> siQtyAdjustments) {
        this.siQtyAdjustments = siQtyAdjustments;
    }


    public Collection<StockQtyAdjustmentHistory> getSiQtyAdjustments() {
        return siQtyAdjustments;
    }


    public void setIsOnOrder(Boolean isOnOrder) {
        this.isOnOrder = isOnOrder;
    }


    public Boolean getIsOnOrder() {
        return isOnOrder;
    }

//    public String toString() {
//        return "\n" + "StockItem: id=" + getItemId() + "; ICNBR=" + icnbr + "; " + getDescription()+
//                " DispenseUnit: '" + getDispenseUnit().getName() + "'" + ":" + this.getClass().getName();
//    }
    /*
    public String toString() {
        return "StockItem{" +
                "asstDivDirector=" + asstDivDirector +
                ", icnbr=" + icnbr +
                ", qtyOnHand=" + qtyOnHand +
                ", locations=" + locations +
                ", reorderPoint=" + reorderPoint +
                ", reorderQty=" + reorderQty +
                ", cycleCountPriority='" + cycleCountPriority + "'" +
                ", fillUntilDepleted=" + fillUntilDepleted +
                ", reorderDate=" + reorderDate +
                ", holdUntilDate=" + holdUntilDate +
                ", packQty=" + packQty +
                ", staggeredDelivery=" + staggeredDelivery +
                ", seasonal=" + seasonal +
                ", printFileURL='" + printFileURL + "'" +
                ", status=" + status +
                ", secondaryContact=" + secondaryContact +
                ", primaryContact=" + primaryContact +
                ", orgBudget=" + orgBudget +
//                ", printSpecFileName='" + printSpecFileName + "'" +
//                ", printSpecFileBinaryValue=" + printSpecFileBinaryValue +
                ", potentialIndicator=" + potentialIndicator +
                ", instructions='" + instructions + "'" +
                "}";
    }*/

    public Boolean getDiscontinued() {
        try {
            String statusCode = this.getStatus().getStatusCode();
            boolean b1 = statusCode.equalsIgnoreCase(Status.INACTIVE)
                    && (!this.getFillUntilDepleted().booleanValue()
                    || (this.getQtyOnHand().intValue() - this.getCurrentDemand().intValue()) <= 0);
            boolean b2 = statusCode.equalsIgnoreCase(Status.ONHOLD)
                    && (!this.getFillUntilDepleted().booleanValue()
                    || (this.getQtyOnHand().intValue() - this.getCurrentDemand().intValue()) <= 0);

            if (b1 || b2) {
                return Boolean.TRUE;
            }

            return Boolean.FALSE;
        }
        catch (Exception e) {
            log.error("Error " + e);
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

//
//  The attached file uploading functionality has been moved to the
//  parent class (Item)
//
    /**
     * Don't invoke this.  Used by Hibernate only.
     */
//    public void setPrintSpecFileBinaryValueHibernate(Blob imageBlob) {
//        this.printSpecFileBinaryValue = this.toByteArray(imageBlob);
//    }


    /**
     * Don't invoke this.  Used by Hibernate only.
     */
//    public Blob getPrintSpecFileBinaryValueHibernate() {
//        byte[] printSpecFileBinaryValue = this.printSpecFileBinaryValue;
//        if (printSpecFileBinaryValue == null) {
//            return null;
//        }
//        return Hibernate.createBlob(printSpecFileBinaryValue);
//    }
//
//    private byte[] toByteArray(Blob fromBlob) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            return toByteArrayImpl(fromBlob, baos);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (baos != null) {
//                try {
//                    baos.close();
//                } catch (IOException ex) {
//                }
//            }
//        }
//    }

//    private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos)
//            throws SQLException, IOException {
//        byte[] buf = new byte[4000];
//        if (fromBlob == null) {
//            return null;
//        }
//        InputStream is = fromBlob.getBinaryStream();
//        try {
//            for (; ;) {
//                int dataSize = is.read(buf);
//
//                if (dataSize == -1)
//                    break;
//                baos.write(buf, 0, dataSize);
//            }
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException ex) {
//                }
//            }
//        }
//        return baos.toByteArray();
//    }


//    @Field(index = Index.TOKENIZED, store = Store.YES)
//    private String getConcatenatedContent() {
//        return getDescription() + " " + " abc";
//    }

    public StockItemLocation getPrimaryStockItemLocation() {
        for (StockItemLocation stockItemLocation : getLocations()) {
            if (stockItemLocation.getIsPrimary() != null && stockItemLocation.getIsPrimary()) return stockItemLocation;
        }
        return null;
    }

    public Set<StockItemLot> getLots() {
        Set result = new LinkedHashSet();
        for (StockItemLot lot : lots) {
            if (lot.getEndDate() != null && new Date().after(lot.getEndDate())) {
                continue;
            }
            result.add(lot);
        }
        return Collections.unmodifiableSet(result);
    }

    public void setLots(Set<StockItemLot> lots) {
        this.lots = lots;
    }

    public void addLot(StockItemLot lot) {
        lots.add(lot);
    }

    public String getItemType() {
        return STOCK_ITEM;
    }

    @Field(name = CATEGORY_CODE, index = Index.TOKENIZED, store = Store.YES)
    public String getCategoryCode() {
        return this.getCategory().getCategoryCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Field(name = CATEGORY_NAME, index = Index.TOKENIZED, store = Store.YES)
    public String getCategoryName() {
        return this.getCategory().getName();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Field(name = MANUFACTURER, index = Index.TOKENIZED, store = Store.YES)                  
    public String getManufacturerName() {
        return this.getManufacturer().getExternalOrgDetail().getOrgName();
    }

    @Field(name = MANUFACTURER_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getManufacturerId() {
        return this.getManufacturer().getManufacturerId().toString();
    }

    @Field(name = HAZARDOUS, index = Index.TOKENIZED, store = Store.YES)
    public String getHazardousName() {
        return this.getHazardous().toString();
    }

    @Field(name = PRIMARY_CONTACT_IND, index = Index.TOKENIZED, store = Store.YES)
    public String getPrimaryContactName() {
        return this.getPrimaryContact().getFirstAndLastName();
    }

    @Field(name = PRIMARY_CONTACT_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getPrimaryContactID() {
        return this.getPrimaryContact().getPersonId().toString();
    }

    @Field(name = SECONDARY_CONTACT_IND, index = Index.TOKENIZED, store = Store.YES)
    public String getSecondaryContactName() {
        return this.getSecondaryContact().getFirstAndLastName();
    }

    @Field(name = SECONDARY_CONTACT_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getSecondaryContactID() {
        return this.getSecondaryContact().getPersonId().toString();
    }

    @Field(name = ORG_BUDGET, index = Index.NO, store = Store.YES)
    public String getOrgBudgetCodeAndName() {
        return this.getOrgBudget().getOrgBudgetCodeAndName();
    }

    @Field(name = ORG_BUDGET_ENDDATE, index = Index.NO, store = Store.YES)
    public String getOrgBudgetEndDate() {
        return DateUtils.toString(this.getOrgBudget().getEndDate(), "yyyyMMdd");
    }

    @Field(name = ORG_BUDGET_ID, index = Index.TOKENIZED, store = Store.YES)
    public String getOrgBudgetId() {
        return this.getOrgBudget().getOrgBudgetId().toString();
    }

    @Field(name = STATUS, index = Index.TOKENIZED, store = Store.YES)
    public String getStatusCode() {
        return this.getStatus().getStatusCode();
    }

    @Field(name = STATUS_NAME, index = Index.TOKENIZED, store = Store.YES)
    public String getStatusName() {
        return this.getStatus().getName();
    }

    @Field(name = DISPENSE_UNIT, index = Index.TOKENIZED, store = Store.YES)
    public String getDispenseUnitName() {
        return this.getDispenseUnit().getName();
    }


    @Field(name = SEASONAL, index = Index.TOKENIZED, store = Store.YES)
    public String getSeasonalAsString() {
        return this.getSeasonal().toString();
    }


    @Field(name = DESCRIPTION_IND, index = Index.TOKENIZED, store = Store.YES)
    @Boost(2.3f)
    public String getDescription() {
        return super.getDescription();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Field(name = ICNBR, index = Index.TOKENIZED, store = Store.YES)
    public String getIcnbrString(){
        if (this.getIcnbr() != null) {
          return StringUtils.formatStringNumber(this.getIcnbr().toString(), 4);
        }
        else return "";
    }

    @Field(name = ANNUAL_USAGE, index = Index.TOKENIZED, store = Store.YES)
    public String getAnnualUsageAsString() {
        return this.getAnnualUsage() != null ? this.getAnnualUsage().toString():"0";
    }

    /**
     * This is just a helper method that returns the
     * ICNBR in this format: CategoryCodeICNBR
     * It's used to search for full ICNBR without having
     * to enter the hyphen or dash
     *
     * @return a string that is the full ICNBR (cccxxxx)
     */
     public String getFullIcnbrWithoutDash() {
        if (getIcnbr() != null && getCategory() != null) {
            NumberFormat numberFormat = new DecimalFormat("0000");
            String icnbrFormat = numberFormat.format(icnbr);
            return getCategory().getCategoryCode() + icnbrFormat;
        } else {
            return "n/a";
        }
    }

    @Field(name = CLASSNAME, index = Index.TOKENIZED, store = Store.YES)
    public String getClassName() {
        return super.getClass().getName();
    }


    @Field(name = CONCATENATED_CONTENT, index = Index.TOKENIZED, store = Store.YES)
    private String getConcatenatedContent()  {
//    public String getConcatenatedFields()  {
            StringBuffer concatenatedFields = new StringBuffer("");


                if (this.getDescription() != null) {
                    concatenatedFields.append(this.getDescription()).append(" ");
                }

                if (this.getCategory() != null) {
                    concatenatedFields.append(this.getCategory().getName()).append(" ");
                }
                if (this.getManufacturer() != null) {
//                    Manufacturer mftr = daoFactory.getManufacturerDAO().getManufacturerById(this.getManufacturer().getManufacturerId(), false);
                    concatenatedFields.append(this.getManufacturer().getExternalOrgDetail().getOrgName()).append(" ");
                }
                if (this.getOrgBudget() != null) {
//                    Long ordBdgtId = this.getOrgBudget().getOrgBudgetId();
//                    OrgBudget orgBdgt = daoFactory.getOrgBudgetDAO().getOrgBudgetById(ordBdgtId, false);
                    concatenatedFields.append(this.getOrgBudget().getOrgBudgetCodeAndName()).append(" ");
                }
                if (this.getStatus() != null) {
//                    Status status = this.getStatus();
//                    status = daoFactory.getStatusDAO().getStatusById(status.getStatusId(), false);
                    concatenatedFields.append(this.getStatus().getName()).append(" ");
                }
                if (this.getItemVendors() != null) {
                    concatenatedFields.append(getVendorNames());
                }

                if (this.getIcnbr() != null) {
                    concatenatedFields.append(getIcnbrString()).append(" ");
                    concatenatedFields.append(getFullIcnbr()).append(" ");
                    concatenatedFields.append(getFullIcnbrWithoutDash()).append(" ");
                }
                if (this.getPrimaryContact() != null) {
//                    Person person = this.getPrimaryContact();
//                    person = daoFactory.getPersonDAO().getPersonById(person.getPersonId(), false);
                    concatenatedFields.append(getPrimaryContact().getFirstAndLastName()).append(" ");
                }
                if (this.getSecondaryContact() != null) {
//                    Person person = this.getSecondaryContact();
//                    person = daoFactory.getPersonDAO().getPersonById(person.getPersonId(), false);
                    concatenatedFields.append(getSecondaryContact().getFirstAndLastName()).append(" ");
                }
                if (this.getAsstDivDirector() != null) {
//                    Person asstDivDirector = this.getAsstDivDirector();
//                    asstDivDirector = daoFactory.getPersonDAO().getPersonById(asstDivDirector.getPersonId(), false);
                    concatenatedFields.append(getAsstDivDirector().getFirstAndLastName()).append(" ");
                }

                return concatenatedFields.toString();
            }

//    @Field(index = Index.TOKENIZED, store = Store.YES)
    @Field(name = VENDOR, index = Index.TOKENIZED, store = Store.YES)
    private String getVendorNames() {
        StringBuffer vendorNamesBuffer = new StringBuffer();
//        if (getItem() != null) {
            for (Object itemVendor : this.getItemVendors()) {
                Vendor vendor = ((ItemVendor) itemVendor).getVendor();
//                try {
//                    vendor = daoFactory.getVendorDAO().getVendorById(vendor.getVendorId(), false);
                    vendorNamesBuffer.append(vendor.getExternalOrgDetail().getOrgName()).append(", ");
//                } catch (InfrastructureException e) {
//                    log.error(e);
//                }
            }
//        }
        return vendorNamesBuffer.toString();
    }

    @Field(name = VENDOR_ID, index = Index.TOKENIZED, store = Store.YES)
    private String getVendorIDs() {
        StringBuffer vendorIDsBuffer = new StringBuffer();
//        if (getItem() != null) {
            for (Object itemVendor : this.getItemVendors()) {
                Vendor vendor = ((ItemVendor) itemVendor).getVendor();
//                try {
//                    vendor = daoFactory.getVendorDAO().getVendorById(vendor.getVendorId(), false);
                    vendorIDsBuffer.append(vendor.getVendorId().toString()).append(" ");
//                } catch (InfrastructureException e) {
//                    log.error(e);
//                }
            }
//        }
        return vendorIDsBuffer.toString();
    }

}


