package us.mn.state.health.builder.email;

import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.dao.DAOFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

public class StockItemHasBeenDiscontinuedEmailBuilder implements EmailBuilder {
    public static Log log = LogFactory.getLog(StockItemHasBeenDiscontinuedEmailBuilder.class);
    private StockItemActionRequest stockItemActionRequest;
    private EmailBean emailBean;


    public StockItemHasBeenDiscontinuedEmailBuilder(StockItemActionRequest stockItemActionRequest, EmailBean emailBean) {
        this.stockItemActionRequest = stockItemActionRequest;
        this.emailBean = emailBean;
    }

    public void buildSubject() {
        StringBuilder subject = new StringBuilder();
        subject.append("Stock Item ").append(stockItemActionRequest.getStockItem().getFullIcnbr())
                .append(" has been set to ").append(stockItemActionRequest.getPotentialStockItem().getStatus().getName());
        emailBean.setSubject(subject.toString());
    }

    public void buildMessage() throws InfrastructureException {
        StringBuilder message = new StringBuilder();
        StockItem potentialSI = stockItemActionRequest.getPotentialStockItem();
        StockItem stockItem = stockItemActionRequest.getStockItem();
        message.append("<html>\n").
                append("<head>\n").append("</head>\n").
                append("<body>\n").
                append("<p><b>Details:</b></p>\n").append("<ul>\n").
                append("\t<li>ICNBR:").append(stockItem.getFullIcnbr()).append("</li>\n").
                append("\t<li>Description:").append(potentialSI.getDescription()).append("</li>\n").
                append("\t<li>Status:").append(potentialSI.getStatus().getName()).append("</li>\n");

        if (potentialSI.getStatus().getStatusCode().equals(Status.ONHOLD)) {
            message.append("\t<li>Hold Until Date:").append(potentialSI.getHoldUntilDate()).append("</li>\n").
                    append("\t<li>Reason For Hold:").append(stockItemActionRequest.getRequestReason()).append("</li>\n");

        }

        String specialInstructions = stockItemActionRequest.getSpecialInstructions();
        message.append("\t<li>Discard Existing Materials:").append(!potentialSI.getFillUntilDepleted()?"YES":"NO").append("</li>\n").
                append("\t<li>Dispense Unit:").append(potentialSI.getDispenseUnit().getName()).append("</li>\n").
                append("\t<li>Quantity On Hand:").append(potentialSI.getQtyOnHand()).append("</li>\n").
                append("\t<li>Current Demand:").append(stockItem.getCurrentDemand()).append("</li>\n").
                append("\t<li>Reorder Point:").append(potentialSI.getReorderPoint()).append("</li>\n").
                append("\t<li>Modification Comments:").append(StringUtils.isBlank(specialInstructions)?"":specialInstructions).append("</li>\n").
                append("</ul>\n").
                append("</body>\n").
                append("</html>");
        emailBean.setMessage(message.toString());
    }

    public void buildTo() throws InfrastructureException {
        StringBuffer to = new StringBuffer();
//        to.append(Utility.createEmailAddress(stockItem.getPrimaryContact()));
//        to.append(Utility.createEmailAddress(stockItem.getSecondaryContact()));
        try {
            Group stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                                            .getGroupDAO()
                                            .findGroupByCode(Group.STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE);
            for(Iterator iter = stkController.getPersonGroupLinks().iterator(); iter.hasNext(); ) {
                PersonGroupLink pgl = (PersonGroupLink)iter.next();
                Person person = pgl.getPerson();
//                to.append(Utility.createEmailAddress(person));
                to.append(person.getEmailAddressPrimary());  //.getNdsUserId()).append("@health.state.mn.us,");
            }
        }
        catch(Exception e) {
            //consume excpetion here. Don't let a problem here
            //crash the system. Its not THAT important. But do log it.
            log.error("Exception in buildTo(): ", e);
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
}
