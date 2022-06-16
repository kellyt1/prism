package us.mn.state.health.builder.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.view.inventory.StockItemActionRequestForm;
import us.mn.state.health.view.inventory.StockItemForm;

public class StockItemActionRequestFormBuilder {
    private DAOFactory daoFactory;
    private StockItemForm potentialStockItemForm;
    private StockItemActionRequest stockItemActionRequest;
    private StockItem stockItem;
    private StockItemActionRequestForm stockItemActionRequestForm;
    private User evaluator;

    public StockItemActionRequestFormBuilder(StockItemActionRequestForm stockItemActionRequestForm,
                                             StockItemForm potentialStockItemForm,
                                             DAOFactory daoFactory) {
        this.stockItemActionRequestForm = stockItemActionRequestForm;
        this.potentialStockItemForm = potentialStockItemForm;
        this.daoFactory = daoFactory;
    }

    public StockItemActionRequestFormBuilder(StockItemActionRequestForm stockItemActionRequestForm,
                                             StockItem stockItem,
                                             StockItemForm potentialStockItemForm,
                                             DAOFactory daoFactory) {
        this.stockItemActionRequestForm = stockItemActionRequestForm;
        this.stockItem = stockItem;
        this.potentialStockItemForm = potentialStockItemForm;
        this.daoFactory = daoFactory;
    }

    public StockItemActionRequestFormBuilder(StockItemActionRequestForm stockItemActionRequestForm,
                                             StockItemForm potentialStockItemForm,
                                             DAOFactory daoFactory,
                                             StockItemActionRequest stockItemActionRequest,
                                             User evaluator) {
        this(stockItemActionRequestForm, potentialStockItemForm, daoFactory);
        this.stockItemActionRequest = stockItemActionRequest;
        this.stockItem = stockItemActionRequest.getStockItem();
        this.evaluator = evaluator;
    }

    public void buildActionRequestType() {
        stockItemActionRequestForm.setActionRequestType(stockItemActionRequest.getActionRequestType());
    }

    public void buildDefaultProperties() {
        stockItemActionRequestForm.setApproved(null);
        stockItemActionRequestForm.setSuggestedVendorId(null);
        stockItemActionRequestForm.setSuggestedVendorName(null);
        stockItemActionRequestForm.setVendorCatalogNbr(null);
        stockItemActionRequestForm.setVendorCost(null);
    }

    public void buildEvaluator() {
        stockItemActionRequestForm.setEvaluator(evaluator);
    }

    public void buildPotentialStockItemForm() throws InfrastructureException {
        stockItemActionRequestForm.setPotentialStockItemForm(potentialStockItemForm);
    }

    public void buildRequestor() {
        stockItemActionRequestForm.setRequestor(stockItemActionRequest.getRequestor());
    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(stockItemActionRequestForm, stockItemActionRequest);
            if(stockItemActionRequest.getSuggestedVendor() != null) {
                stockItemActionRequestForm.setSuggestedVendorId(stockItemActionRequest.getSuggestedVendor().getVendorId().toString());
            } 
            else {
                stockItemActionRequestForm.setSuggestedVendorId(null);
            }
        } 
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building StockItemActionRequestForm: ", rpe);
        }
    }

    public void buildStockItem() {
        stockItemActionRequestForm.setStockItem(stockItem);
    }
    
    public void buildStockQtyChangeReasonRefs() throws InfrastructureException {
        Collection stockQtyChangeReasons = daoFactory.getStockQtyChangeReasonRefDAO().findAll();
        stockItemActionRequestForm.setStkQtyChangeReasonRefs(stockQtyChangeReasons);
    }

    public void buildVendors() throws InfrastructureException {
//        Collection vendors = daoFactory.getVendorDAO().findAll(false);
        Collection vendors = daoFactory.getVendorDAO().findAll();
        stockItemActionRequestForm.setVendors(vendors);
    }

    public void buildDiscardStock() {
        Boolean fillUntilDepleted = stockItem.getFillUntilDepleted();
        if (fillUntilDepleted!=null) {
            stockItemActionRequestForm.setDiscardStock(new Boolean(!fillUntilDepleted.booleanValue()));
        }
        else {
            stockItemActionRequestForm.setDiscardStock(null);
        }
    }

}