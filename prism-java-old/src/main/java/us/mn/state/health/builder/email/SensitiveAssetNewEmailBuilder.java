package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.util.Environment;

import java.util.Iterator;

/**
 * This class is used for creation of the email notification when a requestor
 * requests a line item
 * The email notifies the appropriate evaluators
 */

public class SensitiveAssetNewEmailBuilder implements EmailBuilder {
    public static final String request_message = "<br><br> A new sensitive asset has been put into the system.";
    public static final String link1 = "<br><br>";
    public static final String request = "<br><br> PO #: ";
    public static final String liDescription = "<br><br> Line Item Description: ";
    public static final String purchaser = "<br><br> Purchaser: ";
    public static final String status = "<br><br> Status: ";

    public static final String orderLineItemSubject = "New Sensitive Asset PO#:";

    private OrderLineItem oli;
    private EmailBean emailBean;
    private DAOFactory factory;

    public SensitiveAssetNewEmailBuilder(OrderLineItem oli, EmailBean emailBean, DAOFactory factory) {
        this.oli = oli;
        this.emailBean = emailBean;
        this.factory = factory;
    }

    public void buildSubject() {

        emailBean.setSubject(orderLineItemSubject + oli.getOrder().getPurchaseOrderNumber());
    }

    public void buildMessage() throws InfrastructureException {
        StringBuffer message = new StringBuffer();
        message.append(request_message);

        message.append(link1).append(buildLink());
        message.append(request).append(oli.getOrder().getPurchaseOrderNumber());
        //TODO when we have a non catalog item, is oli.getItem() null?
        message.append(liDescription).append((oli.getItem() == null) ? oli.getItemDescription() : oli.getItem().getDescription());
        message.append(status).append(oli.getStatus().getName());
//        message.append(orgforapproval).append(oli.getFundingSrcSummary().getOrgBudgetCodes());
        message.append(purchaser).append(oli.getOrder().getPurchaser().getFirstAndLastName());
//        Collection requestEvaluations = RequestLineItem.getRequestEvaluations();
        StringBuffer groupInformation = new StringBuffer();
        groupInformation.append(emailBean.getGroupInformaton());
//        for (Iterator iterator = requestEvaluations.iterator(); iterator.hasNext();) {
//            Object o =  iterator.next();
//            groupInformation.append("\n\n").append(o.toString());
//        }
        emailBean.setGroupInformaton(groupInformation.toString());
        emailBean.setMessage(message.toString());
    }

    public void buildTo() throws InfrastructureException {
        StringBuffer to = new StringBuffer();
//        to.append(Utility.createEmailAddress(stockItem.getPrimaryContact()));
//        to.append(Utility.createEmailAddress(stockItem.getSecondaryContact()));
        try {
            Group stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                                            .getGroupDAO()
                                            .findGroupByCode(Group.FIXED_ASSET_CONTROLLER);
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
//        String from = Utility.createEmailAddress(oli.getRequest().getRequestor());
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {

    }

    public void buildBcc() {
        //TODO remove this when this goes to production
//        emailBean.setBcc("lucian.ochian@state.mn.us");
    }

    private String buildLink() throws InfrastructureException {
        StringBuffer buffer = new StringBuffer();
        StringBuffer address = new StringBuffer();
        try {
            buffer.append("<a href='");
            address.append("http://");
            //address.append(InetAddress.getLocalHost().getHostName());
            // Due to the configuration of the machine,   we have had to hardcode in the path. JMP 11/23/2006

            if (Environment.isProduction()) {   //is Production
                address.append(Environment.PROD_PRISM_URL);
            } else if (Environment.isTest()) {   //is Test
               address.append(Environment.NONPROD_PRISM_URL);
            } else {  // localhost??
               address.append(Environment.LOCALHOST_PRISM_URL);
            }

//            address.append("prism.web.health.state.mn.us");
//            address.append("prism.nonprod.health.state.mn.us");
//            address.append("localhost:8080");

//            address.append("/viewMaterialsRequests.do");
            address.append("/viewEditOrder.do");
            address.append("?orderId="+ oli.getOrder().getOrderId());
            buffer.append(address);
            buffer.append("'>");
            buffer.append(address);
            buffer.append("</a>");
        }
        catch (Exception e) {
//do nothing! We do not want to let the email stuff crash the system.
            //if it doesn't work, oh well...
            //throw new InfrastructureException(e);
        }
//        catch (UnknownHostException e) {
//            //do nothing! We do not want to let the email stuff crash the system.
//            //if it doesn't work, oh well...
//            //throw new InfrastructureException(e);
//        }
        return buffer.toString();
    }

}