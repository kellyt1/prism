package us.mn.state.health.model.inventory;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.model.common.Category;

public class OrderFormula implements Serializable {
    private Long orderFormulaId;
    private Double orderCost;
    private Integer leadTimeDays;
    private String insertedBy;
    private Date insertionDate;
    private String lastUpdatedBy;
    private Date lastUpdatedDate;
    private Category category;

    public OrderFormula() {
    }

    public void setOrderFormulaId(Long orderFormulaId) {
        this.orderFormulaId = orderFormulaId;
    }


    public Long getOrderFormulaId() {
        return orderFormulaId;
    }


    public void setOrderCost(Double orderCost) {
        this.orderCost = orderCost;
    }


    public Double getOrderCost() {
        return orderCost;
    }


    public void setLeadTimeDays(Integer leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }


    public Integer getLeadTimeDays() {
        return leadTimeDays;
    }


    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }


    public String getInsertedBy() {
        return insertedBy;
    }


    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }


    public Date getInsertionDate() {
        return insertionDate;
    }


    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }


    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }


    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }


    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    public Category getCategory() {
        return category;
    }
}