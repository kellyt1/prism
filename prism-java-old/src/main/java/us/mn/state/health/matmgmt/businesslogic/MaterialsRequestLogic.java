package us.mn.state.health.matmgmt.businesslogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.RequestLineItemEmailBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.persistence.HibernateUtil;

public class MaterialsRequestLogic  {
    public static Log log = LogFactory.getLog(MaterialsRequestLogic.class);
    
    /**
     * Handle the business of submitting a consumption request.
     * TODO: add business validations here somehow. This should be the last 
     * stand, the last means of making sure that all the requirements of a Request 
     * are met - it must have at least one RequestLineItem, it must have a DeliveryDetail, 
     * it must have a Requestor, each RLI must have at least one funding source unless its 
     * for an 'indirect' stock item, etc... ??
     * 
     * @throws java.lang.Exception
     * @param //qtyPicked
     * @param //rli
     */
    public static void submitMaterialsRequestORCL(Request requestObj) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        try {
            requestObj.executeBusinessRules();
            requestObj.assignTrackingNumber();
            daoFactory.getRequestDAO().makePersistent(requestObj);
            daoFactory.commitTransaction(false);
            HibernateUtil.closeSession();
        }
        catch(Exception e) {
            log.error("Error in submitMaterialsRequest(): ", e);
            throw new Exception(e);
        }

        sendEmailToEvaluators(requestObj);

    }
    public static void submitMaterialsRequest(Request requestObj) throws Exception {     
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        try {                    
            requestObj.executeBusinessRules();
            requestObj.assignTrackingNumber();
            daoFactory.getRequestDAO().makePersistent(requestObj);
            daoFactory.commitTransaction(false);
            HibernateUtil.closeSession();
        }       
        catch(Exception e) {
            log.error("Error in submitMaterialsRequest(): ", e);
            throw new Exception(e);
        }
        
        sendEmailToEvaluators(requestObj);
    }
    
    /**
     * This method sends emails in order to notify the evaluators that a materials action request has been made and each
     * request line item needs evaluation
     *
     * @param shoppingCart
     * @throws InfrastructureException
     */
    private static void sendEmailToEvaluators(Request shoppingCart) throws InfrastructureException {
        Collection emails = new ArrayList();
        Iterator requestIterator = shoppingCart.getRequestLineItems().iterator();
        while (requestIterator.hasNext()) {
            RequestLineItem rli = (RequestLineItem) requestIterator.next();
            if (rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
                EmailBean emailBean = new EmailBean();
                DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
                EmailBuilder emailBuilder = new RequestLineItemEmailBuilder(rli, emailBean, daoFactory);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                emails.add(emailBean);
            }
        }
        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
        emailBusinessDelegate.sendEmails(emails);
    }    
}