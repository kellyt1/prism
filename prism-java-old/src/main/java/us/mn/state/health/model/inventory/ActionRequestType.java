package us.mn.state.health.model.inventory;

import java.io.Serializable;

public class ActionRequestType implements Serializable {

    private String name;
    private String code;
    private Long actionRequestTypeId;

    public static final String NEW_STOCK_ITEM = "NSI";
    public static final String STOCK_ITEM_MODIFICATIONS = "SIM";

    public static final String ACTION_TYPE = "actionType";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getActionRequestTypeId() {
        return actionRequestTypeId;
    }

    public void setActionRequestTypeId(Long actionRequestTypeId) {
        this.actionRequestTypeId = actionRequestTypeId;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }


    public String getNEW_STOCK_ITEM() {
        return NEW_STOCK_ITEM;
    }


    public String getSTOCK_ITEM_MODIFICATIONS() {
        return STOCK_ITEM_MODIFICATIONS;
    }
}
