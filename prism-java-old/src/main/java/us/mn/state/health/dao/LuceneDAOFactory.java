package us.mn.state.health.dao;

import us.mn.state.health.dao.inventory.ItemDAO;
import us.mn.state.health.dao.inventory.LuceneItemDAO;
import us.mn.state.health.dao.inventory.LuceneStockItemDAO;
import us.mn.state.health.dao.inventory.StockItemDAO;

public class LuceneDAOFactory extends BaseDAOFactory {

    public StockItemDAO getStockItemDAO() {
        return new LuceneStockItemDAO();
    }

    public ItemDAO getItemDAO() {
        return new LuceneItemDAO();
    }
    
}