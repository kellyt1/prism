package us.mn.state.health.builder.inventory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.FacilityDAO;
import us.mn.state.health.dao.OrgBudgetDAO;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.StatusDAO;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.inventory.ClassCodeDAO;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.view.inventory.SensitiveAssetForm;

public class SensitiveAssetFormBuilder  {
   private SensitiveAsset sensitiveAsset;
   private SensitiveAssetForm sensitiveAssetForm;
   private DAOFactory daoFactory;
   private OrderLineItem oli;
   
   public SensitiveAssetFormBuilder(OrderLineItem oli, 
                                    SensitiveAsset sensitiveAsset,
                                    SensitiveAssetForm  sensitiveAssetForm,
                                    DAOFactory daoFactory) {
        this.sensitiveAsset = sensitiveAsset;
        this.sensitiveAssetForm = sensitiveAssetForm;
        this.daoFactory = daoFactory;
        this.oli = oli;
   }   
   
   public SensitiveAssetFormBuilder(SensitiveAsset sensitiveAsset, 
                                    SensitiveAssetForm  sensitiveAssetForm,
                                    DAOFactory daoFactory) {
        this(null, sensitiveAsset, sensitiveAssetForm, daoFactory);
   }
   
   public void buildContactPersons() throws InfrastructureException {
       PersonDAO personDAO = daoFactory.getPersonDAO();
       sensitiveAssetForm.setContactPersons(personDAO.findAllMDHEmployees());
   }
   
   public void buildContactPerson() throws InfrastructureException {
        if(sensitiveAsset.getContactPerson() != null) {
            sensitiveAssetForm.setContactPersonId(sensitiveAsset.getContactPerson().getPersonId().toString());
        }       
   }
   
   public void buildVendors() throws InfrastructureException {
       VendorDAO vendorDAO = daoFactory.getVendorDAO();
//       sensitiveAssetForm.setVendors(vendorDAO.findAll(false));
       sensitiveAssetForm.setVendors(vendorDAO.findAll());
   }
   
   public void buildVendor() throws InfrastructureException {
        if(sensitiveAsset.getVendor() != null) {
            sensitiveAssetForm.setVendorId(sensitiveAsset.getVendor().getVendorId().toString());
        }       
   }
   
   public void buildFacilities() throws InfrastructureException {
       FacilityDAO facilityDAO = daoFactory.getFacilityDAO();
       sensitiveAssetForm.setFacilities(facilityDAO.findFacilitiesByType(Facility.TYPE_SUITE_WING));
   }
   
   public void buildFacility() throws InfrastructureException {
        if(sensitiveAsset.getFacility() != null) {
           sensitiveAssetForm.setFacilityId(sensitiveAsset.getFacility().getFacilityId().toString()); 
        }       
   }
   
   public void buildClassCodes() throws InfrastructureException {
       ClassCodeDAO classCodeDAO = daoFactory.getClassCodeDAO();
       sensitiveAssetForm.setClassCodes(classCodeDAO.findAll());
   }
   
   public void buildClassCode() throws InfrastructureException {
        if(sensitiveAsset.getClassCode() != null) {
            sensitiveAssetForm.setClassCodeId(sensitiveAsset.getClassCode().getClassCodeId().toString());
        }       
   }
     
   public void buildAllOrgBudgetsList() throws InfrastructureException {
        OrgBudgetDAO orgBudgetDAO = daoFactory.getOrgBudgetDAO(); 
        sensitiveAssetForm.setAllOrgBudgetsList(orgBudgetDAO.findAllPurchaseOrgBudgets()); 
   }
   
   /**
    * set the owner org budgets property.  This method doesn't look as efficient
    * as it could because we have to make Hibernate load each org budget so they will 
    * be available for the page.  Its not enough to simply call 
    * sensitiveAssetForm.setOwnerOrgBudgets(sensitiveAsset.getOwnerOrgBudgets())
    * because we have the owner org budgets collection set to lazy load.
    */
   public void buildOrgBudgets() throws InfrastructureException {
        
       sensitiveAssetForm.getOwnerOrgBudgets().clear();
       Iterator iter = sensitiveAsset.getOwnerOrgBudgets().iterator();
       while(iter.hasNext()){
           OrgBudget bdgt = (OrgBudget)iter.next();
           sensitiveAssetForm.addOwnerOrgBudget(bdgt);
       }
   }
   
   /**
     * This method is to be used when the user wishes to add another org budget to the 
     *  asset.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void addOwnerOrgBudget() throws InfrastructureException {
        OrgBudgetDAO orgBdgtDAO = daoFactory.getOrgBudgetDAO();
        OrgBudget orgBdgt = orgBdgtDAO.getOrgBudgetById(new Long(sensitiveAssetForm.getOrgBudgetId()), false);
        sensitiveAssetForm.addOwnerOrgBudget(orgBdgt);
    }
    
     /**
     * This method is to be used when the user wishes to remove an org budget from the 
     *  asset.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void removeOwnerOrgBudget() throws InfrastructureException {
        OrgBudgetDAO orgBdgtDAO = daoFactory.getOrgBudgetDAO();
        OrgBudget orgBdgt = orgBdgtDAO.getOrgBudgetById(new Long(sensitiveAssetForm.getOrgBudgetId()), false);
        sensitiveAssetForm.removeOwnerOrgBudget(orgBdgt);
    }
   
   public void buildStatuses() throws InfrastructureException {
       StatusDAO statusDAO = daoFactory.getStatusDAO();
       sensitiveAssetForm.setStatuses(statusDAO.findAllByStatusTypeCode(StatusType.ASSET));
   }
   
   public void buildStatus() throws InfrastructureException {
        if(sensitiveAsset.getStatus() != null) {
           sensitiveAssetForm.setStatusId(sensitiveAsset.getStatus().getStatusId().toString());
        }       
   }
   
   public void buildOrderLineItem() throws InfrastructureException {
       sensitiveAssetForm.setOrderLineItem(oli); 
   }
   
   /**
     *Initialize a SensitiveAssetForm with properties from the oli.   
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
   public void buildPropertiesFromOLI() throws InfrastructureException {
       if(oli != null) {
           sensitiveAssetForm.setCost(oli.getBuyUnitCost().toString());       
           SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
           sensitiveAssetForm.setDateReceived(formatter.format(new Date()));
           sensitiveAssetForm.setItem(oli.getItem());
           sensitiveAssetForm.setMapsFund(oli.getOrder().getPurchaseOrderNumber());   
           sensitiveAssetForm.setVendorId(oli.getOrder().getVendor().getVendorId().toString());
       }       
    }
    
    public void buildSensitiveAsset() throws InfrastructureException {
        sensitiveAssetForm.setSensitiveAsset(sensitiveAsset);
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(sensitiveAssetForm, sensitiveAsset);
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}