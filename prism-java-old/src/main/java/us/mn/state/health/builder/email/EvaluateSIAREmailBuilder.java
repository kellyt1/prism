package us.mn.state.health.builder.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.Utility;
import us.mn.state.health.view.inventory.StockItemActionRequestForm;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.inventory.HibernateStockItemDAO;
import static us.mn.state.health.dao.DAOFactory.*;
import org.hibernate.Hibernate;

/**
 * This class is used for creation of the email notification when an evaluator
 * evaluates a SIAR
 * The email notifies the primary and the secondary contact
 */

public class EvaluateSIAREmailBuilder implements EmailBuilder {
    private static Log log = LogFactory.getLog(EvaluateSIAREmailBuilder.class);
    public static final String stockItemIcnbr = "<br><br> Stock Item ICNBR: ";
    public static final String approveNewSIARSubject = "New Stock Item Request Approved, Request # ";
    public static final String denyNewSIARSubject = "New Stock Item Request Denied, Request #";
    public static final String approveSIARChangeSubject = "Stock Item Change Request Approved, Request # ";
    public static final String denySIARChangeSubject = "Stock Item Change Request Denied, Request # ";

    public static final String request = "<br><br> Request #";
    public static final String status = "<br><br> Status: ";
    public static final String denialReason = "<br><br> Denial Reason: ";
    public static final String siDescription = "<br><br> Stock Item Description: ";

    public static final String approved = "<b>Approved</b>";
    public static final String denied = "<b>Denied</b>";

    public static final String type = "Type of request: ";
    public static final String comments = "<br><br> Comments: ";
    public static final String primaryContact = "<br><br> Primary Contact: ";
    public static final String secondaryContact = "<br><br> SecondaryContact: ";

    public static final int approve_newSIAR = 1;
    public static final int approve_SIARChange = 2;
    public static final int deny_newSIAR = 3;
    public static final int deny_SIARChange = 4;

    private StockItemActionRequestForm siarForm;
    private EmailBean emailBean;
    private StockItemActionRequest siar;

    public EvaluateSIAREmailBuilder(StockItemActionRequestForm siarForm, Person evaluator, EmailBean emailBean) {
        this.siarForm = siarForm;
        this.emailBean = emailBean;
    }

    public EvaluateSIAREmailBuilder(StockItemActionRequestForm siarForm, Person evaluator, EmailBean emailBean, StockItemActionRequest siar) {
        this(siarForm, evaluator,emailBean);
        this.siar = siar;
    }

    public void buildSubject() {
        String subject = "";
        int SIARcase = getSIARCase();
        switch (SIARcase) {
            case 1:
                {
                    subject = approveNewSIARSubject + siarForm.getStockItemActionRequestId();
                    break;
                }
            case 2:
                {
                    subject = approveSIARChangeSubject + siarForm.getStockItemActionRequestId();
                    break;
                }
            case 3:
                {
                    subject = denyNewSIARSubject + siarForm.getStockItemActionRequestId();
                    break;
                }
            case 4:
                {
                    subject =  denySIARChangeSubject + siarForm.getStockItemActionRequestId();
                    break;
                }
        }
        emailBean.setSubject(subject);
    }

    public void buildMessage() {
        int SIARcase = getSIARCase();
        String message = "";

        switch (SIARcase) {
            case 1:
                {
                    message =  getApprovalMessage();
                    break;
                }
            case 2:
                {
                    message =  getApprovalMessage();
                    break;
                }
            case 3:
                {
                    message = getDenialMessage();
                    break;
                }
            case 4:
                {
                    message = getDenialMessage();
                    break;
                }
        }
        emailBean.setMessage(message);
    }

    public void buildTo() throws InfrastructureException {
        StringBuffer to = new StringBuffer();
        to.append(Utility.createEmailAddress(siarForm.getRequestor()));
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        PersonDAO personDAO = factory.getPersonDAO();

        Long lPersonID;

        try {
            lPersonID = Long.valueOf(siarForm.getPotentialStockItemForm().getPrimaryContactId());
            to.append(Utility.createEmailAddress(personDAO.getPersonById(lPersonID,false)));
        } catch (NumberFormatException e) {
            log.error("Not an integer  Evaluate Siar " + siarForm.getPotentialStockItemForm().getPrimaryContactId() );
        } catch (InfrastructureException e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        to.append(Utility.createEmailAddress(siarForm.getPotentialStockItemForm().getPrimaryContactForm()));
//        to.append(Utility.createEmailAddress(siarForm.getPotentialStockItemForm().getSecondaryContactForm()));
        try {
            lPersonID = Long.valueOf(siarForm.getPotentialStockItemForm().getSecondaryContactId());
            to.append(Utility.createEmailAddress(personDAO.getPersonById(lPersonID,false)));
        } catch (NumberFormatException e) {
            log.error("Not an integer  Evaluate Siar " + siarForm.getPotentialStockItemForm().getSecondaryContactId() );
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InfrastructureException e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        emailBean.setTo(to.toString());
    }

    public void buildFrom() throws InfrastructureException {
//        String from = Utility.createEmailAddress(evaluator);
//        emailBean.setFrom(from);
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {
    }

    public void buildBcc() {
        //TODO remove this when this goes to production
//        emailBean.setBcc("lucian.ochian@state.mn.us");
    }

    private String buildComments() {
        return comments + (siar.getSpecialInstructions() == null ? "": siar.getSpecialInstructions());
    }

    private String buildPrimaryContact() {
      return primaryContact + (siar.getPotentialStockItem().getPrimaryContact().getFirstAndLastName());
    }

    private String buildSecondaryContact() {
      return secondaryContact + (siar.getPotentialStockItem().getSecondaryContact().getFirstAndLastName());
    }

    private String buildICNBR() {
        return stockItemIcnbr + siar.getPotentialStockItem().getFullIcnbr();
//        return stockItemIcnbr + siar.getPotentialStockItem().getIcnbr();
    }

    private String getApprovalMessage() {
        StringBuffer message = new StringBuffer();
        message.append(type).append(siar.getActionRequestType().getName())
               .append(buildStockItemDescription())
               .append(buildRequestNumber())
               .append(buildComments())
               .append(buildPrimaryContact())
               .append(buildSecondaryContact())
               .append(buildStatus())
               .append(buildICNBR());
        return message.toString();

    }

    // This the format of message sent for a New StockItem Request email
//    StringBuffer message = new StringBuffer();
//    message.append(type).append(siar.getActionRequestType().getName());
//    message.append(siDescription).append(siar.getPotentialStockItem().getDescription());
//    message.append(request).append(siar.getStockItemActionRequestId());
//    message.append(comments).append(siar.getSpecialInstructions());
//    message.append(primaryContact).append(siar.getPotentialStockItem().getPrimaryContact().getFirstAndLastName());
//    message.append(secondaryContact).append(siar.getPotentialStockItem().getSecondaryContact().getFirstAndLastName());
//    emailBean.setMessage(message.toString());


    private String getDenialMessage() {
        StringBuffer message = new StringBuffer();
        message.append(type).append(siar.getActionRequestType().getName())
               .append(buildStockItemDescription())
               .append(buildRequestNumber())
               .append(buildComments())
               .append(buildPrimaryContact())
               .append(buildSecondaryContact())
               .append(buildStatus())
               .append(buildDenialReason());
        return message.toString();
    }


    private String buildRequestNumber() {
        return request + siarForm.getStockItemActionRequestId();
    }

    private String buildStatus() {
        return status + (siarForm.getApproved().booleanValue() ? approved : denied);
    }

    private String buildStockItemDescription() {
        return siDescription + siarForm.getPotentialStockItemForm().getDescription();
    }

    private String buildDenialReason() {
        return denialReason + siarForm.getEvaluationReason();
    }

    private int getSIARCase() {
        String actionRequestTypeCode = siarForm.getActionRequestType().getCode();
        if (actionRequestTypeCode.equals(ActionRequestType.NEW_STOCK_ITEM)
                && siarForm.getApproved().booleanValue()) {
            return approve_newSIAR;
        }
        else {
            if (actionRequestTypeCode.equals(ActionRequestType.STOCK_ITEM_MODIFICATIONS)
                    && siarForm.getApproved().booleanValue()) {
                return approve_SIARChange;
            }
            else {
                if (actionRequestTypeCode.equals(ActionRequestType.NEW_STOCK_ITEM)
                        && (!siarForm.getApproved().booleanValue())) {
                    return deny_newSIAR;
                }
                else {
                    return deny_SIARChange;
                }
            }
        }
    }

}
