package us.mn.state.health.builder.inventory;

import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.view.inventory.StockItemForm;

public class StockItemLocationBuilder {
    private boolean addStockItemLocation;
    private DAOFactory daoFactory;
    private StockItem stockItem;
    private StockItemForm stockItemForm;    
    
    public StockItemLocationBuilder(StockItem stockItem, 
                                    StockItemForm stockItemForm, 
                                    DAOFactory daoFactory,
                                    boolean addStockItemLocation) {            
        this.stockItem = stockItem;
        this.stockItemForm = stockItemForm;
        this.daoFactory = daoFactory;
        this.addStockItemLocation = addStockItemLocation;        
    }
    
    public void buildStockItemLocation() {
        if(addStockItemLocation) {
            
        }
        else {
            
        }
    }
}