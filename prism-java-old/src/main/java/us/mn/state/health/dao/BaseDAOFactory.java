package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.inventory.*;
import us.mn.state.health.dao.materialsrequest.*;
import us.mn.state.health.dao.purchasing.AssetViewDAO;
import us.mn.state.health.dao.purchasing.HelpDeskTicketTrackingDAO;
import us.mn.state.health.dao.purchasing.PurchasingOrderDAO;
import us.mn.state.health.dao.purchasing.PurchasingOrderLineItemDAO;
import us.mn.state.health.dao.receiving.DeliveryTicketDAO;

public abstract class BaseDAOFactory extends DAOFactory {

    public void commitTransaction(boolean beginNewTransactionAfterCommit) throws InfrastructureException {

    }

    public ActionRequestTypeDAO getActionRequestTypeDAO() {
        return null;
    }

    public StockItemDAO getStockItemDAO() {
        return null;
    }

    public CategoryDAO getCategoryDAO() {
        return null;
    }
    public FiscalYearsDAO getFiscalYearsDAO() {
        return null;
    }

    public ItemDAO getItemDAO() {
        return null;
    }

    public StockItemActionRequestDAO getStockItemActionRequestDAO() {
        return null;
    }

    public PersonDAO getPersonDAO() {
        return null;
    }

    public EmployeeDAO getEmployeeDAO() {
        return null;
    }

    public OrgBudgetDAO getOrgBudgetDAO() {
        return null;
    }

    public StatusDAO getStatusDAO() {
        return null;
    }

    public UnitDAO getUnitDAO() {
        return null;
    }

    public VendorDAO getVendorDAO() {
        return null;
    }

    public OrderFormulaDAO getOrderFormulaDAO() {
        return null;
    }

    public UserDAO getUserDAO() {
        return null;
    }

    public StockItemFacilityDAO getStockItemFacilityDAO() {
        return null;
    }

    public ManufacturerDAO getManufacturerDAO() {
        return null;
    }

    public RequestDAO getRequestDAO() {
        return null;
    }

    public RequestLineItemDAO getRequestLineItemDAO() {
        return null;
    }

    public RequestLineItemFundingSourceDAO getRequestLineItemFundingSourceDAO() {
        return null;
    }

    public RequestEvaluationDAO getRequestEvaluationDAO() {
        return null;
    }

//    public PersonalAddressBookDAO getPersonalAddressBookDAO() {
//        return null;
//    }
//
//    public AddressBookDAO getAddressBookDAO() {
//        return null;
//    }

    public PriorityDAO getPriorityDAO() {
        return null;
    }

    public GroupDAO getGroupDAO() {
        return null;
    }

    public FacilityDAO getFacilityDAO() {
        return null;
    }

    public ShoppingListDAO getShoppingListDAO() {
        return null;
    }

    public PurchaseItemDAO getPurchaseItemDAO() {
        return null;
    }

    public PurchasingOrderDAO getPurchasingOrderDAO() {
        return null;
    }

    public PurchasingOrderLineItemDAO getPurchasingOrderLineItemDAO() {
        return null;
    }

    public HelpDeskTicketTrackingDAO getHelpDeskTicketTrackingDAO() {
        return null;
    }

    public SensitiveAssetDAO getSensitiveAssetDAO() {
        return null;
    }

    public FixedAssetDAO getFixedAssetDAO() {
        return null;
    }

    public ClassCodeDAO getClassCodeDAO() {
        return null;
    }

    public DeliveryTicketDAO getDeliveryTicketDAO() {
        return null;
    }

    public ObjectCodeDAO getObjectCodeDAO(){
        return null;
    }

    public StockQtyChangeReasonRefDAO getStockQtyChangeReasonRefDAO() {
        return null;
    }

    public PhoneDAO getPhoneDAO() {
        return null;
    }

    public ExternalOrgDetailDAO getExternalOrgDetailDAO() {
        return null;
    }

    public MailingAddressDAO getMailingAddressDAO() {
        return null;
    }

    public VendorContractDAO getVendorContractDAO() {
        return null;
    }

    public VendorAccountDAO getVendorAccountDAO() {
        return null;
    }
    public ExternalReportDAO getExternalReportDAO() {
        return null;
    }
    public BusinessRulesORCLDAO getBusinessRulesORCLDAO() {
        return null;
    }

    public NotificationEmailAddressDAO getNotificationEmailAddressDAO() {
        return null;
    }

    public DeliveryDetailDAO getDeliveryDetailDAO() {
        return null;
    }

    public AssetViewDAO getAssetViewDAO() {
        return null;
    }
}