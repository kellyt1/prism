package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

public class StatusType implements Serializable {
    private Long statusTypeId;
    private String name;
    private Date insertionDate;
    private String insertedBy;
    private String code;

    public static final String MATERIALS_REQUEST = "MRQ";
    public static final String MATERIALS_ORDER = "MOR";
    public static final String STOCK_ITEM_ACTION_REQUEST = "SIA";
    public static final String STOCK_ITEM = "STI";
    public static final String ASSET = "ASS";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getStatusTypeId() {
        return statusTypeId;
    }

    public void setStatusTypeId(Long statusTypeId) {
        this.statusTypeId = statusTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }


    public Date getInsertionDate() {
        return insertionDate;
    }


    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }


    public String getInsertedBy() {
        return insertedBy;
    }


    public String getSTOCK_ITEM_ACTION_REQUEST() {
        return STOCK_ITEM_ACTION_REQUEST;
    }


    public String getSTOCK_ITEM() {
        return STOCK_ITEM;
    }
}
