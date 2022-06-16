package us.mn.state.health.model.inventory;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.model.common.ModelMember;

public class StockItemLot extends ModelMember implements Serializable {
    private Long lotId;
    private String lotCode;
    private Date expirationDate;
    private StockItem stockItem;


    protected StockItemLot() {
    }

    public static StockItemLot createStockItemLot(StockItem stockItem, String lotCode, Date expDate, String user) {
        StockItemLot lot = new StockItemLot();
        lot.setExpirationDate(expDate);
        lot.setLotCode(lotCode);
        lot.setInsertedBy(user);
        lot.setInsertionDate(new Date());
        lot.setStockItem(stockItem);
        return lot;
    }


    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockItemLot lot = (StockItemLot) o;

        if (endDate != null ? !endDate.equals(lot.endDate) : lot.endDate != null) return false;
        if (endedBy != null ? !endedBy.equals(lot.endedBy) : lot.endedBy != null) return false;
        if (expirationDate != null ? !expirationDate.equals(lot.expirationDate) : lot.expirationDate != null)
            return false;
        if (!insertedBy.equals(lot.insertedBy)) return false;
        if (!insertionDate.equals(lot.insertionDate)) return false;
        if (lotCode != null ? !lotCode.equals(lot.lotCode) : lot.lotCode != null) return false;
        if (lotId != null ? !lotId.equals(lot.lotId) : lot.lotId != null) return false;
        if (updateDate != null ? !updateDate.equals(lot.updateDate) : lot.updateDate != null) return false;
        if (updatedBy != null ? !updatedBy.equals(lot.updatedBy) : lot.updatedBy != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (lotId != null ? lotId.hashCode() : 0);
        result = 31 * result + (lotCode != null ? lotCode.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + insertedBy.hashCode();
        result = 31 * result + insertionDate.hashCode();
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (endedBy != null ? endedBy.hashCode() : 0);
        return result;
    }
}
