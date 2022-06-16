package us.mn.state.health.model.purchasing;


/**
 * Created by IntelliJ IDEA.
 * User: RodenT1
 * Date: Apr 14, 2009
 * Time: 3:20:02 PM
 * To change this template use File | Settings | File Templates.
 */


public class HelpDeskTicketTracking {
    private static final String TRUNCATED_MSG = "<br><b>TRUNCATED...SEE PRISM FOR COMPLETE REQUEST</b>";
    Long trackingId;
    String  orgDiv;
    String  contactName;
    String  contactPhone;
    String  contactEmail;
    String  category;
    String  subCategory;
    String  description;
    String  status;
    Date    insertDate;
    String  insertedBy;
    Long    personId;
    Date    origDate;
    String  copyEE1;
    String  copyEE2;
    String  copyEE3;
    String  buildingOffice;
    String  assignedDiv;
    String  assignedTo;
    Date    dateNeeded;
    String  trackingNumber;
    String statusComments

    public HelpDeskTicketTracking() {
        assignedDiv = "istm";
        assignedTo = "Health.ITpurchasing@state.mn.us";
        insertedBy = "PARIT";
        status = "open";
        origDate = new Date();
        insertDate = new Date();
        category = "Desktop Hardware";
        subCategory = "unknown";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description?.length() < 4000 ? description : description?.substring(0, 4000 - TRUNCATED_MSG.length()) + TRUNCATED_MSG;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory?.length() < 100 ? subCategory : subCategory?.substring(0, 100)
    }
}