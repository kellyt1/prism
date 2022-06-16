package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.Utility;

import java.util.Iterator;
import java.util.List;

/**
 * This class is used for creation of the email notification when an evaluator
 * evaluates a materials request action for one request line item
 * The email notifies the requestor
 */

public class EvaluateRequestLineItemEmailBuilder implements EmailBuilder{
    public static final String request = "<br><br> Tracking Number #";
    public static final String helpdesknumber = "<br><br> Help Desk ID #";
    public static final String liDescription = "<br><br> Line Item Description: ";
    public static final String status = "<br><br> Status: ";
    public static final String evaluators = "<br><br> Evaluators: ";
    public static final String denialReason = "<br><br> Denial Reason: ";
    public static final String evaluateMaterialsRequestSubject = "Evaluation Result of Materials Request Action, Tracking Number # ";
    private RequestLineItem rli;
    private EmailBean emailBean;
    private DAOFactory factory;

    public EvaluateRequestLineItemEmailBuilder(RequestLineItem rli, EmailBean emailBean, DAOFactory factory) {
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
        if (rli.getRequest().getTrackingNumber() != null) {
            if (rli.getRequest().getHelpDeskticketId() != null) {
                message.append(helpdesknumber).append(rli.getRequest().getHelpDeskticketId());
            }
        }
        message.append(liDescription).append((rli.getItem() == null ) ? rli.getItemDescription() : rli.getItem().getDescription());
        message.append(status);
        if (rli.getStatus().getStatusCode().equals(rli.getStatus().WAITING_FOR_PURCHASE)) {
          message.append("has been Approved and is now ");
        }
        else if (rli.getStatus().getStatusCode().equals(rli.getStatus().WAITING_FOR_DISPERSAL)) {
          message.append("has been Approved, is on hand and is now ");
        }
//        message.append(status).append(rli.getStatus().getName());
        message.append(rli.getStatus().getName());
        if (rli.getDenialReason() != null && !rli.getDenialReason().equals("")) {
            message.append(denialReason).append(rli.getDenialReason());
        }
        message.append(evaluators).append(getEvaluatorsNames());
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

    private String getEvaluatorsNames() throws InfrastructureException {
        StringBuffer evaluatorsNames = new StringBuffer();
        List evaluators = Utility.getEvaluators(rli, factory.getPersonDAO());
        Iterator iterator = evaluators.iterator();
        while(iterator.hasNext()){
            Person evaluator = (Person) iterator.next();
            String tempName = " " + evaluator.getFirstAndLastName() + ",";
            // We check to see if we don't have a evaluator more than 1 time
            if (evaluatorsNames.indexOf(tempName)== -1 ) {
                evaluatorsNames.append(" ").append(evaluator.getFirstAndLastName()).append(",");
            }
        }
        evaluatorsNames.deleteCharAt(evaluatorsNames.lastIndexOf(","));
        return evaluatorsNames.toString();
    }
}
