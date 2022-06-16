package us.mn.state.health.builder.inventory;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.StockItemFacility;
import us.mn.state.health.view.inventory.InventoryMaintenanceForm;

public class InventoryMaintenanceFormBuilder  {

    private InventoryMaintenanceForm inventoryMaintenanceForm;
    
    private DAOFactory daoFactory;
    
    public InventoryMaintenanceFormBuilder(InventoryMaintenanceForm inventoryMaintenanceForm,
            DAOFactory daoFactory) {
        
        this.inventoryMaintenanceForm = inventoryMaintenanceForm;
        this.daoFactory = daoFactory;
    }
    
    public void buildCategories() throws InfrastructureException {
        Category category = 
            daoFactory.getCategoryDAO().findByCategoryCode(Category.MATERIALS);
        inventoryMaintenanceForm.setCategory(category);
    }
    
    public void buildStockItemLocations()  throws InfrastructureException {
        Collection stockItemFacilities = 
            daoFactory.getStockItemFacilityDAO().findStockItemFacilitiesByType(StockItemFacility.TYPE_BUILDING);
        inventoryMaintenanceForm.setFacilities(stockItemFacilities); 
    }
}