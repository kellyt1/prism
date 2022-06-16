package us.mn.state.health.builder.inventory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.view.inventory.StockItemForm;

public class StockItemBuilder extends PurchaseItemBuilder {
    private StockItem stockItem;
    private StockItemForm stockItemForm;

    public StockItemBuilder(StockItem stockItem,
                            StockItemForm stockItemForm,
                            User user,
                            DAOFactory daoFactory) {                            
        super(stockItem, stockItemForm, user, daoFactory);        
        this.stockItem = stockItem;
        this.stockItemForm = stockItemForm;        
    }
    
    public void buildDefaultProperties() {
        if(stockItem.getCycleCountPriority() == null)
            stockItem.setCycleCountPriority("N/A");
        if(stockItem.getFillUntilDepleted() == null)
            stockItem.setFillUntilDepleted(Boolean.FALSE);
        if(stockItem.getEconomicOrderQty() == null)
            stockItem.setEconomicOrderQty(new Integer(0));
        if(stockItem.getEstimatedAnnualUsage() == null)
            stockItem.setEstimatedAnnualUsage(new Integer(0));
        if(stockItem.getPackQty() == null)
            stockItem.setPackQty(new Integer(0));
        if(stockItem.getQtyOnHand() == null)
            stockItem.setQtyOnHand(new Integer(0));
        if(stockItem.getReorderDate() == null)
            stockItem.setReorderDate(new Date());
        if(stockItem.getReorderPoint() == null)
            stockItem.setReorderPoint(new Integer(0));
        if(stockItem.getReorderQty() == null)
            stockItem.setReorderQty(new Integer(0));
        if(stockItem.getStaggeredDelivery() == null)
            stockItem.setStaggeredDelivery(Boolean.FALSE);
        if(stockItem.getPotentialIndicator() == null) 
            stockItem.setPotentialIndicator(Boolean.TRUE);
    }

    public void buildDispenseUnit() {
        Unit unit = stockItemForm.getDispenseUnit();
        stockItem.setDispenseUnit(unit);
    }

    public void buildOrgBudget() throws InfrastructureException {
        OrgBudget orgBudget = null;
        
        if(StringUtils.nullOrBlank(stockItemForm.getOrgBudgetId())) {
            orgBudget = daoFactory.getOrgBudgetDAO().findByOrgBudgetCode(OrgBudget.UKNOWN);
        }
        else {
            Long id = new Long(stockItemForm.getOrgBudgetId());
            orgBudget = daoFactory.getOrgBudgetDAO().getOrgBudgetById(id, false);
        }
        stockItem.setOrgBudget(orgBudget);
    }
    
    public void buildLocations() {
////        stockItem.setLocations(stockItemForm.getMyStockItemLocations());
//        stockItem.getLocations().clear();
//        for(Iterator iter = stockItemForm.getMyStockItemLocations().iterator(); iter.hasNext(); ) {
//            StockItemLocation loc = (StockItemLocation)iter.next();
//            StockItemLocation newLoc = new StockItemLocation();
//            newLoc.setFacility(loc.getFacility());
//            newLoc.setIsPrimary(loc.getIsPrimary());
//            newLoc.setLocationCode(loc.getLocationCode());
//            stockItem.addLocation(newLoc);
//        }
    }
    
    public void buildObjectCode() {
        stockItem.setObjectCode(stockItemForm.getObjectCode());
    }

//    public void buildPrintSpecFile() throws InfrastructureException {
//        if(stockItemForm.getPrintSpecFile() != null) {
//            try {
//                stockItem.setPrintSpecFileName(stockItemForm.getPrintSpecFile().getFileName());
//                stockItem.setPrintSpecFileBinaryValue(stockItemForm.getPrintSpecFile().getFileData());
//                stockItemForm.getPrintSpecFile().destroy();
//            }
//            catch(FileNotFoundException fnfe) {
//                throw new InfrastructureException("Failed Building Print Spec File: ", fnfe);
//            }
//            catch(IOException ioe) {
//                throw new InfrastructureException("Failed Building Print Spec File: ", ioe);
//            }
//        }
//    }

    public void buildStatus() {
        stockItem.setStatus(stockItemForm.getStatus());
    }
    
    public void buildPrimaryContact() throws InfrastructureException {
        if(stockItemForm != null && !StringUtils.nullOrBlank(stockItemForm.getPrimaryContactId())) {
            Long id = new Long(stockItemForm.getPrimaryContactId());
            Person primaryContact = daoFactory.getPersonDAO().getPersonById(id, false);
            stockItem.setPrimaryContact(primaryContact);
        }
    }

    public void buildSecondaryContact() throws InfrastructureException {
        if(stockItemForm != null && !StringUtils.nullOrBlank(stockItemForm.getSecondaryContactId())) {
            Long id = new Long(stockItemForm.getSecondaryContactId());
            Person contact = daoFactory.getPersonDAO().getPersonById(id, false);
            stockItem.setSecondaryContact(contact);
        }
    }

    public void buildAsstDivDirector() throws InfrastructureException {
         if(stockItemForm != null && !StringUtils.nullOrBlank(stockItemForm.getAsstDivDirId())) {
            Long id = new Long(stockItemForm.getAsstDivDirId());
            Person person = daoFactory.getPersonDAO().getPersonById(id, false);
            stockItem.setAsstDivDirector(person);
        }
    }
}