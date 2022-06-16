package us.mn.state.health.model.inventory;

import java.io.Serializable;

public class StockQtyChangeReasonRef implements Serializable {
    private Long id;
    private String reason;
    private String code;

    public static String RETURN_CODE = "RET";
    public static String INVENTORY_ADJUSTMENT_CODE = "ADJ";
    public static String TRANSFER_FROM_STOCK = "TFS";

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getReason() {
        return reason;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}