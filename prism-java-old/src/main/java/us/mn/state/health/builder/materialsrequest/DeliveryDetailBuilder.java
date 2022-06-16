package us.mn.state.health.builder.materialsrequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.view.materialsrequest.DeliveryDetailForm;

public class DeliveryDetailBuilder  {
    private static Log log = LogFactory.getLog(DeliveryDetailBuilder.class);
    private DAOFactory daoFactory;
    private DeliveryDetail deliveryDetail;
    private DeliveryDetailForm deliveryDetailForm;
    private Person requestor;
    public static String MINNCOR_FACILITY_ID = "87761498";
    public static String LAB_FACILITY_ID = "76686";

    public DeliveryDetailBuilder(DeliveryDetail deliveryDetail, DAOFactory daoFactory) {
        this.deliveryDetail = deliveryDetail;
        this.daoFactory = daoFactory;
    }
    
    public DeliveryDetailBuilder(DeliveryDetailForm deliveryDetailForm, 
                                 DeliveryDetail deliveryDetail,
                                 DAOFactory daoFactory) {
        this(deliveryDetail, daoFactory);
        this.deliveryDetailForm = deliveryDetailForm;
    }
    
    public DeliveryDetailBuilder(DeliveryDetailForm deliveryDetailForm, 
                                 DeliveryDetail deliveryDetail,
                                 Person requestor,
                                 DAOFactory daoFactory) {
        this(deliveryDetailForm, deliveryDetail, daoFactory);
        this.requestor = requestor;
    }
    
    /**
     * Set up the default delivery information
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildDefault() throws InfrastructureException {
        if(requestor != null && deliveryDetail != null) {
           Person recipient = daoFactory.getPersonDAO().getPersonById(requestor.getPersonId(), false);
           deliveryDetail.setOrganization(null);         
           
           if(recipient.getPrimaryFacility() != null) {
               deliveryDetail.setFacility(recipient.getPrimaryFacility());
               
               deliveryDetail.setMailingAddress(recipient.getPrimaryFacility().getBuildingMailingAddress());
               log.debug("buildDefault() - user: " + recipient.getFirstAndLastName() + " buildingMailingAddr: " + recipient.getPrimaryFacility().getBuildingMailingAddress().getShortSummary());
           }            
           
           deliveryDetail.setRecipient(recipient);
        }
        else {
            log.debug("buildDefault() - either requestor was null or deliveryDetail was null, so doing nothing");
        }
    }

    /**
     * Set up the default delivery information for stock items
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildStock() throws InfrastructureException {
        deliveryDetail.setRecipient(null);
        deliveryDetail.setFacility(daoFactory.getFacilityDAO().getFacilityById(Long.valueOf(MINNCOR_FACILITY_ID), false));
        deliveryDetail.setMailingAddress(daoFactory.getFacilityDAO().getFacilityById(Long.valueOf(MINNCOR_FACILITY_ID), false).getBuildingMailingAddress());
    }
    
    /**
     * This should usually be the first method called, cuz other values depend 
     * on the selected organization
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildOrganization() throws InfrastructureException {
        if(!StringUtils.nullOrBlank(deliveryDetailForm.getOrganizationId()) && 
           DeliveryDetailForm.DELIVER_TO_EXT_ORG.equals(deliveryDetailForm.getDeliverToType())) {
            //user opted to deliver to an external org
            Long orgId = new Long(deliveryDetailForm.getOrganizationId());
            ExternalOrgDetail extOrg = daoFactory.getExternalOrgDetailDAO().getExternalOrgDetailById(orgId, false);
            deliveryDetail.setOrganization(extOrg);
        }
        else {
            deliveryDetail.setOrganization(null);            
        }        
    }
    
    /**
     * Set the Facility only if the organizationId property is null or blank, which indicates MDH is the organization.
     * If organizationId is not null or blank, set the facility to null
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void buildFacility() throws InfrastructureException {
        if(DeliveryDetailForm.DELIVER_TO_MDH.equals(deliveryDetailForm.getDeliverToType())) {
            if(!StringUtils.nullOrBlank(deliveryDetailForm.getFacilityId())) {
                Long facilityId = new Long(deliveryDetailForm.getFacilityId());
                Facility facility = daoFactory.getFacilityDAO().getFacilityById(facilityId, false);
                deliveryDetail.setFacility(facility);
            }
        /*} else if(DeliveryDetailForm.INVENTORY_SSC.equals(deliveryDetailForm.getDeliverToType())){
            deliveryDetail.setFacility(daoFactory.getFacilityDAO().getFacilityById(SSC_FACILITY_ID, false));
        } else if(DeliveryDetailForm.INVENTORY_LAB.equals(deliveryDetailForm.getDeliverToType())){
            deliveryDetail.setFacility(daoFactory.getFacilityDAO().getFacilityById(LAB_FACILITY_ID, false));*/
        } else {
            deliveryDetail.setFacility(null);
        }        
     }
    
    /**
     * 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildRecipient() throws InfrastructureException {
        if(StringUtils.nullOrBlank(deliveryDetailForm.getRecipientId())) {
            deliveryDetail.setRecipient(null);
        }
        else {
            Long personId = new Long(deliveryDetailForm.getRecipientId());
            Person person = daoFactory.getPersonDAO().getPersonById(personId, false);
            deliveryDetail.setRecipient(person);
        }        
    }
    
    /**
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildMailingAddress() throws InfrastructureException {
       if((DeliveryDetailForm.DELIVER_TO_EXT_ORG.equals(deliveryDetailForm.getDeliverToType()) || 
           DeliveryDetailForm.DELIVER_TO_CITIZEN.equals(deliveryDetailForm.getDeliverToType())) &&
          !StringUtils.nullOrBlank(deliveryDetailForm.getMailingAddressId())) {
            //we're sending to either a private citizen or an external org
            Long mailingAddressId = new Long(deliveryDetailForm.getMailingAddressId());
            MailingAddress mailingAddress = daoFactory.getMailingAddressDAO().getMailingAddressById(mailingAddressId, false);
            deliveryDetail.setMailingAddress(mailingAddress);
        }
        else if(DeliveryDetailForm.DELIVER_TO_MDH.equals(deliveryDetailForm.getDeliverToType()) &&
                !StringUtils.nullOrBlank(deliveryDetailForm.getFacilityId())) {
            //we're sending to an MDH employee at an MDH facility
            Long facilityId = new Long(deliveryDetailForm.getFacilityId());
            Facility facility = daoFactory.getFacilityDAO().getFacilityById(facilityId, false);
            deliveryDetail.setMailingAddress(facility.getBuildingMailingAddress());
        }
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(deliveryDetailForm != null) {
                PropertyUtils.copyProperties(deliveryDetail, deliveryDetailForm);
            }
        } 
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}