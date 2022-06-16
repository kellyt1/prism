package us.mn.state.health.matmgmt.action

import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.dao.DAOFactory
import us.mn.state.health.model.common.*
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation
import us.mn.state.health.model.materialsrequest.Request
import us.mn.state.health.model.materialsrequest.RequestLineItem
import us.mn.state.health.security.ApplicationResources
import us.mn.state.health.util.Environment

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by IntelliJ IDEA.
 * User: RodenT1a
 * Date: Nov 6, 2007
 * Time: 9:15:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewApprovalStatusServlet  extends HttpServlet {

   
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean showAnyway = false;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        Request matReq = null;
        Long requestId;
        Boolean displayAll = false;
        String requestIdStr = request.getParameter("requestId");
        if (requestIdStr) {
            requestId = new Long(requestIdStr);
            try {
                matReq = daoFactory.getRequestDAO().getRequestById(requestId,false);
            } catch (InfrastructureException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        String trackingNumber = "";
        String userMessage = "";
        Long requestLineItemId;
        if (request.getParameter("requestLineItemId")) {
            requestLineItemId = new Long( request.getParameter("requestLineItemId"));
        } else {  //use supplied requestId to get first rli
            displayAll = true;
            showAnyway = true;
            ArrayList rlis =  new ArrayList(matReq.getRequestLineItems());
            requestLineItemId = ((RequestLineItem)rlis.get(0)).getRequestLineItemId();
            trackingNumber = matReq.getTrackingNumber();
            userMessage = "<font color='red'>There are no longer any Line Items in this Request that require your Approval.<br/>";
            userMessage += "Here is the current Approval Summary for this Request.</font><br/>";

        }

        String sShowIndicator = request.getParameter("showIndicator");
        String skin = request.getParameter("skin");
        String displayAllrlis = request.getParameter("displayAllrlis");
        Integer rliCount;

        //todo check user to see if they still have to approve
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);

        if (displayAllrlis) displayAll = true;
        if (sShowIndicator) showAnyway = true;
        if (skin) request.getSession().setAttribute("skin", (skin.equals("PRISM2")) ? "PRISM2" : "reset");
        RequestLineItem rli = null;


        try {
            rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(requestLineItemId);
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

        if (rli) {
            if (!rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_APPROVAL) || showAnyway) {
                try {
                    matReq = daoFactory.getRequestDAO().getRequestById(rli.getRequest().getRequestId(),false);
                } catch (InfrastructureException e) {
                    e.printStackTrace();
                }

                Collection<RequestLineItem> rlis = matReq.getRequestLineItems();
                rliCount = rlis.size();

                if (request.getParameter("trackingNumber") != null) {
                    trackingNumber = request.getParameter("trackingNumber");
                }                    

                // Write HTML header
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println("<html><head><title>Request Approval Status</title>" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"${request.contextPath}/webjars/bootstrap/3.3.5/css/bootstrap.min.css\"/>\n" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"${request.contextPath}/css/main.css\"/>\n" +
                        "</head>");
                if (!userMessage) {
                    out.println("<body>\n" +
                            "<div id=\"mdhHeaderDiv\" class=\"row\">\n" +
                            "                <div>" +
                            "                    <div class=\"mdhLogo\">\n" +
                            "                        <a href=\"http://www.health.state.mn.us/\">\n" +
                            "                            <img src=\"/images/logo-mdh-mn-h-whi_rgb.png\" alt=\"MDH Logo\"/>\n" +
                            "                            " +
                            "                        </a>\n" +
                            "                    </div>\n" +
                            "                </div>\n" +
//                            "                <div id=\"headerRightSide\" class=\"col-sm-7 hidden-xs\">\n" +
                            "                    <div class=\"appTitleDiv\" >PRISM</div>\n" +
                            "                </div>\n" +
                            "            </div>");
                }
                    out.println("<div style=\"margin: 15px;\"");
                out.println("<br /> <h4>"  + userMessage + "<h4>");
                out.println("<h2>General Request Information:</h2><br/>");
                out.println("<strong>Tracking Number:</strong> " + trackingNumber + "<br/>" );
                out.println("There is <strong>" + rliCount + "</strong> line item(s) in this request<br/>" );

                if (rliCount > 1 && !displayAll) {
                    out.println(buildLink(request.getQueryString(), "Display all line items for this request") + "<br/>");
                }
                out.println("<hr/><br/>");

                int i = 0;
                if (displayAll) {
                    for (Iterator<RequestLineItem> rliIterator = rlis.iterator(); rliIterator.hasNext();) {
                        rli =  rliIterator.next();
                        displayRliInfo(++i,rliCount,out,rli,showAnyway, trackingNumber);
                    }
                }
                else {
                    i=-1;
                    displayRliInfo(++i,rliCount,out,rli,showAnyway, trackingNumber);
                }
                // Write HTML footer

                // Write System Administration information
                out.println("<br/><br/><br/><hr/>");
                out.println("<h3>Please ignore the following system administration information:</h3><br/>" );

                if (displayAll) {
                    for (Iterator<RequestLineItem> rliIterator = rlis.iterator(); rliIterator.hasNext();) {
                        rli =  rliIterator.next();
                        displayRliInfoTech(out,rli,showAnyway, trackingNumber);
                    }
                } else {
                    displayRliInfoTech(out,rli,showAnyway, trackingNumber);
                }

                out.println("</div></body></html>");
                out.flush();
                out.close();
            } else {
                response.sendRedirect(response.encodeRedirectURL("/viewEvaluateMaterialsRequest.do?requestId="+rli.getRequest().getRequestId())+ "&skin=reset")
            }
        } else {  //Request Line Item NOT found
            // Write HTML header
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            out.println("<html><head><title>Request Approval Status</title></head><body>");
            out.println("<br/>");
            out.println("The Request, For Item ID: <strong>" + requestLineItemId + " Does NOT exist");

            // Write HTML footer
            out.println("</body></html>");
            out.flush();
            out.close();
        }
    }

    private String buildLink(String queryString, String linkText){
        String environment = Environment.LOCALHOST_PRISM_URL;

        if (Environment.isProduction()) environment = Environment.PROD_PRISM_URL;
        else if (Environment.isTest())  environment = Environment.NONPROD_PRISM_URL;

        return "<a href='" + (Environment.isProduction() ? "https://" : "http://") + "${environment}/viewApprovalStatus?${queryString}&displayAllrlis=true&showIndicator=true'>${linkText}</a>";
    }

    private void displayRliInfo(int j, Integer rliCount, PrintWriter out, RequestLineItem rli, Boolean showAnyway, String trackingNumber) {
        Status status = rli.getStatus();
        Collection<MaterialsRequestEvaluation> requestEvaluations = rli.getRequestEvaluations();
        
        out.println("<br/>");
        if (j > 0) {
            out.println("<br/>========================");
            out.println("  Request Line Item <strong>" + j + "</strong> of <strong>" + rliCount + "</strong>  ");
            out.println("=========================<br/><br/>");
        }

        out.println("<strong>RequestLineItemId:</strong> " + rli.getRequestLineItemId() + "<br/>");
        if (rli.getItemDescription() != null) {
            out.println("<strong>Description:</strong> " + rli.getItemDescription() + "<br/>");
        } else if (rli.getItem().getDescription() != null) {
            out.println("<strong>Description:</strong> " + rli.getItem().getDescription() + "<br/>");
        } else {
            out.println("<strong>Description:</strong> BLANK");
        }

        out.println("<br/>");

        if (status.getStatusCode().equals(Status.APPROVED)) {
            out.println("<strong>HAS ALREADY BEEN APPROVED.</strong>");
            out.println("<br/><br/>&nbsp;&nbsp;&nbsp;<strong>Response(s):</strong><br />");
        } else if (status.getStatusCode().equals(Status.DENIED)) {
            out.println("<strong>HAS BEEN DENIED.</strong>");
            out.println("<br/><br/>&nbsp;&nbsp;&nbsp;<strong>Response(s):</strong><br />");
            out.println(rli.getDenialReason());
        } else if (status.getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
            out.println("<strong>IS WAITING FOR APPROVAL.</strong>");
            out.println("<br/><br/>&nbsp;&nbsp;&nbsp;<strong>Response(s):</strong><br />");
        } else  {
            out.println("<strong>IS NO LONGER WAITING FOR APPROVAL.</strong><br />");
        }

        out.println("<br/><hr/>");
        out.println("<br/><h2>Approval Group Information:</h2><br/>" );
        out.println("(Note that One or More Groups are listed below depending on how many Group Approvals are required for this request)<br/>" );
        out.println("<br/>----------------------------------------------------------------------------------</br>");

        Integer i = 0;
        MaterialsRequestEvaluation requestEvaluation;
        Group evalGroup;
        for (Iterator<MaterialsRequestEvaluation> iterator = requestEvaluations.iterator(); iterator.hasNext();) {
            requestEvaluation =  iterator.next();
            evalGroup = requestEvaluation.getEvaluatorGroup();

            out.println("&nbsp;&nbsp;&nbsp;Approval Group # " + (++i) + ": <strong>" + evalGroup.getGroupName()+"</strong><br/>");

            if (requestEvaluation.getEvaluationDecision().getName().equals("Waiting For Approval")) {
                out.println("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This Request Line Item is <strong>Waiting For Approval</strong> from this Approval Group.<br/>");
            }
            else if (requestEvaluation.getEvaluationDecision().getName().equals("Denied")) {
                out.println("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This Request Line Item has been <strong>DENIED</strong> for this Approval Group by "
                        + requestEvaluation.getEvaluator().getFirstAndLastName() + " on " + requestEvaluation.getEvaluationDate() + "<br/>");
            }
            else if (requestEvaluation.getEvaluationDecision().getName().equals("Approved")) {
                out.println("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This Request Line Item has been <strong>APPROVED</strong>  for this Approval Group by "
                        + requestEvaluation.getEvaluator().getFirstAndLastName() + " on " + requestEvaluation.getEvaluationDate() + "<br/>");
            }

            out.println("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>The following person(s) are able to grant approval for this Approval</strong><br/>");
            out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Group.  Please contact one of them if you need to send a reminder</strong><br/>");
            out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>to them to grant approval for your PRISM request.</strong>");
            for(Iterator<PersonGroupLink> iterator2 = evalGroup.getPersonGroupLinks().iterator();iterator2.hasNext();) {
                    PersonGroupLink personGroupLink = iterator2.next();
                    String outString = "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + personGroupLink.getPerson().getFirstAndLastName();

                    if (personGroupLink.getPerson().getWorkLandPhone() != null)   outString += " Phone " + personGroupLink.getPerson().getWorkLandPhone().getNumber();
                    out.println(outString);
            }

            out.println("<br/><br/>--------------------------------------------------------------------------------------<br/>");
        }
        if (j > 0) {
            out.println("<br/><strong>========================================================================================</strong>");
        }
    }

    private  void displayRliInfoTech(PrintWriter out, RequestLineItem rli, Boolean showAnyway, String trackingNumber) {
        Status status = rli.getStatus();
        Collection<MaterialsRequestEvaluation> requestEvaluations = rli.getRequestEvaluations();

        out.println("The Request, For Item ID: <strong>" + rli.getRequestLineItemId() + "</strong><br />");
        out.println("Tracking Number: <strong>" + trackingNumber + "</strong><br />");
        if (rli.getItemDescription() != null) {
            out.println("Description: <strong>" + rli.getItemDescription() + "</strong><br/>");
        } else if (rli.getItem().getDescription() != null) {
            out.println("Description: <strong>" + rli.getItem().getDescription() + "</strong><br/>");
        } else {
            out.println("Description: <strong>BLANK</strong>");
        }

        out.println("Status Code: " + status.getStatusCode() + "<br/>") ;
        out.println("Status Name: " + status.getName() + "<br/><br/>") ;


        if (status.getStatusCode().equals(Status.APPROVED)) {
            out.println("<strong>HAS ALREADY BEEN APPROVED.</strong>");
            out.println("<br /><br />&nbsp;&nbsp;&nbsp;<strong>Response(s):</strong><br/>");
        } else if (status.getStatusCode().equals(Status.DENIED)) {
            out.println("<strong>HAS BEEN DENIED.</strong>");
            out.println("<br /><br />&nbsp;&nbsp;&nbsp;<strong>Response(s):</strong><br/>");
        } else if (status.getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
            out.println("<strong>IS WAITING FOR APPROVAL.</strong>");
            out.println("<br /><br />&nbsp;&nbsp;&nbsp;<strong>Response(s):</strong><br/>");
        } else  {
            out.println("<strong>IS NO LONGER WAITING FOR APPROVAL.</strong><br/>");
        }

        RequestEvaluation requestEvaluation;
        Group evalGroup;
        for (Iterator<MaterialsRequestEvaluation> iterator = requestEvaluations.iterator(); iterator.hasNext();) {
            requestEvaluation =  iterator.next();
            out.println("<br/>&nbsp;&nbsp;&nbsp;" + requestEvaluation.toString().replace("\n","<br />&nbsp;&nbsp;&nbsp;")+"<br/>");
            evalGroup = requestEvaluation.getEvaluatorGroup();

            out.println("<br/><strong>Person(s) able to approve from " + evalGroup.getGroupCode() + "<br/>");
            for(Iterator<PersonGroupLink> iterator2 = evalGroup.getPersonGroupLinks().iterator();iterator2.hasNext();) {
                    PersonGroupLink personGroupLink = iterator2.next();
                    String outString = "<br/>&nbsp;&nbsp;&nbsp;&nbsp;" + personGroupLink.getPerson().getFirstAndLastName();

                    if (personGroupLink.getPerson().getWorkLandPhone() != null)   outString += " Phone " + personGroupLink.getPerson().getWorkLandPhone().getNumber();
                    out.println(outString);
            }
            out.println("<br/>-----------------------------------------<br/>");
        }
    }
}