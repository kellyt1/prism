package us.mn.state.health.matmgmt.action;

import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.RequestEvaluation;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.common.exceptions.InfrastructureException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rodent1
 * Date: Jun 2, 2009
 * Time: 3:42:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewStockItemActionRequestInfo extends HttpServlet {


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
             throws ServletException, IOException {

        boolean showAnyway = false;
        Long itemId = new Long( request.getParameter("itemId"));
        String sShowIndicator = request.getParameter("showIndicator");
        String skin = request.getParameter("skin");

        if (skin != null) {
            if (skin.equals("PRISM2")) {
                 request.getSession().setAttribute("skin","PRISM2");
            }
            else {
                request.getSession().setAttribute("skin","reset");
            }
        }


        if (sShowIndicator != null ) showAnyway = true;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
         Collection<StockItemActionRequest> siars = null;
        StockItemActionRequest siar = null;
        StockItem si = null;
        try {
            siars = daoFactory.getStockItemActionRequestDAO().findSIARByPotentialItemId(itemId);
        } catch (InfrastructureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Boolean siarFound = false;
        for (Iterator siarIter = siars.iterator(); siarIter.hasNext();) {
            siar = (StockItemActionRequest)siarIter.next();
            si = siar.getPotentialStockItem();
            if (si != null && si.getItemId().equals(itemId)) {
                siarFound = true;
                break;
            }
        }

        if (siarFound) {
            // Write HTML header
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println("<html><head><title>Stock Item Action Request Information</title></head><body>");
//                out.println("<% String context = request.getContextPath();%>");
//                out.println("<%@ include file=\"materialsManagementHead.jsp\" %>");
                out.println("<b>Stock Item Action Request Information:</b>" );
                out.println("<table cellspacing=\"0\" cellpadding=\"2\" border=\"1\"><tr>");
                out.println("<tr><td>Item ICNBR: </td><td>" + si.getFullIcnbr() + "</td></tr>");
                out.println("<tr><td>Suggested Vendor Name: </td><td>"+ siar.getSuggestedVendorName() + "</td></tr>");
                out.println("<tr><td>Vendor Catalog #: </td><td>"+ siar.getVendorCatalogNbr() + "</td></tr>");
                out.println("<tr><td>Cost: </td><td>"+ siar.getVendorCost() + "</td></tr>");
                out.println("</table>");


                // Write HTML footer

                // Write System Administration information

                out.println("</body></html>");
                out.flush();
                out.close();
        } else {  //Request Line Item NOT found
            // Write HTML header
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println("<html><head><title>Stock Item Action Request Information</title></head><body>");
                out.println("<br />");
                out.println("Stock Item Action Request Information Does NOT exist");

                // Write HTML footer
                out.println("</body></html>");
                out.flush();
                out.close();
        }
    }
}
