package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.inventory.*;
import us.mn.state.health.dao.materialsrequest.*;
import us.mn.state.health.dao.purchasing.AssetViewDAO;
import us.mn.state.health.dao.purchasing.HelpDeskTicketTrackingDAO;
import us.mn.state.health.dao.purchasing.PurchasingOrderDAO;
import us.mn.state.health.dao.purchasing.PurchasingOrderLineItemDAO;
import us.mn.state.health.dao.receiving.DeliveryTicketDAO;

public abstract class DAOFactory {
    //list of DAO types supported by the factory
    public static final int ORACLE = 1;
    public static final int LDAP = 2;
    public static final int HIBERNATE = 3;
    public static final int LUCENE = 4;
    public static final int DEFAULT = HIBERNATE;

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case ORACLE:
                return new OracleDAOFactory();
            case LDAP:
                 return new LdapDAOFactory();
            case HIBERNATE:
                return new HibernateDAOFactory();
            case LUCENE:
                return new LuceneDAOFactory();
            default:
                return null;
        }
    }

    public abstract void commitTransaction(boolean beginNewTransactionAfterCommit) throws InfrastructureException;

    /**
     * There will be an abstract method for each specific DAO that can
     * be created.  The concrete factories will have to
     * implement these methods.
     */
    public abstract ActionRequestTypeDAO getActionRequestTypeDAO();

    public abstract ItemDAO getItemDAO();

    public abstract ManufacturerDAO getManufacturerDAO();

    public abstract StockItemDAO getStockItemDAO();

    public abstract CategoryDAO getCategoryDAO();

    public abstract FiscalYearsDAO getFiscalYearsDAO();

    public abstract StockItemActionRequestDAO getStockItemActionRequestDAO();

    public abstract PersonDAO getPersonDAO();

    public abstract EmployeeDAO getEmployeeDAO();

    public abstract UnitDAO getUnitDAO();

    public abstract OrgBudgetDAO getOrgBudgetDAO();

    public abstract StatusDAO getStatusDAO();

    public abstract GroupDAO getGroupDAO();

    public abstract VendorDAO getVendorDAO();

    public abstract OrderFormulaDAO getOrderFormulaDAO();

    public abstract UserDAO getUserDAO();

    public abstract StockItemFacilityDAO getStockItemFacilityDAO();

    public abstract RequestLineItemDAO getRequestLineItemDAO();

    public abstract RequestDAO getRequestDAO();

    public abstract RequestLineItemFundingSourceDAO getRequestLineItemFundingSourceDAO();

    public abstract RequestEvaluationDAO getRequestEvaluationDAO();

    public abstract PriorityDAO getPriorityDAO();

    public abstract FacilityDAO getFacilityDAO();

    public abstract ShoppingListDAO getShoppingListDAO();

    public abstract PurchaseItemDAO getPurchaseItemDAO();

    public abstract PurchasingOrderDAO getPurchasingOrderDAO();

    public abstract PurchasingOrderLineItemDAO getPurchasingOrderLineItemDAO();

    public abstract HelpDeskTicketTrackingDAO getHelpDeskTicketTrackingDAO() ;

    public abstract SensitiveAssetDAO getSensitiveAssetDAO();

    public abstract FixedAssetDAO getFixedAssetDAO();

    public abstract ClassCodeDAO getClassCodeDAO();

    public abstract DeliveryTicketDAO getDeliveryTicketDAO();

    public abstract ObjectCodeDAO getObjectCodeDAO();

    public abstract StockQtyChangeReasonRefDAO getStockQtyChangeReasonRefDAO();

    public abstract PhoneDAO getPhoneDAO();

    public abstract ExternalOrgDetailDAO getExternalOrgDetailDAO();

    public abstract MailingAddressDAO getMailingAddressDAO();

    public abstract VendorContractDAO getVendorContractDAO();

    public abstract VendorAccountDAO getVendorAccountDAO();
    
    public abstract ExternalReportDAO getExternalReportDAO();

    public abstract BusinessRulesORCLDAO getBusinessRulesORCLDAO();

    public abstract NotificationEmailAddressDAO getNotificationEmailAddressDAO();

    public abstract DeliveryDetailDAO getDeliveryDetailDAO();

    public abstract AssetViewDAO getAssetViewDAO();
}