package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.email.EmailBean;

import java.util.Iterator;

/**
 * This class is used for creation of the email notification when an evaluator
 * evaluates a materials request action for one request line item
 * The email notifies the requestor
 */

public class BudgetManagerEmailBuilder implements EmailBuilder{
    public static final String request = "<br><br> Tracking Number #";
    public static final String AC2Code = "<br><br> AC2 Code: ";
    public static final String RLIAmount = "<br><br> Amount: ";
    public static final String RLICategory = "<br><br> Category: ";
    public static final String liDescription = "<br><br> Description: ";
    public static final String liJustification = "<br><br> Justification: ";
    public static final String status = "<br><br> Status: ";
    public static final String evaluators = "<br><br> Budget Manager ";
    public static final String evaluateMaterialsRequestSubject = "Request with encumbrance change Tracking Number # ";
    private RequestLineItem rli;
    private EmailBean emailBean;
    private DAOFactory factory;
    private String extraMessage;

    public BudgetManagerEmailBuilder(RequestLineItem rli, EmailBean emailBean, DAOFactory factory) {
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
        message.append(AC2Code).append(rli.getSwiftItemId());
        message.append(RLIAmount).append(rli.getItemCost() * rli.getQuantity());
        message.append(RLICategory).append(rli.getItemCategory().getName());
        message.append(liDescription).append(rli.getItemDescription());
        if (rli.getItemJustification() != null) {
            message.append(liJustification).append(rli.getItemJustification());
        }


        message.append(status);
        message.append(" has a change in amount encumbered please adjust MNIT accounts accordingly ");

//        message.append("<br/><br/>Message: ".concat(extraMessage));
        message.append(evaluators);
        
        emailBean.setMessage(message.toString());
    }

    public void buildTo() throws InfrastructureException {
        StringBuffer to = new StringBuffer();
//        to.append(Utility.createEmailAddress(stockItem.getPrimaryContact()));
//        to.append(Utility.createEmailAddress(stockItem.getSecondaryContact()));
        try {
            Group stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                    .getGroupDAO()
                    .findGroupByCode(Group.IT_BUDGET_EVALUATORS);
            for(Iterator iter = stkController.getPersonGroupLinks().iterator(); iter.hasNext(); ) {
                PersonGroupLink pgl = (PersonGroupLink)iter.next();
                Person person = pgl.getPerson();
//                to.append(Utility.createEmailAddress(person));
                //to.append(person.getNdsUserId()).append("@health.state.mn.us,");
                to.append(person.getEmailAddressPrimary());
                if (iter.hasNext()) {
                    to.append(",");
                }
            }
        }
        catch(Exception e) {
            //consume excpetion here. Don't let a problem here
            //crash the system. Its not THAT important. But do log it.
            //log.error("Exception in buildTo(): ", e);
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

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }
}