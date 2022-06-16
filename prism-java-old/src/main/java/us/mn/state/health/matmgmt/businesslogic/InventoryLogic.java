package us.mn.state.health.matmgmt.businesslogic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.StockItemHitReorderPointEmailBuilder;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;

import java.util.Calendar;

public class InventoryLogic  {
    public static Log log = LogFactory.getLog(InventoryLogic.class);
    
    /**
     * Handle the business of fulfilling a stock request: 
     * deduct qty on hand from stock item, set the status on the request line item, 
     * notify stock item contacts if the stock item hits ROP.
     * @throws java.lang.Exception
     * @param qtyPicked
     * @param rli
     */
    public static void fillStockRequest(RequestLineItem rli, int qtyPicked) throws Exception {        
        int totalQtyFilled = qtyPicked + rli.getQuantityFilled().intValue();
        rli.setQuantityFilled(new Integer(totalQtyFilled));
        rli.setDateDelivered(Calendar.getInstance().getTime());
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        
        //deduct on-hand amt for the stock item
        StockItem si = (StockItem) daoFactory.getStockItemDAO().getStockItemById(rli.getItem().getItemId(), true);
        si.setQtyOnHand(new Integer(Math.max(si.getQtyOnHand().intValue() - qtyPicked, 0)));
        rli.setItem(si);  //is this necessary?
        //notifyStockItemHitReorderPoint(si);

        //set the status of the rli to Fulfilled if it has been fulfilled, or if the
        //stock item is inactive AND the qtyOnHand is less than 1.
        if(totalQtyFilled >= rli.getQuantity().intValue() ||
           (si.getStatus().getStatusCode().equals(Status.INACTIVE) && si.getQtyOnHand().intValue() < 1)) {
              Status fulfilled = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                                                                                             Status.FULFILLED);
              rli.setStatus(fulfilled);
        }
    }
    
     /**
     * helper method to send an email to the appropriate parties if the
     * stock item has hit its re-order point.
     *
     * @param si
     */
    private static void notifyStockItemHitReorderPoint(StockItem si) {
        if (si.getQtyOnHand().intValue() <= si.getReorderPoint().intValue()) {
            try {
                EmailBean emailBean = new EmailBean();
                EmailBuilder emailBuilder = new StockItemHitReorderPointEmailBuilder(si, emailBean);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                emailBusinessDelegate.sendEmail(emailBean);
            }
            catch (Exception e) {
                log.error("Error in InventoryLogic.notifyStockItemHitReorderPoint()",e);
            }
        }
    }
    private static void notifyStockItemHitSafetyStock(StockItem si) {
        int sio = 0;
        if (si.getSafetyStock() !=null) sio = si.getSafetyStock().intValue();

        if (sio > 0 && si.getQtyOnHand().intValue() <= sio) {
            try {
                EmailBean emailBean = new EmailBean();
                EmailBuilder emailBuilder = new StockItemHitReorderPointEmailBuilder(si, emailBean);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                emailBusinessDelegate.sendEmail(emailBean);
            }
            catch (Exception e) {
                log.error("Error in InventoryLogic.notifyStockItemHitReorderPoint()",e);
            }
        }
    }

}