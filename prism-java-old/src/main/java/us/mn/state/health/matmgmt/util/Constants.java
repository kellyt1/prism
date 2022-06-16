package us.mn.state.health.matmgmt.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Constants {
    private static Log log = LogFactory.getLog(Constants.class);

    public static final String MATERIALS_MANAGMENT_CODE = "MatMgmt";
    public static final String MATERIALS_MANAGMENT_NAME = "Materials Management";

    public static final String REQUEST_INDEX_PATH_NAME = "The directory of the Request Lucene Index";
    public static final String TEMP_REQUEST_INDEX_PATH_NAME = "The temp directory of the Request Lucene Index";
    public static final String REQUEST_INDEX_PATH_CODE = "RIP";
    public static final String TEMP_REQUEST_INDEX_PATH_CODE = "TRIP";

    public static final String REQUEST_LINE_ITEM_INDEX_PATH_NAME = "The directory of the Request Line Item Lucene Index";
    public static final String TEMP_REQUEST_LINE_ITEM_INDEX_PATH_NAME = "The temp directory of the Request Line Item Lucene Index";
    public static final String REQUEST_LINE_ITEM_INDEX_PATH_CODE = "RLIIP";
    public static final String TEMP_REQUEST_LINE_ITEM_INDEX_PATH_CODE = "TRLIIP";

    public static final String PURCHASE_ITEM_INDEX_PATH_NAME = "The directory of the Purchase Item Lucene Index";
    public static final String TEMP_PURCHASE_ITEM_INDEX_PATH_NAME = "The temp directory of the Purchase Item Lucene Index";
    public static final String PURCHASE_ITEM_INDEX_PATH_CODE = "PIP";
    public static final String TEMP_PURCHASE_ITEM_INDEX_PATH_CODE = "TPIP";

    public static final String STOCK_ITEM_INDEX_PATH_NAME = "The directory of the Stock Item Lucene Index";
    public static final String TEMP_STOCK_ITEM_INDEX_PATH_NAME = "The temp directory of the Stock Item Lucene Index";
    public static final String STOCK_ITEM_INDEX_PATH_CODE = "SIIP";
    public static final String TEMP_STOCK_ITEM_INDEX_PATH_CODE = "TSIIP";
    public static final String PARIT_COMPUTER_SHIP_TO = "PARITCOMPUTERSHIPTO";

    public static final String RULES4J_CONFIGURATION_PATH_NAME = "The data directory of Rules4J";
    public static final String RULES4J_CONFIGURATION_PATH_CODE = "R4JP";

    public static final String INVENTORY_REPORTS_PATH_NAME = "The data directory of the IReport Files for inventory reports";
    public static final String INVENTORY_REPORTS_PATH_CODE = "IRP";

    public static final String RECEIVING_REPORTS_PATH_NAME = "The directory of the iReport files for Receiving reports";
    public static final String RECEIVING_REPORTS_PATH_CODE = "RRPTS";

    public static final String ORDER_INDEX_PATH_NAME = "The directory of the Orders Lucene Index";
    public static final String TEMP_ORDER_INDEX_PATH_NAME = "The temp directory of the Orders Lucene Index";
    public static final String ORDER_INDEX_PATH_CODE = "OLIP";
    public static final String TEMP_ORDER_INDEX_PATH_CODE = "TOLIP";

    public static final String ORDER_LINE_ITEM_INDEX_PATH_NAME = "The directory of the OrderLineItems Lucene Index";
    public static final String TEMP_ORDER_LINE_ITEM_INDEX_PATH_NAME = "The temp directory of the OrderLineItems Lucene Index";
    public static final String ORDER_LINE_ITEM_INDEX_PATH_CODE = "OLIIP";
    public static final String TEMP_ORDER_LINE_ITEM_INDEX_PATH_CODE = "TOLIIP";

    public static final String ASSET_INDEX_PATH_NAME = "The directory of the Fixed and Sensitive Assets Index";
    public static final String TEMP_ASSET_INDEX_PATH_NAME = "The temp directory of the Fixed and Sensitive Assets Index";
    public static final String ASSET_INDEX_PATH_CODE = "ALIP";
    public static final String TEMP_ASSET_INDEX_PATH_CODE = "TALIP";

    public static final String ENV_KEY = "enviromentKey";
    public static final String DEVDB = "DEVDB";
    public static final String TEST = "TEST";
    public static final String STAGE = "STAGE";
    public static final String PROD = "PRDI";

    public static final String PRDI_SESSION_FACTORY = "sessionFactory_PRDI";
    public static final String STG_SESSION_FACTORY = "sessionFactory_STG";
    public static final String TST_SESSION_FACTORY = "sessionFactory_TST";
    public static final String DEV_SESSION_FACTORY = "sessionFactory_DEV";
    public static final String DEPLOYMENT_SESSION_FACTORY = "sessionFactory";

    public static final String YES = "y";
    public static final String NO = "n";

    public static final String DAYS_TO_CANCELATION = "DAYS_TO_CANCEL";
    public static final String DAYS_TO_WARNING = "DAYS_TO_WARN";

    public static final String MESSAGE_NOTIFY_LOGIN = "MESSAGE_NOTIFY_LOGIN";
    public static final String MESSAGE_NOTIFY_MAINMENU = "MESSAGE_NOTIFY_MAINMENU";
    public static final String BUDGET_BUILDER_LINK = "BUDGET_BUILDER_LINK";
    public static final String ITPurchase = "SCOMP";
    //Added OR logic to catch missed IT purchase items entered in PRISM instead of PARIT.
    public static final String ITPurchase2 = "REQUEST0";

    public static final String RETRIEVE_GROUP_MEMBERSHIP_FROM = "RETRIEVE_GROUP_MEMBERSHIP_FROM";
    public static final String RETRIEVE_GROUP_MEMBERSHIP_FROM_MS_ACTIVE_DIRECTORY = "MS_ACTIVE_DIRECTORY";
    public static final String RETRIEVE_GROUP_MEMBERSHIP_FROM_ORACLE_TABLES = "ORACLE_TABLES";

    public static final String AUTHENTICATION_SERVER = "AUTHENTICATION_SERVER";


    //Federal Reporting Category is tied to the Appropriation code unless the
    //Appropriation code is 202 in which case the Reporting Category is tied
    //to the Org Budget Code.
    public static final String FEDERAL_REPORTING_CATEGORY_SPECIAL_APPROPRIATION_CODE = "202";
    public static final String REMEDYSERVER = "ars@bmc-tst-app.oet.state.mn.us";
    //Constants used for the additional functionality of Two Level Approvals
    public static final int APPROVAL_LEVEL_ONE = 1;
    public static final int APPROVAL_LEVEL_TWO = 2;

    public static final String APPROVER = "Approver";
    public static final String LEVEL_ONE_APPROVER = "Level One Approver";
    public static final String LEVEL_TWO_APPROVER = "Level Two Approver";
    public static final String LEVEL_ONE_AND_LEVEL_TWO_APPROVER = "Level One and Level Two Approver";
    public static final String CLICK_TO_VIEW_DETAILS = "Click to view details";
    public static final String WAITING_FOR_OTHERS_TO_DO_LEVEL_ONE_APPROVAL = "Waiting for Other(s) to do Level One Approval";
    public static final String DASHES_TWO = " -- ";
    public static final String NO_ACTION_REQUIRED_BY_YOU = "No action required by you";

    public static final String TRIGGER_TEXT1_TO_DETERMINE_WHETHER_TO_DISPLAY_APPROVAL_STATUS_PAGE = APPROVER;
    public static final String TRIGGER_TEXT2_TO_DETERMINE_WHETHER_TO_DISPLAY_APPROVAL_STATUS_PAGE = CLICK_TO_VIEW_DETAILS;

    public static final String AUTHENTICATION_SERVER_2 = "AUTHENTICATION_SERVER_2";
    public static final String KEYCLOAK_TOKEN_ENDPOINT = getEnvProperty("kc.token.endpoint");
    public static final String KEYCLOAK_USERINFO_ENDPOINT = getEnvProperty("kc.userinfo.endpoint");
    public static final String KEYCLOAK_CLIENT_ID = getEnvProperty("kc.clientId");
    public static final String ENVIRONMENT = getEnvProperty("environment");

    private static String getEnvProperty(String name) {
        String value = null;
        try{
            InitialContext ic = new InitialContext();
            Context xmlContext = (Context) ic.lookup("java:comp/env");
            value = (String) xmlContext.lookup(name);
        } catch (NamingException e){
            log.error("Error retrieving environment property named: "+ name +" from Context.xml:", e);
        }
        return value;
    }
}