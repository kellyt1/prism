package us.mn.state.health.builder.inventory;

import java.util.Date;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.FacilityDAO;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.StatusDAO;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.inventory.ClassCodeDAO;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.view.inventory.SensitiveAssetForm;

public class SensitiveAssetBuilder  {
   private SensitiveAsset sensitiveAsset;
   private SensitiveAssetForm sensitiveAssetForm;
   private DAOFactory daoFactory;
   private User user;
   
   public SensitiveAssetBuilder(SensitiveAsset sensitiveAsset, 
                                SensitiveAssetForm sensitiveAssetForm,
                                DAOFactory daoFactory,
                                User user) {
        this.sensitiveAsset = sensitiveAsset;
        this.sensitiveAssetForm = sensitiveAssetForm;
        this.daoFactory = daoFactory;
        this.user = user;
   }
   
   public void buildContactPerson() throws InfrastructureException {
       PersonDAO personDAO = daoFactory.getPersonDAO();
       if(!StringUtils.nullOrBlank(sensitiveAssetForm.getContactPersonId())) {
            Person contactPerson = personDAO.getPersonById(new Long(sensitiveAssetForm.getContactPersonId()), false);
            sensitiveAsset.setContactPerson(contactPerson);
       }              
   }
   
   public void buildVendor() throws InfrastructureException {
       VendorDAO vendorDAO = daoFactory.getVendorDAO();
       if (!StringUtils.nullOrBlank(sensitiveAssetForm.getVendorId())) {
           sensitiveAsset.setVendor(vendorDAO.getVendorById(new Long(sensitiveAssetForm.getVendorId()), false));
       }
   }
   
   public void buildFacility() throws InfrastructureException {
       FacilityDAO facilityDAO = daoFactory.getFacilityDAO();
       if(!StringUtils.nullOrBlank(sensitiveAssetForm.getFacilityId())) {
           sensitiveAsset.setFacility(facilityDAO.getFacilityById(new Long(sensitiveAssetForm.getFacilityId()), false));
       }       
   }
   
   public void buildClassCode() throws InfrastructureException {
       ClassCodeDAO classCodeDAO = daoFactory.getClassCodeDAO();
       if(!StringUtils.nullOrBlank(sensitiveAssetForm.getClassCodeId())) {
          sensitiveAsset.setClassCode(classCodeDAO.getClassCodeById(new Long(sensitiveAssetForm.getClassCodeId()), false));
       }
   }
    
   public void buildStatus() throws InfrastructureException {
       StatusDAO statusDAO = daoFactory.getStatusDAO();
       if(!StringUtils.nullOrBlank(sensitiveAssetForm.getStatusId())) {
           sensitiveAsset.setStatus(statusDAO.getStatusById(new Long(sensitiveAssetForm.getStatusId()), false));
       }
    }
    
   public void buildOwnerOrgBudgets() throws InfrastructureException {
        if(sensitiveAssetForm.getOwnerOrgBudgets() != null) {
            sensitiveAsset.getOwnerOrgBudgets().clear();
            Iterator iter = sensitiveAssetForm.getOwnerOrgBudgets().iterator();
            while(iter.hasNext()) {
                OrgBudget orgBudget = (OrgBudget)iter.next();
                sensitiveAsset.addOwnerOrgBudget(orgBudget);
            } 
        }        
   }
   
   public void buildItem() throws InfrastructureException {
       if(sensitiveAsset.getItem() == null) {
           sensitiveAsset.setItem(sensitiveAssetForm.getItem());
       }       
   } 
   
   public void buildOrderLineItem() throws InfrastructureException {
       sensitiveAsset.setOrderLineItem(sensitiveAssetForm.getOrderLineItem());
   }
   
   public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(sensitiveAssetForm != null) {
                PropertyUtils.copyProperties(sensitiveAsset, sensitiveAssetForm);
            }
        } 
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building SensitiveAsset: ", rpe);
        }
    }
    
    public void buildMetaProperties() {
        if(sensitiveAsset.getSensitiveAssetId() == null) {
            sensitiveAsset.setInsertedBy(user.getUsername());
            sensitiveAsset.setInsertionDate(new Date());
        }
        else {
            sensitiveAsset.setChangedBy(user.getUsername());
            sensitiveAsset.setChangeDate(new Date());
        }
    }   
}