package us.mn.state.health.domain.repository.inventory;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.util.hibernate.SearchCriteria;

public class StockItemSearchCriteria extends SearchCriteria implements Serializable {
//    public static final int SORT_BY_REQUEST_ID = 0;
//    public static final int SORT_BY_DATE_REQUESTED = 1;
//
//    private int sortBy = SORT_BY_REQUEST_ID;

    private String statusId;
    private String holdUntilDateString;
    private Date holdUntilDate;

    public boolean isStatusIdSpecified() {
        return isSpecified(statusId);
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public boolean isHoldUntilDateStringSpecified() {
        return isSpecified(getHoldUntilDateString());
    }

    public boolean isHoldUntilDateSpecified() {
        return isSpecified(getHoldUntilDate());
    }

    public String getHoldUntilDateString() {
        return holdUntilDateString;
    }

    public void setHoldUntilDateString(String holdUntilDateString) {
        this.holdUntilDateString = holdUntilDateString;
    }

    public void setHoldUntilDate(Date holdUntilDate) {
        this.holdUntilDate = holdUntilDate;
    }

    public Date getHoldUntilDate() {
        return holdUntilDate;
    }
}
