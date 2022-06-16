package us.mn.state.health.view.materialsrequest.reports;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.MappingDispatchAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import us.mn.state.health.matmgmt.util.Report;
import us.mn.state.health.common.report.JasperReportWriter;

import java.util.HashMap;
import java.sql.Connection;


public class FishConsumptionAction extends MappingDispatchAction{
    private static Log log = LogFactory.getLog(FishConsumptionAction.class);




//    public ActionForward generateReport(ActionMapping mapping,
//                                                        ActionForm purchaseReport,
//                                                        HttpServletRequest request,
//                                                        HttpServletResponse response) throws Exception {
//     //public ActionForward generateReport(HttpServletRequest request,HttpServletResponse response, Collection<RequestLineItemForm> rliForms) throws Exception {
//    log.debug("FishConsumptionAction");
//        String outFilePath = "/requests/reports/index.jsp";
//        HashMap hashmap = new HashMap();
//        String rptName="report1";
//        String reportFilePath = Report.REPORTS_DIR_PATH_NAME + rptName;
//        Collection reportData = new ArrayList();
//        String fundingSrcSubRptPath = Report.REPORTS_DIR_PATH_NAME + "report1.jasper";
//
//
//        hashmap.put("fundingSrcSubRpt", fundingSrcSubRptPath);
////        for (Iterator rliFormIterator = rliForms.iterator(); rliFormIterator.hasNext();) {
////            RequestLineItem rli = ((RequestLineItemForm)rliFormIterator.next()).getRequestLineItem();
////            RequestLineItemData rliData = new RequestLineItemData(rli);
////            reportData.add(rliData);
////        }
//
//        FishConsumptionForm purchaseReportForm = new FishConsumptionForm();
//        purchaseReportForm = (FishConsumptionForm)purchaseReport;
//        hashmap.put("Start Date",purchaseReportForm.getDateFrom());
//        hashmap.put("End Date",purchaseReportForm.getDateTo());
//
//
//        JasperReportWriter reportWriter = new JasperReportWriter(this.getServlet(),response,
//                reportFilePath,
//                outFilePath,
//                reportData,
//                hashmap);
//
//        outFilePath = reportWriter.write();
//
//        return new ActionForward(outFilePath, true);
//     }

    public ActionForward generateReport(ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        HashMap hashmap = new HashMap();
        FishConsumptionForm fishingForm = (FishConsumptionForm)form;
        String startDate = fishingForm.getDateFrom();
        String endDate = fishingForm.getDateTo();
        //String catId = fishingForm.getCategoryId();
        //String icnbr = fishingForm.getIcnbr();
        //log.info("value of categories is "+fishingForm.getCategoryId());
        hashmap.put("StartDate",startDate);
        hashmap.put("EndDate",endDate);
//        hashmap.put("ICNBR",icnbr);
        //hashmap.put("CatId",catId);
        Connection conn = null;
        String rptName = "fishConsumption";
        
//        return generateReport(conn, Report.REPORTS_DIR_PATH_NAME + "WardOutOfStockReport", hashmap, response);
        return generateReport(Report.REPORTS_DIR_PATH_NAME + rptName, hashmap, response);
    }

    
    public ActionForward generateReport(String reportFilePath, HashMap hashmap, HttpServletResponse response) throws Exception {
        String outFilePath = Report.GENERAL_REPORT_OUTPUT_PATH;
        Connection conn = null;
        JasperReportWriter reportWriter =  new JasperReportWriter(this.getServlet(),
                                                                  reportFilePath,
                                                                  outFilePath,
                                                                  conn,
                                                                  hashmap);
        outFilePath = reportWriter.write("csv");
        return new ActionForward(outFilePath, true);
        //return null;
    }
}
