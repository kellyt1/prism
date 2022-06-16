package us.mn.state.health.builder.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.ExtOrgDetailMailingAddress;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonMailingAddress;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.view.materialsrequest.DeliveryDetailForm;

public class DeliveryDetailFormBuilder  {
    private DAOFactory daoFactory;
    private DeliveryDetail deliveryDetail;
    private DeliveryDetailForm deliveryDetailForm;
    private Person requestor;
    private static Log log = LogFactory.getLog(DeliveryDetailFormBuilder.class);
    
    public DeliveryDetailFormBuilder(DeliveryDetailForm deliveryDetailForm, DAOFactory daoFactory) {
        this.deliveryDetailForm = deliveryDetailForm;
        this.daoFactory = daoFactory;
    }
    
    public DeliveryDetailFormBuilder(DeliveryDetailForm deliveryDetailForm, 
                                     DeliveryDetail deliveryDetail,
                                     DAOFactory daoFactory) {
        this(deliveryDetailForm, daoFactory);
        this.deliveryDetail = deliveryDetail;
    }
    
    public DeliveryDetailFormBuilder(DeliveryDetailForm deliveryDetailForm, 
                                     DeliveryDetail deliveryDetail,
                                     Person requestor,
                                     DAOFactory daoFactory) {
        this(deliveryDetailForm, deliveryDetail, daoFactory);
        this.requestor = requestor;
    }
    
    public void buildDefault() throws InfrastructureException {
        if(requestor != null) {           
        }
    }
    
    /**
     * This should usually be the first method called, cuz other values depend 
     * on the selected organization
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildOrganizations() throws InfrastructureException {
        Collection orgs = null;
        if(!StringUtils.nullOrBlank(deliveryDetailForm.getOrgNameFirstCharStart()) &&
           !StringUtils.nullOrBlank(deliveryDetailForm.getOrgNameFirstCharEnd())) {
            char start = deliveryDetailForm.getOrgNameFirstCharStart().toCharArray()[0];
            char end = deliveryDetailForm.getOrgNameFirstCharEnd().toCharArray()[0];
            orgs = daoFactory.getExternalOrgDetailDAO().findByNameFirstCharRange(start, end, true);   //exlude vendors
        }
        else if(deliveryDetailForm.getOrganizations().isEmpty()) {  //else load all of them, if the collection is empty
            orgs = daoFactory.getExternalOrgDetailDAO().findAll(true);
        }  
        
        if(deliveryDetail != null && deliveryDetail.getOrganization() != null) {
            orgs.add(deliveryDetail.getOrganization());  //make sure the existing org shows up on the list
            deliveryDetailForm.setOrganizationId(deliveryDetail.getOrganization().getOrgId().toString());  //...and that its selected
        }
        deliveryDetailForm.setOrganizations(orgs);
    }
    
    /**
     * Only applies when the selected org is null or blank (indicates MDH).  Grab list 
     * of all valid MDH facilities - i.e., those to which stuff can be delivered.
     * Call buildOrganizations() first!
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void buildFacilities() throws InfrastructureException {
        if(StringUtils.nullOrBlank(deliveryDetailForm.getOrganizationId())) {
            Collection facilities = daoFactory.getFacilityDAO().findFacilitiesByType(Facility.TYPE_SUITE_WING);
            facilities.addAll(daoFactory.getFacilityDAO().findFacilitiesByType(Facility.TYPE_SPECIAL_USE_AREA));
            facilities.addAll(daoFactory.getFacilityDAO().findFacilitiesByType(Facility.TYPE_ROOM));
            try {
                CollectionUtils.sort(facilities, "facilityName", true);
            }
            catch(ReflectivePropertyException e) {
                log.error("ReflectivePropertyException in buildFacilities(): ", e);
                throw new InfrastructureException(e);
            }
            deliveryDetailForm.setFacilities(facilities);
            if(deliveryDetail != null && deliveryDetail.getFacility() != null) {
                deliveryDetailForm.setFacilityId(deliveryDetail.getFacility().getFacilityId().toString());
            } 
        }
    }
    
    /**
     * Only applies when the selected org is null or blank (indicates MDH).  Grab list 
     * of all valid MDH facilities of the specified type.
     * Call buildOrganizations() first!
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void buildFacilities(String facilityType) throws InfrastructureException {
        Collection facilities = daoFactory.getFacilityDAO().findFacilitiesByType(facilityType);
        try {
            CollectionUtils.sort(facilities, "facilityName", true);
        }
        catch(ReflectivePropertyException e) {
            log.error("ReflectivePropertyException in buildFacilities(String facType): ", e);
            throw new InfrastructureException(e);
        }
        deliveryDetailForm.setFacilities(facilities);
        if(deliveryDetail != null && deliveryDetail.getFacility() != null) {
            deliveryDetailForm.setFacilityId(deliveryDetail.getFacility().getFacilityId().toString());
        } 
    }
    
    /**
     * Grab a list of all people affiliated with the selected organization. 
     * Call buildOrganizations() first!
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildRecipients() throws InfrastructureException {
        Collection persons = new TreeSet();
        if(DeliveryDetailForm.DELIVER_TO_MDH.equals(deliveryDetailForm.getDeliverToType())) {       
            //user opted to deliver to an MDH Employee, so get all employees
            persons = daoFactory.getPersonDAO().findAllMDHEmployees();
            if(requestor != null ){
                deliveryDetailForm.setRecipientId(requestor.getPersonId().longValue() + "");
            }
        }
        else {
            deliveryDetailForm.setRecipients(null);
            deliveryDetailForm.setRecipientId(null);
        }
        
        deliveryDetailForm.setRecipients(persons);
    }
    
    /**
     * Grab a list of all non-MDH employees (private citizens)
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildPrivateCitizenRecipients() throws InfrastructureException {
        Collection persons = daoFactory.getPersonDAO().findAllNonMDHEmployees();
        deliveryDetailForm.setRecipients(persons);
    }
    
    /**
     * Grab the list of the selected person's mailing addresses.  
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildPrivateCitizenMailingAddresses() throws InfrastructureException {
        deliveryDetailForm.getMailingAddresses().clear();
        if(!StringUtils.nullOrBlank(deliveryDetailForm.getRecipientId())) {
            Long id = new Long(deliveryDetailForm.getRecipientId());
            Person person = daoFactory.getPersonDAO().getPersonById(id, false);
            for(Iterator iter = person.getPersonMailingAddresses().iterator(); iter.hasNext(); ) {
                PersonMailingAddress pma = (PersonMailingAddress)iter.next();
                deliveryDetailForm.getMailingAddresses().add(pma.getMailingAddress());
            }
        }
//        if(!deliveryDetailForm.getMailingAddresses().isEmpty()) {   //pre-select the first one
//            deliveryDetailForm.setMailingAddressId(((MailingAddress)deliveryDetailForm.getMailingAddresses().iterator().next()).getMailingAddressId().toString());
//        } 
    }
    
    /**
     * Grab a list of all mailingAddresses affiliated with the selected EXTERNAL organization. 
     * Do nothing if the organization is not selected (null or blank, which indicates MDH)
     * Call buildOrganizations() first!
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildMailingAddresses() throws InfrastructureException {
        Collection mailingAddresses = new ArrayList();
        if(!StringUtils.nullOrBlank(deliveryDetailForm.getOrganizationId())) {       
            //only get an External Orgs mailing addresses if the orgId is not null or blank
            ExternalOrgDetail externalOrg = 
                daoFactory.getExternalOrgDetailDAO().getExternalOrgDetailById(new Long(deliveryDetailForm.getOrganizationId()), false);
            Iterator iter = externalOrg.getMailingAddresses().iterator();
            while(iter.hasNext()) {
                ExtOrgDetailMailingAddress extOrgDetailMailingAddress = (ExtOrgDetailMailingAddress)iter.next();
                MailingAddress mailingAddress = extOrgDetailMailingAddress.getMailingAddress();
                mailingAddresses.add(mailingAddress);
            }
        }
        deliveryDetailForm.setMailingAddresses(mailingAddresses);
        if(deliveryDetail != null && 
           deliveryDetail.getOrganization() != null &&
           deliveryDetail.getOrganization().getOrgId().toString().equals(deliveryDetailForm.getOrganizationId()) && 
           deliveryDetail.getMailingAddress() != null) {
              deliveryDetailForm.setMailingAddressId(deliveryDetail.getMailingAddress().getMailingAddressId().toString());
        }
//        else {
//            if(!mailingAddresses.isEmpty()) {   //pre-select the first one
//                deliveryDetailForm.setMailingAddressId(((MailingAddress)mailingAddresses.iterator().next()).getMailingAddressId().toString());
//            }  
//        }
    }
}