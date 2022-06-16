package us.mn.state.health.builder.email;

import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.Utility;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;

/**
 * This class is used for creation of the email notification when a requestor
 * request a new Stock Item
 * The email notifies all the buyers
 */

public class FillStockRequestEmailBuilder implements EmailBuilder {
    private RequestForm requestForm;
    private EmailBean emailBean;

    public static final String subject = "Stock Item request has been filled";

    public static final String requestNbr = "<br><br> Request Tracking Number: ";
    public static final String itemDescriptionHeader = "<th><u><b>Stock Item Description:</b></u></th>";
    public static final String quantityRequestedHeader = "<th><u><b>Qty Requested:</b></u></th>";
    public static final String quantitySentHeader = "<th><u><b>Qty Shipped:</b></u></th>";

    public FillStockRequestEmailBuilder(RequestForm requestForm, EmailBean emailBean) {
        this.requestForm = requestForm;
        this.emailBean = emailBean;
    }

    public void buildSubject() {
        emailBean.setSubject(subject);
    }

    public void buildMessage() {
        StringBuffer message = new StringBuffer();
        message.append(requestNbr).append(requestForm.getTrackingNumber());
        message.append("<br /><br /><table><tr>");
        message.append(itemDescriptionHeader);
        message.append(quantityRequestedHeader);
        message.append(quantitySentHeader);
        message.append("</tr>");
        Iterator iter = requestForm.getRequestLineItemForms().iterator();
        while(iter.hasNext()){
            RequestLineItemForm rliForm = (RequestLineItemForm)iter.next();
            message.append("<tr>");
            message.append("<td align='left'>" + rliForm.getItem().getDescription() + "</td>");
            message.append("<td align='center'>" + rliForm.getQuantity() + "</td>");
            message.append("<td align='center'>" + rliForm.getQuantityPicked() + "</td>");
            message.append("</tr>");
        }
        message.append("</table>");
        emailBean.setMessage(message.toString());
    }

    public void buildTo() throws InfrastructureException {        
        emailBean.setTo(Utility.createEmailAddress(requestForm.getRequestor()));
    }

    public void buildFrom() throws InfrastructureException {
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {

    }

    public void buildBcc() {
        //TODO remove this when this goes to production
//        emailBean.setBcc("shawn.flahave@health.state.mn.us");
    }
}
