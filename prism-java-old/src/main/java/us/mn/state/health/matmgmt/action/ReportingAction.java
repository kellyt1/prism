package us.mn.state.health.matmgmt.action;

import java.net.URLDecoder;
import java.util.*;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.reporting.GeneralReportingFormBuilder;
import us.mn.state.health.common.report.JasperReportWriter;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Report;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.purchasing.AssetView;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.purchasing.adapter.AssetViewAdapter;
import us.mn.state.health.model.purchasing.adapter.AssetViewDTO;
import us.mn.state.health.model.receiving.ReceivingDetail;
import us.mn.state.health.view.purchasing.AssetsViewReportForm;
import us.mn.state.health.view.reporting.GeneralReportingForm;

/**
* Struts Controller that processes Receiving actions
*/
public class ReportingAction extends MappingDispatchAction {       
    private static Log log = LogFactory.getLog(ReportingAction.class);
  
    /**
     * prepare the form to collect parameters necessary to generate the To Be Stocked Report.
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward viewCreateToBeStockedReport(ActionMapping mapping,
                                                     ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception {
            GeneralReportingForm genForm = (GeneralReportingForm)form;
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);            
            GeneralReportingFormBuilder builder = new GeneralReportingFormBuilder(genForm, daoFactory);
            builder.buildFacilities();
            return mapping.findForward("success");
    }
    
    /**
     * Action that creates the ToBeStocked report.
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward printToBeStockedReport(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        GeneralReportingForm genForm = (GeneralReportingForm)form;
        String startDate = genForm.getStartDate();
        String endDate = genForm.getEndDate();
        if (startDate == null || startDate.trim().equals("")) {
            startDate = DateUtils.toString((DateUtils.addDaysToDate(Calendar.getInstance().getTime(),-7)));
        }
        if (endDate == null || endDate.trim().equals("")){
            endDate = DateUtils.toString(Calendar.getInstance().getTime());
        }

        Collection results = daoFactory.getPurchasingOrderLineItemDAO()
                                       .findStockItemsToBeStocked(genForm.getFacilityId(),
                                                                  DateUtils.createDate(startDate, DateUtils.DEFAULT_DATE_FORMAT), 
                                                                  DateUtils.createDate(endDate, DateUtils.DEFAULT_DATE_FORMAT));
        //we have to do the following crap because of lazy-loading issues.  The report is expecting
        //to be able to cast oli.item to a StockItem, but it can't do that to a proxy.  So we have to 
        //eager load the items here.  We have seen that Hibernate does not consider the oli as 'dirty' 
        //due to our call to setItem(), so luckily it does not try to issue a SQL update.
        for(Iterator iter = results.iterator(); iter.hasNext(); ) {
            ReceivingDetail rd = (ReceivingDetail)iter.next();
            OrderLineItem oli = rd.getOrderLineItem();
            Item item = (Item)oli.getItem();
            if(item.getItemType().equals(Item.STOCK_ITEM)) {
                StockItem si = daoFactory.getStockItemDAO().getStockItemById(item.getItemId(), false);
                oli.setItem(si);
            }
        }
//        String subReportPath = this.getServlet().getServletContext().getRealPath(Report.REPORTS_DIR_PATH_NAME + "stockItemLocations.jasper");
        String subReportPath = Report.REPORTS_DIR_PATH_NAME + "stockItemLocations.jasper";
        HashMap hashmap = new HashMap();
        hashmap.put("stockItemLocationsSubReport", subReportPath);
        return generateReport(results, Report.TO_BE_STOCKED, hashmap, response);
    }   
    
    /**
     * Prepare the form for collection the parameters needed for generating the Delivery Receipt report.
     * @throws java.lang.Exception
     * @return ActionForward representing the next view resource
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward viewCreateDeliveryReceipt(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
            GeneralReportingForm genForm = (GeneralReportingForm)form;
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);            
            GeneralReportingFormBuilder builder = new GeneralReportingFormBuilder(genForm, daoFactory);
            builder.buildFacilities();
            return mapping.findForward("success");
    }
    
    /**
     * Action that creates the DeliveryReceipt report.
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward printDeliveryReceiptReport(ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        GeneralReportingForm genForm = (GeneralReportingForm)form;
        String startDate = genForm.getStartDate();
        String endDate = genForm.getEndDate();
        if (startDate == null || startDate.trim().equals("")
         || endDate == null || endDate.trim().equals("")
         || genForm.getFacilityId() == null || genForm.getFacilityId().trim().equals("")) {
            return mapping.findForward("error");    
        }
        Collection results = daoFactory.getPurchasingOrderLineItemDAO()
                                       .findPurchaseItemsToBeDelivered(genForm.getFacilityId(),
                                                                       DateUtils.createDate(startDate, DateUtils.DEFAULT_DATE_FORMAT), 
                                                                       DateUtils.createDate(endDate, DateUtils.DEFAULT_DATE_FORMAT));
//        String subReportPath = this.getServlet()
//                                   .getServletContext()
//                                   .getRealPath(Report.REPORTS_DIR_PATH_NAME + "deliveryReceiptRLI_SubRpt.jasper");
        String subReportPath = Report.REPORTS_DIR_PATH_NAME + "deliveryReceiptRLI_SubRpt.jasper";

        String baseDir = this.getServlet().getServletContext().getRealPath(Report.IMAGES_DIR_PATH_NAME);
        HashMap hashmap = new HashMap();
        hashmap.put("rliSubReport", subReportPath);
        hashmap.put("BaseDir", baseDir);
        return generateReport(results, Report.DELIVERY_RECEIPT, hashmap, response);
    }  
        

    /**
     * Action that creates the DeliveryReceipt report.
     * @throws java.lang.Exception
     * @return
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward printDynamicPrismReport(ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        HashMap hashmap = new HashMap();
        Connection conn = null;
        String rptName = request.getParameter("rptName");
        //the equal sign '=' has been encoded as %3d  or  %3D  as in
        //printDynamicPrismReport.do?rptName=WardOutOfStockReport
        // to
        //printDynamicPrismReport.do?rptName%3dWardOutOfStockReport
        if (rptName == null) {
            String queryString = request.getQueryString();
            if (queryString.indexOf("%3d") > -1) {
                rptName = queryString.substring(queryString.indexOf("%3d") + 3);
            }
            else if (queryString.toUpperCase().indexOf("%3D") > -1) {
                rptName = queryString.substring(queryString.toUpperCase().indexOf("%3D") + 3);
            }
        }
//        return generateReport(conn, Report.REPORTS_DIR_PATH_NAME + "WardOutOfStockReport", hashmap, response);
        return generateReport(Report.REPORTS_DIR_PATH_NAME + rptName, hashmap, response);
    }

    /**
     * This is not a Struts action, but a helper method for this class... Perhaps it should be private.
     * It does return an ActionForward to the appropriate PDF that it generates.
     */
    public ActionForward generateReport(Collection reportData, String reportFilePath, HashMap hashmap, HttpServletResponse response) throws Exception {
        String outFilePath = Report.GENERAL_REPORT_OUTPUT_PATH;      
        JasperReportWriter reportWriter =  new JasperReportWriter(this.getServlet(),
                                                                  reportFilePath, 
                                                                  outFilePath,
                                                                  reportData,
                                                                  hashmap);
        outFilePath = reportWriter.write();
        return new ActionForward(outFilePath, true);
        //return null;
    }

    /**
     * This is not a Struts action, but a helper method for this class... Perhaps it should be private.
     * It does return an ActionForward to the appropriate PDF that it generates.
     */
    public ActionForward generateReport(String reportFilePath, HashMap hashmap, HttpServletResponse response) throws Exception {
        String outFilePath = Report.GENERAL_REPORT_OUTPUT_PATH;
        Connection conn = null;
        JasperReportWriter reportWriter =  new JasperReportWriter(this.getServlet(),
                                                                  reportFilePath,
                                                                  outFilePath,
                                                                  conn,
                                                                  hashmap);
        outFilePath = reportWriter.write();
        return new ActionForward(outFilePath, true);
        //return null;
    }

    public ActionForward readyAssetsViewReport(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        AssetsViewReportForm pageForm = (AssetsViewReportForm) form;
        pageForm.setResults(null);
        pageForm.setOrderDate(null);
        return mapping.findForward("success");
    }

    public ActionForward generateAssetsViewReport(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception{
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        AssetsViewReportForm queryForm = (AssetsViewReportForm)form;
        String startDate = queryForm.getOrderDate();
        List<AssetView> results = new ArrayList<>();
        if(!startDate.equalsIgnoreCase("")){
            results = daoFactory.getAssetViewDAO()
                    .findAssetsSinceOrderDate(DateUtils.createDate(startDate, DateUtils.DEFAULT_DATE_FORMAT));
        }
        List<AssetViewDTO> viewDTOList = new ArrayList<>();
        for (AssetView a : results){
            viewDTOList.add(AssetViewAdapter.toDTO(a));
        }
        queryForm.setResults(viewDTOList);
        return mapping.findForward("success");
    }
}