package us.mn.state.health.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.inventory.*;
import us.mn.state.health.dao.materialsrequest.*;
import us.mn.state.health.dao.purchasing.*;
import us.mn.state.health.dao.receiving.DeliveryTicketDAO;
import us.mn.state.health.dao.receiving.HibernateDeliveryTicketDAO;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateDAOFactory extends BaseDAOFactory {
    private static Log log = LogFactory.getLog(HibernateDAOFactory.class);

    public void commitTransaction(boolean beginNewTransactionAfterCommit) throws InfrastructureException {
        try {
            HibernateUtil.commitTransaction();
            if(beginNewTransactionAfterCommit) {
                HibernateUtil.closeSession();
                HibernateUtil.beginTransaction();
            }
        }
        catch(InfrastructureException e) {
            log.error("HibernateDAOFactory commitTransaction failed:" + e);
            throw new InfrastructureException(e);
        }
    }

    public ActionRequestTypeDAO getActionRequestTypeDAO() {
        return new HibernateActionRequestTypeDAO();
    }

    public StockItemDAO getStockItemDAO() {
        return new HibernateStockItemDAO();
    }

    public CategoryDAO getCategoryDAO() {
        return new HibernateCategoryDAO();
    }
    public FiscalYearsDAO getFiscalYearsDAO() {
        return new HibernateFiscalYearsDAO();
    }

    public ItemDAO getItemDAO() {
        return new HibernateItemDAO();
    }

    public StockItemActionRequestDAO getStockItemActionRequestDAO() {
        return new HibernateStockItemActionRequestDAO();
    }

    public PersonDAO getPersonDAO() {
        return new HibernatePersonDAO();
    }

    public EmployeeDAO getEmployeeDAO() {
        return new HibernateEmployeeDAO();
    }

    public OrgBudgetDAO getOrgBudgetDAO() {
        return new HibernateOrgBudgetDAO();
    }

    public StatusDAO getStatusDAO() {
        return new HibernateStatusDAO();
    }

    public UnitDAO getUnitDAO() {
        return new HibernateUnitDAO();
    }

    public VendorDAO getVendorDAO() {
        return new HibernateVendorDAO();
    }

    public OrderFormulaDAO getOrderFormulaDAO() {
        return new HibernateOrderFormulaDAO();
    }

    public UserDAO getUserDAO() {
        return new HibernateUserDAO();
    }

    public StockItemFacilityDAO getStockItemFacilityDAO() {
        return new HibernateStockItemFacilityDAO();
    }

    public ManufacturerDAO getManufacturerDAO() {
        return new HibernateManufacturerDAO();
    }

    public RequestDAO getRequestDAO() {
        return new HibernateRequestDAO();
    }

    public RequestLineItemDAO getRequestLineItemDAO() {
        return new HibernateRequestLineItemDAO();
    }

    public RequestLineItemFundingSourceDAO getRequestLineItemFundingSourceDAO() {
        return new HibernateRequestLineItemFundingSourceDAO();
    }

    public RequestEvaluationDAO getRequestEvaluationDAO() {
        return new HibernateRequestEvaluationDAO();
    }

//    public PersonalAddressBookDAO getPersonalAddressBookDAO() {
//        return new HibernatePersonalAddressBookDAO();
//    }
//
//    public AddressBookDAO getAddressBookDAO() {
//        return new HibernateAddressBookDAO();
//    }

    public PriorityDAO getPriorityDAO() {
        return new HibernatePriorityDAO();
    }

    public GroupDAO getGroupDAO() {
        return new HibernateGroupDAO();
    }

    public FacilityDAO getFacilityDAO() {
        return new HibernateFacilityDAO();
    }

    public ShoppingListDAO getShoppingListDAO() {
        return new HibernateShoppingListDAO();
    }

    public PurchaseItemDAO getPurchaseItemDAO() {
        return new HibernatePurchaseItemDAO();
    }

    public PurchasingOrderDAO getPurchasingOrderDAO() {
        return new HibernatePurchasingOrderDAO();
    }

    public PurchasingOrderLineItemDAO getPurchasingOrderLineItemDAO() {
        return new HibernatePurchasingOrderLineItemDAO();
    }

    public HelpDeskTicketTrackingDAO getHelpDeskTicketTrackingDAO() {
        return new HibernateHelpDeskTicketTrackingDAO();
    }

    public SensitiveAssetDAO getSensitiveAssetDAO() {
        return new HibernateSensitiveAssetDAO();
    }

    public FixedAssetDAO getFixedAssetDAO() {
        return new HibernateFixedAssetDAO();
    }

    public ClassCodeDAO getClassCodeDAO() {
        return new HibernateClassCodeDAO();
    }

    public DeliveryTicketDAO getDeliveryTicketDAO() {
        return new HibernateDeliveryTicketDAO();
    }

    public ObjectCodeDAO getObjectCodeDAO(){
        return new HibernateObjectCodeDAO();
    }

    public StockQtyChangeReasonRefDAO getStockQtyChangeReasonRefDAO() {
        return new HibernateStockQtyChangeReasonRefDAO();
    }

    public PhoneDAO getPhoneDAO() {
        return new HibernatePhoneDAO();
    }

    public ExternalOrgDetailDAO getExternalOrgDetailDAO() {
        return new HibernateExternalOrgDetailDAO();
    }

    public MailingAddressDAO getMailingAddressDAO() {
        return new HibernateMailingAddressDAO();
    }

    public VendorContractDAO getVendorContractDAO() {
        return new HibernateVendorContractDAO();
    }

    public VendorAccountDAO getVendorAccountDAO() {
        return new HibernateVendorAccountDAO();
    }
    public ExternalReportDAO getExternalReportDAO() {
        return new HibernateExternalReportDAO();
    }
    public BusinessRulesORCLDAO getBusinessRulesORCLDAO() {
        return new HibernateBusinessRulesORCLDAO();
    }

    public NotificationEmailAddressDAO getNotificationEmailAddressDAO() {return new HibernateNotificationEmailAddressDAO();}

    public DeliveryDetailDAO getDeliveryDetailDAO() {return new HibernateDeliveryDetailDAO();}

    public AssetViewDAO getAssetViewDAO() {
        return new HibernateAssetViewDAO();
    }
}