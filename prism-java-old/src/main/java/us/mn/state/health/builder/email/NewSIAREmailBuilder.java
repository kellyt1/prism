package us.mn.state.health.builder.email;

import java.util.List;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.Utility;
import us.mn.state.health.persistence.HibernateUtil;

/**
 * This class is used for creation of the email notification when a requestor
 * request a new Stock Item
 * The email notifies all the buyers
 */

public class NewSIAREmailBuilder implements EmailBuilder{
    private StockItemActionRequest siar;
    private EmailBean emailBean;
    private Person user;

    public static final String subject = "PRISM Stock Item Action Request submitted";
    public static final String type = "Type of request: ";
    public static final String request = "<br><br> Request #";
    public static final String siDescription = "<br><br> Stock Item Description: ";
    public static final String comments = "<br><br> Comments: ";
    public static final String primaryContact = "<br><br> Primary Contact: ";
    public static final String secondaryContact = "<br><br> SecondaryContact: ";

    public NewSIAREmailBuilder(StockItemActionRequest siar,Person user, EmailBean emailBean) {
        this.siar = siar;
        this.emailBean = emailBean;
        this.user = user;
        try {
            HibernateUtil.refresh(siar.getPotentialStockItem().getPrimaryContact());
            HibernateUtil.refresh(siar.getPotentialStockItem().getSecondaryContact());
        } 
        catch (InfrastructureException e) {
            e.printStackTrace();
        }
    }


    public void buildSubject() {
        emailBean.setSubject(subject + " #: " + siar.getStockItemActionRequestId());
    }

    public void buildMessage() {
        StringBuffer message = new StringBuffer();
        message.append(type).append(siar.getActionRequestType().getName());
        message.append(siDescription).append(siar.getPotentialStockItem().getDescription());
        message.append(request).append(siar.getStockItemActionRequestId());
        message.append(comments).append(siar.getSpecialInstructions());
        message.append(primaryContact).append(siar.getPotentialStockItem().getPrimaryContact().getFirstAndLastName());
        message.append(secondaryContact).append(siar.getPotentialStockItem().getSecondaryContact().getFirstAndLastName());
        emailBean.setMessage(message.toString());
    }

    public void buildTo() throws InfrastructureException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        PersonDAO personDAO = factory.getPersonDAO();
//        List buyers = personDAO.findPersonsByGroupCode(Group.BUYER_CODE);
        List buyers = personDAO.findPersonsByGroupCode(Group.STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE);
        String to = Utility.createEmailAddresses(buyers);
        emailBean.setTo(to);
    }

    public void buildFrom() throws InfrastructureException {
//        String from = Utility.createEmailAddress(user);
//        emailBean.setFrom(from);
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {

    }

    public void buildBcc() {
        //TODO remove this when this goes to production
//        emailBean.setBcc("lucian.ochian@state.mn.us");
    }
}
