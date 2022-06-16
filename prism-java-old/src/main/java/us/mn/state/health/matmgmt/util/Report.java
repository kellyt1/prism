package us.mn.state.health.matmgmt.util;

import java.io.File;

public class Report {
    //    public static final String REPORTS_DIR_PATH_NAME = "/reports/";
    public static final String REPORTS_DIR_PATH_NAME = (System.getProperty("PRISM_REPORTS") == null ? System.getenv("PRISM_REPORTS") : System.getProperty("PRISM_REPORTS")) + File.separatorChar;
    public static final String IMAGES_DIR_PATH_NAME = "/images/";    
    public static final String PURCHASING_OUTPUT_PATH = "/purchasing/";
    public static final String GENERAL_REPORT_OUTPUT_PATH = "/reports/";    
    public static final String RECEIVING_OUTPUT_PATH = "/receiving/";
    public static final String INVENTORY_OUTPUT_PATH = "/inventory/";
    public static final String PURCHASE_ORDER = REPORTS_DIR_PATH_NAME + "PurchaseOrder";    
    public static final String PURCHASE_ORDER_INTERNAL = REPORTS_DIR_PATH_NAME + "PurchaseOrderInternal";
    public static final String PURCHASE_ORDER_INTERNAL_SWIFT = REPORTS_DIR_PATH_NAME + "PurchaseOrderInternalSWIFT";
    public static final String ACCOUNTS_PAYABLE_PURCHASE_ORDER = REPORTS_DIR_PATH_NAME + "AccountsPayablePurchaseOrder";
    public static final String MAILING_LABELS = REPORTS_DIR_PATH_NAME + "mailingLabels";
    public static final String PACKING_SLIP = REPORTS_DIR_PATH_NAME + "packingSlip";
    public static final String RLI_SUB_REPORT = REPORTS_DIR_PATH_NAME + "requestLineItemsSubReport";
    public static final String PICK_LIST = REPORTS_DIR_PATH_NAME + "picklist";
    public static final String DELIVERY_TICKET = REPORTS_DIR_PATH_NAME + "deliveryTicket";
    public static final String TO_BE_STOCKED = REPORTS_DIR_PATH_NAME + "ToBeStocked";
    public static final String DELIVERY_RECEIPT = REPORTS_DIR_PATH_NAME + "deliveryReceipt";
}