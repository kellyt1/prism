package us.mn.state.health.dao

import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.dao.inventory.ActionRequestTypeDAO
import us.mn.state.health.dao.inventory.ClassCodeDAO
import us.mn.state.health.dao.inventory.FixedAssetDAO
import us.mn.state.health.dao.inventory.ItemDAO
import us.mn.state.health.dao.inventory.OrderFormulaDAO
import us.mn.state.health.dao.inventory.SensitiveAssetDAO
import us.mn.state.health.dao.inventory.StockItemActionRequestDAO
import us.mn.state.health.dao.inventory.StockItemDAO
import us.mn.state.health.dao.inventory.StockItemFacilityDAO
import us.mn.state.health.dao.inventory.StockQtyChangeReasonRefDAO
import us.mn.state.health.dao.inventory.UnitDAO
import us.mn.state.health.dao.materialsrequest.PurchaseItemDAO
import us.mn.state.health.dao.materialsrequest.RequestDAO
import us.mn.state.health.dao.materialsrequest.RequestEvaluationDAO
import us.mn.state.health.dao.materialsrequest.RequestLineItemDAO
import us.mn.state.health.dao.materialsrequest.RequestLineItemFundingSourceDAO
import us.mn.state.health.dao.materialsrequest.ShoppingListDAO
import us.mn.state.health.dao.purchasing.AssetViewDAO
import us.mn.state.health.dao.purchasing.HelpDeskTicketTrackingDAO
import us.mn.state.health.dao.purchasing.PurchasingOrderDAO
import us.mn.state.health.dao.purchasing.PurchasingOrderLineItemDAO
import us.mn.state.health.dao.receiving.DeliveryTicketDAO

/**
 * Created by demita1 on 5/16/2016.
 */
class DAOFactoryStub extends DAOFactory {

    @Override
    void commitTransaction(boolean beginNewTransactionAfterCommit) throws InfrastructureException {

    }

    @Override
    ActionRequestTypeDAO getActionRequestTypeDAO() {
        return null
    }

    @Override
    ItemDAO getItemDAO() {
        return null
    }

    @Override
    ManufacturerDAO getManufacturerDAO() {
        return null
    }

    @Override
    StockItemDAO getStockItemDAO() {
        return null
    }

    @Override
    CategoryDAO getCategoryDAO() {
        return null
    }

    @Override
    FiscalYearsDAO getFiscalYearsDAO() {
        return null
    }

    @Override
    StockItemActionRequestDAO getStockItemActionRequestDAO() {
        return null
    }

    @Override
    PersonDAO getPersonDAO() {
        return null
    }

    @Override
    EmployeeDAO getEmployeeDAO() {
        return null
    }

    @Override
    UnitDAO getUnitDAO() {
        return null
    }

    @Override
    OrgBudgetDAO getOrgBudgetDAO() {
        return null
    }

    @Override
    StatusDAO getStatusDAO() {
        return null
    }

    @Override
    GroupDAO getGroupDAO() {
        return null
    }

    @Override
    VendorDAO getVendorDAO() {
        return null
    }

    @Override
    OrderFormulaDAO getOrderFormulaDAO() {
        return null
    }

    @Override
    UserDAO getUserDAO() {
        return null
    }

    @Override
    StockItemFacilityDAO getStockItemFacilityDAO() {
        return null
    }

    @Override
    RequestLineItemDAO getRequestLineItemDAO() {
        return null
    }

    @Override
    RequestDAO getRequestDAO() {
        return null
    }

    @Override
    RequestLineItemFundingSourceDAO getRequestLineItemFundingSourceDAO() {
        return null
    }

    @Override
    RequestEvaluationDAO getRequestEvaluationDAO() {
        return null
    }

    @Override
    PriorityDAO getPriorityDAO() {
        return new PriorityDAOStub()
    }

    @Override
    FacilityDAO getFacilityDAO() {
        return null
    }

    @Override
    ShoppingListDAO getShoppingListDAO() {
        return null
    }

    @Override
    PurchaseItemDAO getPurchaseItemDAO() {
        return null
    }

    @Override
    PurchasingOrderDAO getPurchasingOrderDAO() {
        return null
    }

    @Override
    PurchasingOrderLineItemDAO getPurchasingOrderLineItemDAO() {
        return null
    }

    @Override
    HelpDeskTicketTrackingDAO getHelpDeskTicketTrackingDAO() {
        return null
    }

    @Override
    SensitiveAssetDAO getSensitiveAssetDAO() {
        return null
    }

    @Override
    FixedAssetDAO getFixedAssetDAO() {
        return null
    }

    @Override
    ClassCodeDAO getClassCodeDAO() {
        return null
    }

    @Override
    DeliveryTicketDAO getDeliveryTicketDAO() {
        return null
    }

    @Override
    ObjectCodeDAO getObjectCodeDAO() {
        return null
    }

    @Override
    StockQtyChangeReasonRefDAO getStockQtyChangeReasonRefDAO() {
        return null
    }

    @Override
    PhoneDAO getPhoneDAO() {
        return null
    }

    @Override
    ExternalOrgDetailDAO getExternalOrgDetailDAO() {
        return null
    }

    @Override
    MailingAddressDAO getMailingAddressDAO() {
        return null
    }

    @Override
    VendorContractDAO getVendorContractDAO() {
        return null
    }

    @Override
    VendorAccountDAO getVendorAccountDAO() {
        return null
    }

    @Override
    ExternalReportDAO getExternalReportDAO() {
        return null
    }

    @Override
    BusinessRulesORCLDAO getBusinessRulesORCLDAO() {
        return null
    }

    @Override
    NotificationEmailAddressDAO getNotificationEmailAddressDAO() {
        return null
    }

    @Override
    DeliveryDetailDAO getDeliveryDetailDAO() {
        return null
    }

    @Override
    AssetViewDAO getAssetViewDAO() {
        return null
    }
}
