package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.Utility;

/**
 * This class is used for creation of the email notification when an evaluator
 * evaluates a materials request action for one request line item
 * The email notifies the requestor
 */

public class ReopenRequestLineItemEmailBuilder implements EmailBuilder{
    public static final String request = "<br><br> Tracking Number #";
    public static final String liDescription = "<br><br> Line Item Description: ";
    public static final String status = "<br><br> Status: ";
    public static final String evaluators = "<br><br> Purchasing ";
    public static final String evaluateMaterialsRequestSubject = "Request reopened by purchasing, Tracking Number # ";
    private RequestLineItem rli;
    private EmailBean emailBean;
    private DAOFactory factory;
    private String extraMessage;

    public ReopenRequestLineItemEmailBuilder(RequestLineItem rli, EmailBean emailBean, DAOFactory factory) {
        this.rli = rli;
        this.emailBean = emailBean;
        this.factory = factory;
    }

    public void buildSubject() {
        emailBean.setSubject(evaluateMaterialsRequestSubject + rli.getRequest().getTrackingNumber());
    }

    public void buildMessage() throws InfrastructureException {
        StringBuffer message = new StringBuffer();
        message.append(request).append(rli.getRequest().getTrackingNumber());
        message.append(liDescription).append((rli.getItem() == null ) ? rli.getItemDescription() : rli.getItem().getDescription());
        message.append(status);
        message.append("has been reopened and is in need of more information. Please go to my requests and provide the additional information. ");

        message.append("<br/><br/>Message: ".concat(extraMessage));
//        message.append(status).append(rli.getStatus().getName());
        //message.append(rli.getStatus().getName());
        message.append(evaluators);
        
        emailBean.setMessage(message.toString());
    }

    public void buildTo() throws InfrastructureException {
        Person requestor = rli.getRequest().getRequestor();
        String to = Utility.createEmailAddress(requestor);
        emailBean.setTo(to);
    }

    public void buildFrom() throws InfrastructureException {
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {

    }

    public void buildBcc() {

    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }
}