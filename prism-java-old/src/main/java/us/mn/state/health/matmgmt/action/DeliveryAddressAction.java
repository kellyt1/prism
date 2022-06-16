package us.mn.state.health.matmgmt.action;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.DeliveryAddressFormBuilder;
import us.mn.state.health.builder.MailingAddressBuilder;
import us.mn.state.health.builder.PersonBuilder;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.ExtOrgDetailMailingAddress;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.ExternalOrgDetailRep;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonMailingAddress;
import us.mn.state.health.model.common.User;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.common.DeliveryAddressForm;
import us.mn.state.health.view.common.MailingAddressForm;
import us.mn.state.health.view.common.PersonForm;
import us.mn.state.health.persistence.HibernateUtil;

/**
 * Action class to handle everything related to adding new delivery addresses to PRISM system.
 * @author Shawn Flahave
 */
public class DeliveryAddressAction extends MappingDispatchAction {
    private static Log log = LogFactory.getLog(DeliveryAddressAction.class);
    
    /**
     * Prepares view of Step 1 for adding a new address to PRISM.
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward viewAddNewAddressStep1(ActionMapping mapping, 
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        DeliveryAddressFormBuilder builder = new DeliveryAddressFormBuilder(delAddrForm, daoFactory);
        builder.buildExternalOrgs();
        builder = null;
        
        return mapping.findForward("success");
    }
    
    /**
     * Prepares view of Step 2 for adding a new address to PRISM.
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward viewAddNewAddressStep2(ActionMapping mapping, 
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        log.debug("THE SIZE OF extOrgs is: " + delAddrForm.getExtOrgs().size());
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        DeliveryAddressFormBuilder builder = new DeliveryAddressFormBuilder(delAddrForm, daoFactory);
        User user = (User)request.getSession().getAttribute(ApplicationResources.USER);
        
        if(!delAddrForm.getNewOrOld().equals(DeliveryAddressForm.OLD_ORG)) {
           //user either chose to add a new org or to work with a private citizen (person)
           
            if(delAddrForm.getExtOrgForm() == null) {
                log.error("Error in viewAddNewAddressStep2: both the selectedExtOrgId and the " + 
                          "ExternalOrgForm property of DeliveryAddressForm are null.  " + 
                          "One or the other must be present.  User that triggered this error: " + user.getUsername());
                return mapping.findForward("failure");
            }
            
            if(delAddrForm.getNewOrOld().equals(DeliveryAddressForm.NEW_ORG) &&
               !StringUtils.nullOrBlank(delAddrForm.getExtOrgForm().getOrgName())) {  
                //user added a new external org, so save it
                ExternalOrgDetail extOrgDetail = new ExternalOrgDetail();
                PropertyUtils.copyProperties(extOrgDetail, delAddrForm.getExtOrgForm());
                extOrgDetail.setInsertedBy(user.getUsername());
                extOrgDetail.setInsertionDate(new Date());
                extOrgDetail.setOrgName(extOrgDetail.getOrgName().toUpperCase());
                extOrgDetail.setOrgDescription("added via PRISM for new delivery addresses");
                ExternalOrgDetail extOrgDetail2 = daoFactory.getExternalOrgDetailDAO().findByOrgName(extOrgDetail.getOrgName());
                if(extOrgDetail2 == null) {  //don't save a duplicate.
                    daoFactory.getExternalOrgDetailDAO().makePersistent(extOrgDetail);
                }
                else {
                    extOrgDetail = extOrgDetail2;
                }
                
                delAddrForm.setSelectedExtOrgId(extOrgDetail.getOrgId().toString());
                request.setAttribute("NEWEXTORG",extOrgDetail.getOrgId().toString());
            } 
            else if(delAddrForm.getNewOrOld().equals(DeliveryAddressForm.PRIVATE_CITIZEN)) {
                //user chose to enter a new private citizen, or a new address for an existing private citizen 
                //build a list of persons that are not employees
                builder.buildPrivateCitizenPersons();
                builder = null;
                return mapping.findForward("step2");
            }
        }
        
//        builder.buildPersons();
        builder = null;
        
        return mapping.findForward("step3");    //skip the person stuff
    }
    
    /**
     * Prepares view of Step 3 for adding a new address to PRISM.
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward viewAddNewAddressStep3(ActionMapping mapping, 
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        if ((delAddrForm.getSelectedExtOrgId() == null || delAddrForm.getSelectedExtOrgId().equals(""))  && request.getAttribute("NEWEXTORG") != null) {
            delAddrForm.setSelectedExtOrgId((String)request.getAttribute("NEWEXTORG"));
            request.removeAttribute("NEWEXTORG");
        }
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        DeliveryAddressFormBuilder builder = new DeliveryAddressFormBuilder(delAddrForm, daoFactory);
        
        builder.buildMailingAddresses();
        builder = null;
        
        return mapping.findForward("success");
    }
    
    /**
     * Final step for adding a new address to PRISM.  This action handles persisting the new delivery address
     * and then forwards to some default menu page.
     * 1) Make sure each Person in contactPersons collection is set up as a rep for the selected external org.
     * 2) Make sure that each address in the mailingAddresses collection is set up as a mailing address for the selected ext org.
     * 3) persist the external org, which should  indirectly persist the new relationships
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward addNewAddressFinalStep(ActionMapping mapping, 
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User user = (User)request.getSession().getAttribute(ApplicationResources.USER);
        
        if(delAddrForm.getNewOrOld().equals(DeliveryAddressForm.PRIVATE_CITIZEN)) {
            Long personId = new Long(delAddrForm.getSelectedPrivateCitizenPersonId());
            Person person = daoFactory.getPersonDAO().getPersonById(personId, false);
            createMailingAddressPersonRelationships(person,  delAddrForm.getMailingAddresses());
            daoFactory.getPersonDAO().makePersistent(person);
        }
        else {
            Long extOrgId = new Long(delAddrForm.getSelectedExtOrgId());
            ExternalOrgDetail extOrgDetail = daoFactory.getExternalOrgDetailDAO().getExternalOrgDetailById(extOrgId, false);
            createExtOrgPersonRelationships(extOrgDetail, delAddrForm.getContactPersons());
            createExtOrgMailingAddressRelationships(extOrgDetail, delAddrForm.getMailingAddresses(), user.getUsername());           
            daoFactory.getExternalOrgDetailDAO().makePersistent(extOrgDetail);
        }
        
        if(!StringUtils.nullOrBlank(delAddrForm.getForward())) {
            return new ActionForward(delAddrForm.getForward());
        }
        
        delAddrForm.setReset(true);
        
        return mapping.findForward("success");
    }
    
    /**
     * Helper method to establish relationship between Person and MailingAddress (via PersonMailingAddress).  
     * @param person
     * @param mailingAddresses
     */
    private void createMailingAddressPersonRelationships(Person person, Collection mailingAddresses) {
        for(Iterator iter = mailingAddresses.iterator(); iter.hasNext(); ) {
            boolean create = true;
            MailingAddress mailingAddress = (MailingAddress)iter.next();
            //only add this mailingAddress only if its not already in there
            for(Iterator iter2 = person.getPersonMailingAddresses().iterator(); iter2.hasNext(); ) {
                PersonMailingAddress pma = (PersonMailingAddress)iter2.next(); 
                if(pma.getMailingAddress().equals(mailingAddress)) {
                    create = false;
                    break;
                }
            } 
            if(create) {
                PersonMailingAddress.create(person, mailingAddress, "prism");
            }            
        }
    }
    
    /**
     * Helper method to establish relationship between ExtOrgDetail and Person.  
     * Creates an ExtOrgDetailRep if the Person is not already a rep for the given 
     * external org.
     * @param persons
     * @param extOrgDetail
     */
    private void createExtOrgPersonRelationships(ExternalOrgDetail extOrgDetail, Collection persons) {
        for(Iterator iter = persons.iterator(); iter.hasNext(); ) {
            boolean create = true;
            Person person = (Person)iter.next();
            //only add this person as a rep if they aren't already a rep
            for(Iterator iter2 = extOrgDetail.getReps().iterator(); iter2.hasNext(); ) {
                ExternalOrgDetailRep extOrgDetRep = (ExternalOrgDetailRep)iter2.next(); 
                if(extOrgDetRep.getRep().equals(person)) {
                    create = false;
                    break;
                }
            } 
            if(create) {
                ExternalOrgDetailRep.create(extOrgDetail, person);
            }            
        }
    }
    
    /**
     * Helper method to establish relationship between ExtOrgDetail and MailingAddress.  
     * Creates an ExtOrgDetailMailingAddress if the MailingAddress is not already a present for the given 
     * external org.
     * @param username
     * @param mailingAddresses
     * @param extOrgDetail
     */
    private void createExtOrgMailingAddressRelationships(ExternalOrgDetail extOrgDetail, Collection mailingAddresses, String username) {
        for(Iterator iter = mailingAddresses.iterator(); iter.hasNext(); ) {     
            boolean create = true;
            MailingAddress mailingAddress = (MailingAddress)iter.next();
            //only add this person as a rep if they aren't already a rep
            for(Iterator iter2 = extOrgDetail.getMailingAddresses().iterator(); iter2.hasNext(); ) {
                ExtOrgDetailMailingAddress extOrgDetMA = (ExtOrgDetailMailingAddress)iter2.next(); 
                if(extOrgDetMA.getMailingAddress().equals(mailingAddress)) {
                    create = false;
                    break;
                }
            } 
            if(create) {
                 ExtOrgDetailMailingAddress.create(extOrgDetail, mailingAddress, username);
            }           
        }
    }
  
  
    /**
     * Action that moves PERSONS from the available contacts collection to the Current Contacts section... Sets up 
     * selected persons to be stored as ExternalOrgDetailReps.  
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward addContacts(ActionMapping mapping, 
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        String[] personsToAdd = delAddrForm.getSelectedNonContactPersonIds();
        for(int i = 0; i < personsToAdd.length; i++) {
           Long id = new Long(personsToAdd[i]);
           Person person = daoFactory.getPersonDAO().getPersonById(id, false);
           delAddrForm.getContactPersons().add(person);
           delAddrForm.getNonContactPersons().remove(person);
        }
        return mapping.findForward("success");
    }
    
    /**
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward viewAddPerson(ActionMapping mapping, 
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        
        return mapping.findForward("success");
    }
    
    /**
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward addPerson(ActionMapping mapping, 
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        PersonForm personForm = delAddrForm.getPersonForm();
        Person person = new Person();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User user = (User)request.getSession().getAttribute(ApplicationResources.USER);
        PersonBuilder builder = new PersonBuilder(person, personForm, daoFactory, user.getUsername());        
        builder.buildSimpleProperties();
        builder.buildDefaultProperties();
        
        daoFactory.getPersonDAO().makePersistent(person);       //save the new person record 
        
        personForm.reset(mapping, request);
        
        if(delAddrForm.getNewOrOld().equals(DeliveryAddressForm.PRIVATE_CITIZEN)) {
            delAddrForm.getPrivateCitizenPersons().add(person);
            CollectionUtils.sort(delAddrForm.getPrivateCitizenPersons(), "lastName", true);
        }
        else {
            delAddrForm.getContactPersons().add(person);
            CollectionUtils.sort(delAddrForm.getContactPersons(), "lastName", true);
        }        
        
        return mapping.findForward("success");
    }
    
    /**
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward viewAddNewMailingAddress(ActionMapping mapping, 
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        
        return mapping.findForward("success");
    }
    
    /**
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward addNewMailingAddress(ActionMapping mapping, 
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        DeliveryAddressForm delAddrForm = (DeliveryAddressForm)form;
        MailingAddressForm mailingAddressForm = delAddrForm.getMailingAddressForm();
        MailingAddress mailingAddress = new MailingAddress();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User user = (User)request.getSession().getAttribute(ApplicationResources.USER);
        MailingAddressBuilder builder = new MailingAddressBuilder(mailingAddress, mailingAddressForm, user);
        builder.buildSimpleProperties();
        builder.buildDefaultProperties();
        daoFactory.getMailingAddressDAO().makePersistent(mailingAddress);        
        mailingAddressForm.reset(mapping, request);
        delAddrForm.getMailingAddresses().add(mailingAddress);
        
        return mapping.findForward("success");
    }
}