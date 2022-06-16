package us.mn.state.health.builder.email

import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.dao.DAOFactory
import us.mn.state.health.dao.PersonDAO
import us.mn.state.health.model.common.Group
import us.mn.state.health.model.materialsrequest.RequestLineItem
import us.mn.state.health.model.util.email.EmailBean
import us.mn.state.health.model.util.email.Utility
import us.mn.state.health.util.Environment

/**
 * This class is used for creation of the email notification when a requestor
 * requests a line item
 * The email notifies the appropriate evaluators
 */

public class RequestLineItemEmailBuilder implements EmailBuilder, EmailBuilderBook {
    public static final String request_message = "<br><br> A request has been submitted and requires your approval. Click on the link to bring up PRISM.  If you are not taken " +
                                                 "directly to the approval for this item then choose the following menu option --> " +
                                                 "Request Goods & Services --> Evaluate Requests, to show any outstanding requests that are waiting for your approval.";
    public static final String request_message_book = "<br><br> A request for a book has been submitted.";
    public static final String request_message_conference_meeting = "<br><br> A request for a conference/meeting has been submitted and is ready for funding adjustments and Level Two Approval.";
    public static final String link1 = "<br><br>";
    public static final String request = "<br><br> Tracking Number #: ";
    public static final String liDescription = "<br><br> Line Item Description: ";
    public static final String requestor = "<br><br> Requestor: ";
    public static final String status = "<br><br> Status: ";
    public static final String orgforapproval = "<br><br> Budget Code: ";
    public static final String requestLineItemSubject = "Materials Request Action, Tracking Number # ";
    public static final String quantity = "<br><br> Quantity: ";

    private RequestLineItem rli;
    private EmailBean emailBean;
    private DAOFactory factory;
    private String skin="";

    public RequestLineItemEmailBuilder(RequestLineItem rli, EmailBean emailBean, DAOFactory factory) {
        this.rli = rli;
        this.emailBean = emailBean;
        this.factory = factory;
    }
    public RequestLineItemEmailBuilder(RequestLineItem rli, EmailBean emailBean, DAOFactory factory, String skin) {
        this(rli, emailBean, factory);
        this.skin=skin;
    }

    public void buildSubject() {
        emailBean.setSubject(requestLineItemSubject + rli.getRequest().getTrackingNumber());
    }

    public void buildMessage() throws InfrastructureException {
        StringBuffer message = new StringBuffer();
        message.append(request_message);

        //This link is not working correclty unless the user is already logged into Prism
        message.append(link1).append(buildLink());
        message.append(request).append(rli.getRequest().getTrackingNumber());
        message.append(liDescription).append((rli.getItem() == null) ? rli.getItemDescription() : rli.getItem().getDescription());
        message.append(status).append(rli.getStatus().getName());
        message.append(orgforapproval).append(rli.getFundingSrcSummary().getOrgBudgetCodes());
        message.append(requestor).append(rli.getRequest().getRequestor().getFirstAndLastName());
        Collection requestEvaluations = rli.getRequestEvaluations();
        StringBuffer groupInformation = new StringBuffer();
        groupInformation.append(emailBean.getGroupInformaton());
        for (Iterator iterator = requestEvaluations.iterator(); iterator.hasNext();) {
            Object o =  iterator.next();
            groupInformation.append("\n\n").append(o.toString());
        }
        emailBean.setGroupInformaton(groupInformation.toString());
        emailBean.setMessage(message.toString());
    }

    public void buildMessage(String groupCode) throws InfrastructureException {
        if (groupCode.equals("PRISM-BOOK-NOTIFICATION")) {
            StringBuffer message = new StringBuffer();
            message.append(request_message_book);

            message.append(request).append(rli.getRequest().getTrackingNumber());
            //TODO when we have a non catalog item, is rli.getItem() null?
            message.append(liDescription).append((rli.getItem() == null) ? rli.getItemDescription() : rli.getItem().getDescription());
            message.append(quantity).append(rli.getQuantity());
            message.append(status).append(rli.getStatus().getName());
            message.append(orgforapproval).append(rli.getFundingSrcSummary().getOrgBudgetCodes());
            message.append(requestor).append(rli.getRequest().getRequestor().getFirstAndLastName());
            Collection requestEvaluations = rli.getRequestEvaluations();
            StringBuffer groupInformation = new StringBuffer();
            groupInformation.append(emailBean.getGroupInformaton());
            for (Iterator iterator = requestEvaluations.iterator(); iterator.hasNext();) {
                Object o =  iterator.next();
                groupInformation.append("\n\n").append(o.toString());
            }
            emailBean.setGroupInformaton(groupInformation.toString());
            emailBean.setMessage(message.toString());
        } else if (groupCode.equals(Group.CONFERENCE_COORDINATORS)) {
            StringBuffer message = new StringBuffer();
            message.append(request_message_conference_meeting);

            message.append(request).append(rli.getRequest().getTrackingNumber());
            message.append(liDescription).append((rli.getItem() == null) ? rli.getItemDescription() : rli.getItem().getDescription());
            message.append(quantity).append(rli.getQuantity());
            message.append(status).append(rli.getStatus().getName());
            message.append(orgforapproval).append(rli.getFundingSrcSummary().getOrgBudgetCodes());
            message.append(requestor).append(rli.getRequest().getRequestor().getFirstAndLastName());
            Collection requestEvaluations = rli.getRequestEvaluations();
            StringBuffer groupInformation = new StringBuffer();
            groupInformation.append(emailBean.getGroupInformaton());
            for (Iterator iterator = requestEvaluations.iterator(); iterator.hasNext();) {
                Object o =  iterator.next();
                groupInformation.append("\n\n").append(o.toString());
            }
            emailBean.setGroupInformaton(groupInformation.toString());
            emailBean.setMessage(message.toString());
        }
    }

    public void buildTo() throws InfrastructureException {
        PersonDAO personDAO = factory.getPersonDAO();
        List evaluators = Utility.getEvaluators(rli, personDAO);
        String to = Utility.createEmailAddresses(evaluators);
        emailBean.setTo(to);
    }

    public void buildTo(String groupCode) throws InfrastructureException {
        PersonDAO personDAO = factory.getPersonDAO();
        List groupMembers = personDAO.findPersonsByGroupCode(groupCode);
        String to = Utility.createEmailAddresses(groupMembers);
        emailBean.setTo(to);
    }

    public void buildFrom() throws InfrastructureException {
        emailBean.setFrom(systemEmailAddress);
    }

    /*public void buildCc() throws InfrastructureException {
        String cc;
        if(skin != null && skin != ""){
            cc = Utility.createEmailAddress(rli.getRequest().getRequestor());
            emailBean.setCc(cc);
        }
    }*/

    public void buildCc(){}

    public void buildBcc() {
//        emailBean.setBcc("lucian.ochian@state.mn.us");
    }

    private String buildLink() throws InfrastructureException {
        StringBuilder buffer = new StringBuilder();
        StringBuilder address = new StringBuilder();
        try {
            buffer.append("<a href='");

            // Due to the configuration of the machine,   we have had to hardcode in the path. JMP 11/23/2006
            if (Environment.isProduction()) {
                address.append("https://");
            } else address.append("https://");

            
            if (Environment.isProduction()) {   //is Production
                address.append(Environment.PROD_PRISM_URL);
            } else if (Environment.isTest()) {   //is Test
               address.append(Environment.NONPROD_PRISM_URL);
            } else {  // localhost??
               address.append(Environment.LOCALHOST_PRISM_URL);
            }

            address.append("/viewApprovalStatus")
                    .append("?requestLineItemId=")
                    .append(rli.getRequestLineItemId())
                    .append("&trackingNumber=")
                    .append(rli.getRequest().getTrackingNumber());

            if (skin) {
                address.append("&skin=").append(skin);
            }

            buffer.append(address)
                    .append("'>")
                    .append(address)
                    .append("</a>")
        }
        catch (Exception ignore) {
            //do nothing! Don't let email stuff crash the system. if it doesn't work, oh well...
        }
        return buffer.toString();
    }
}