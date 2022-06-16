package us.mn.state.health.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.ExtOrgDetailMailingAddress;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonMailingAddress;
import us.mn.state.health.view.common.DeliveryAddressForm;

public class DeliveryAddressFormBuilder {
    private DeliveryAddressForm deliveryAddressForm;
    private DAOFactory daoFactory; 
    
    public DeliveryAddressFormBuilder(DeliveryAddressForm deliveryAddressForm, DAOFactory daoFactory) {
        this.deliveryAddressForm = deliveryAddressForm;
        this.daoFactory = daoFactory;
    }

    public void buildExternalOrgs() throws Exception {
        deliveryAddressForm.setExtOrgs(daoFactory.getExternalOrgDetailDAO().findAll(true));


    }
    
    /* no longer relevant - we are not using Person records as contacts for external orgs.
    public void buildPersons() throws Exception {
        if(!StringUtils.nullOrBlank(deliveryAddressForm.getSelectedExtOrgId())) {
            Long extOrgId = new Long(deliveryAddressForm.getSelectedExtOrgId());
            ExternalOrgDetail extOrgDetail = daoFactory.getExternalOrgDetailDAO().getExternalOrgDetailById(extOrgId, false);
            Collection reps = new ArrayList();
            for(Iterator iter = extOrgDetail.getReps().iterator(); iter.hasNext(); ) {
                ExternalOrgDetailRep rep = (ExternalOrgDetailRep)iter.next();
                reps.add(rep.getRep());
            }
            deliveryAddressForm.setContactPersons(reps);
            Collection nonReps = daoFactory.getPersonDAO().findAllNonMDHEmployees();
            nonReps.removeAll(reps);
            deliveryAddressForm.setNonContactPersons(nonReps);        
        }
    }
    */
    
    public void buildPrivateCitizenPersons() throws Exception {
        Collection nonEmployees = daoFactory.getPersonDAO().findAllNonMDHEmployees();
        deliveryAddressForm.setPrivateCitizenPersons(nonEmployees);
    }
    
    public void buildMailingAddresses() throws Exception {        
        if(deliveryAddressForm != null) {
            deliveryAddressForm.getMailingAddresses().clear();
            if(!deliveryAddressForm.getNewOrOld().equals(DeliveryAddressForm.PRIVATE_CITIZEN) && 
               !StringUtils.nullOrBlank(deliveryAddressForm.getSelectedExtOrgId())) {
                Long extOrgId = new Long(deliveryAddressForm.getSelectedExtOrgId());
                ExternalOrgDetail extOrgDetail = daoFactory.getExternalOrgDetailDAO().getExternalOrgDetailById(extOrgId, false);
                Collection extOrgDetailMailingAddresses = new ArrayList();  //collection of ExtOrgDetailMailingAddress objects
                for(Iterator iter = extOrgDetail.getMailingAddresses().iterator(); iter.hasNext(); ) {
                    ExtOrgDetailMailingAddress extOrgDetMA = (ExtOrgDetailMailingAddress)iter.next();
                    extOrgDetailMailingAddresses.add(extOrgDetMA.getMailingAddress());
                }
                deliveryAddressForm.setMailingAddresses(extOrgDetailMailingAddresses);
            }
            else if(deliveryAddressForm.getNewOrOld().equals(DeliveryAddressForm.PRIVATE_CITIZEN) &&
                    !StringUtils.nullOrBlank(deliveryAddressForm.getSelectedPrivateCitizenPersonId())) {
                Long id = new Long(deliveryAddressForm.getSelectedPrivateCitizenPersonId());
                Person person = daoFactory.getPersonDAO().getPersonById(id, false);
                Collection mailingAddresses = new ArrayList();
                for(Iterator iter = person.getPersonMailingAddresses().iterator(); iter.hasNext(); ) {
                    PersonMailingAddress pma = (PersonMailingAddress)iter.next();
                    mailingAddresses.add(pma.getMailingAddress());
                }
                deliveryAddressForm.setMailingAddresses(mailingAddresses);
            }  
        }        
    }
}
