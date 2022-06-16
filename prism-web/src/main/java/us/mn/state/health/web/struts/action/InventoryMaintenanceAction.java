package us.mn.state.health.web.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.facade.inventory.InventoryReportsFacade;
import us.mn.state.health.facade.inventory.InventoryReportsFacadeResult;
import us.mn.state.health.view.inventory.Command;
import us.mn.state.health.view.inventory.StockItemsReportForm;

public class InventoryMaintenanceAction extends MappingDispatchAction {
    private InventoryReportsFacade inventoryReportsFacade;

    public void setInventoryReportsFacade(InventoryReportsFacade inventoryReportsFacade) {
        this.inventoryReportsFacade = inventoryReportsFacade;
    }

    public ActionForward viewStockItemsBelowROPNotOnOrder(ActionMapping mapping,
                                                          ActionForm form,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {

        StockItemsReportForm stockItemsReportForm = (StockItemsReportForm) form;

        //check if the action is hit using the menu
        String reset = request.getParameter("cmd");
        if(Command.RESET.equals(reset)){
            stockItemsReportForm.setReset(true);
            stockItemsReportForm.reset(mapping, request);
            stockItemsReportForm.setWoLocations(true);
        }

        if (stockItemsReportForm.getCategories().size() == 0
                || stockItemsReportForm.getStockItemFacilities().size() == 0) {
            InventoryReportsFacadeResult searchCriteria = inventoryReportsFacade.loadSearchCriteriaLists();

            List categories = searchCriteria.getCategories();
            List facilities = searchCriteria.getStockItemFacilities();

            stockItemsReportForm.setCategories(categories);
            stockItemsReportForm.setStockItemFacilities(facilities);
        }

        if(Command.RESET.equals(reset)){
            return mapping.findForward("success");
        }

        Long[] selectedCategories = stockItemsReportForm.getSelectedCategories();
        Long[] selectedFacilities = stockItemsReportForm.getSelectedFacilities();

        InventoryReportsFacadeResult facadeResult =
                inventoryReportsFacade.findStockItemsBelowROP(selectedCategories, selectedFacilities, stockItemsReportForm.getWoLocations());
        stockItemsReportForm.setStockItems(facadeResult.getStockItems());
        return mapping.findForward("success");
    }
}
