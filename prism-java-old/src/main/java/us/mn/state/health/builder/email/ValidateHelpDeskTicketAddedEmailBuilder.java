package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.util.Environment;

/**
 * This class is used for creation of the email notification after a computer purchase is approved
 * It will send in the help desk ticket # as well as the requestor to send information to
 *
 */

public class ValidateHelpDeskTicketAddedEmailBuilder implements EmailBuilder {
    public static final String request_message = "<br><br> A help desk ticket has been created for ";
    public static final String link1 = "<br><br>";
    public static final String request = "<br><br> PO #: ";
    public static final String liDescription = "<br><br> Line Item Description: ";
    public static final String purchaser = "<br><br> Purchaser: ";
    public static final String status = "<br><br> Status: ";

    public static final String HELPDESKNONPROD = "fyi.nonprod.health.state.mn.us/login/fusebox/";
    public static final String HELPDESKPROD = "fyi.web.health.state.mn.us/login/fusebox/";

    public static final String EMAIL_SUBJECT = "Computer Purchase approved for: ";

    private Request req;
    private EmailBean emailBean;
    private DAOFactory factory;
    private String trackingID;
    private String emailAddress;

    public ValidateHelpDeskTicketAddedEmailBuilder(Request req, EmailBean emailBean, DAOFactory factory) {
        this.req = req;
        this.emailBean = emailBean;
        this.factory = factory;
//        this.trackingID = helpdeskID;
    }

    public void buildSubject() {
        emailBean.setSubject(EMAIL_SUBJECT + req.getTrackingNumber());
    }

    public void buildMessage() throws InfrastructureException {
        emailBean.setMessage(request_message + " " + req.getTrackingNumber() + link1 + buildLink());
    }

    public void buildTo() throws InfrastructureException {
        StringBuilder to = new StringBuilder();
        try {
            to.append(emailAddress);
        }
        catch(Exception ignore) {
            //consume excpetion here. Don't let a problem here
            //crash the system. Its not THAT important. But do log it.
        }
        emailBean.setTo(to.toString());

    }


    public void buildFrom() throws InfrastructureException {
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {

    }

    public void buildBcc() {

    }

    private String buildLink() throws InfrastructureException {
        StringBuilder buffer = new StringBuilder();
        StringBuilder address = new StringBuilder();
        try {
            address.append("https://");

            if (Environment.isProduction()) {
                address.append(HELPDESKPROD);
            } else if (Environment.isTest()) {
                address.append(HELPDESKNONPROD);
            } else {
                // Environment is either dev or localhost.
                address.append(HELPDESKNONPROD);
            }

            address.append("helpdeskstatus/index.cfm?fuseaction=GetStatus&TRACKING_ID=").append(trackingID);
            buffer.append("<a href='")
                    .append(address)
                    .append("'>")
                    .append(address)
                    .append("</a>");
        }
        catch (Exception ignore) {
            //Do nothing! We do not want to let the email stuff crash the system.
            //if it doesn't work, oh well...
        }
        return buffer.toString();
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}