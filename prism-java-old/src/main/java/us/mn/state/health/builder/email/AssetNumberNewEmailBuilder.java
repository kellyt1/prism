package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;
import us.mn.state.health.view.purchasing.OrderNoteForm;

import java.util.Iterator;

/**
 * This class is used for creation of the email notification when an asset type
 *      indicating an asset that should be tracked with an assigned asset number
 *      is received.
 * The email notifies the Agency Asset Coordinator by sending an email to
 *      Health.AssetManagement@state.mn.us.
 * This email is to contain the following information:
 *      - Destination Facility
 *      - PO Number
 *      - Item Description
 *      - Asset Number assigned to at least ONE received item.
 *      - Notes
 *      - Buy Units Ordered (quantity)
 */

public class AssetNumberNewEmailBuilder implements EmailBuilder {
    public static final String assetMessage = "A trackable asset has been received.";
    public static final String destinationTxt = "Destination Facility:";
    public static final String poNumTxt = "PO Number: ";
    public static final String liDescriptionTxt = "Line Item Description: ";
    public static final String assetNumberTxt = "Asset Number: ";
    public static final String numberOrderedTxt = "Buy Units Ordered: ";
    public static final String notes = "Notes: ";
    public static final String agencyAssetCoordEmail = "Health.AssetManagement@state.mn.us";
    public static final String orderLineItemSubject = "Trackable Asset Received";
    public static final String messageDiv = "===============================";
    public static final String testAddress = "Mike.Driscoll@state.mn.us";

    private OrderLineItem oli;
    private EmailBean emailBean;
    private OrderLineItemForm oliForm;
    private OrderForm orderForm;

    public AssetNumberNewEmailBuilder(OrderForm orderForm, OrderLineItemForm oliForm, EmailBean emailBean) {
        this.emailBean = emailBean;
        this.orderForm = orderForm;
        this.oliForm = oliForm;
        this.oli = oliForm.getOrderLineItem();
    }

    public void buildSubject() {
        emailBean.setSubject(orderLineItemSubject);
    }

    public void buildMessage() throws InfrastructureException {
        StringBuffer message = new StringBuffer();
        String nLine = "<br>";
        String n2Line = "<br><br>";
        message.append(messageDiv + nLine + assetMessage + nLine + messageDiv + n2Line);
        message.append(destinationTxt + nLine).append(oli.getOrder().getShipToAddress().getShortSummaryInOneLine() + n2Line);
        message.append(poNumTxt).append(oli.getOrder().getPurchaseOrderNumber() + nLine);
        message.append(liDescriptionTxt + nLine).append((oli.getItem() == null) ? oli.getItemDescription() : oli.getItem().getDescription() + nLine);
        message.append(n2Line + assetNumberTxt).append(oliForm.getAssetNumber() + n2Line);
        message.append(notes + nLine).append(getNotes());
        message.append(nLine + numberOrderedTxt).append(oli.getQuantity() + nLine);
        emailBean.setMessage(message.toString());

    }

    public void buildTo() throws InfrastructureException {
        emailBean.setTo(agencyAssetCoordEmail);
    }


    public void buildFrom() throws InfrastructureException {
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {

    }

    public void buildBcc() {
    }

    private String getNotes(){
        StringBuilder notes = new StringBuilder();
        for (Iterator iterator = orderForm.getOrderNoteForms().iterator(); iterator.hasNext();) {
            OrderNoteForm note = (OrderNoteForm) iterator.next();
            notes.append(note.getNote()).append("<br>");
        }

        return notes.toString();
    }

}