package us.mn.state.health.model.legacySystem.inventory;

/**
 * This class maps the PRCHFLE_DBF table that contains information about
 * the purchasing of an item
 * It has a 1 to 1 relationship with the class Inventory
 */

public class Purchase extends Inventory{
    private String vendorId1;
    private String vendorId2;
    private String vendorId3;
//    private Vendr vendor1;
    private String itemNbr;
    private String retCost;
    private String discount;
    private String ourCost;
    private String buyROP;
    private String buyUnit;
    private String cont;
    private String expDate;
    private String delivery;
    private String code;
    private String usage;
    private String eoq;
//    private Vendr vendor2;
    private String itemNbr2;
//    private Vendr vendor3;
    private String itemNbr3;
    private String count;
    private String lastSODate;
    private String lastROPDate;
    private String mismatch;
    private String created;
    private String history;

    public String getVendorId1() {
        return vendorId1;
    }

    public void setVendorId1(String vendorId1) {
        this.vendorId1 = vendorId1;
    }

    public String getVendorId2() {
        return vendorId2;
    }

    public void setVendorId2(String vendorId2) {
        this.vendorId2 = vendorId2;
    }

    public String getVendorId3() {
        return vendorId3;
    }

    public void setVendorId3(String vendorId3) {
        this.vendorId3 = vendorId3;
    }

//    public Vendr getVendor1() {
//        return vendor1;
//    }
//
//    public void setVendor1(Vendr vendor1) {
//        this.vendor1 = vendor1;
//    }

    public String getItemNbr() {
        return itemNbr;
    }

    public void setItemNbr(String itemNbr) {
        this.itemNbr = itemNbr;
    }

    public String getRetCost() {
        return retCost;
    }

    public void setRetCost(String retCost) {
        this.retCost = retCost;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOurCost() {
        return ourCost;
    }

    public void setOurCost(String ourCost) {
        this.ourCost = ourCost;
    }

    public String getBuyROP() {
        return buyROP;
    }

    public void setBuyROP(String buyROP) {
        this.buyROP = buyROP;
    }

    public String getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(String buyUnit) {
        this.buyUnit = buyUnit;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getEoq() {
        return eoq;
    }

    public void setEoq(String eoq) {
        this.eoq = eoq;
    }

    public String getItemNbr2() {
        return itemNbr2;
    }

    public void setItemNbr2(String itemNbr2) {
        this.itemNbr2 = itemNbr2;
    }

    public String getItemNbr3() {
        return itemNbr3;
    }

    public void setItemNbr3(String itemNbr3) {
        this.itemNbr3 = itemNbr3;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLastSODate() {
        return lastSODate;
    }

    public void setLastSODate(String lastSODate) {
        this.lastSODate = lastSODate;
    }

    public String getLastROPDate() {
        return lastROPDate;
    }

    public void setLastROPDate(String lastROPDate) {
        this.lastROPDate = lastROPDate;
    }

    public String getMismatch() {
        return mismatch;
    }

    public void setMismatch(String mismatch) {
        this.mismatch = mismatch;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

//    public Vendr getVendor2() {
//        return vendor2;
//    }
//
//    public void setVendor2(Vendr vendor2) {
//        this.vendor2 = vendor2;
//    }
//
//    public Vendr getVendor3() {
//        return vendor3;
//    }
//
//    public void setVendor3(Vendr vendor3) {
//        this.vendor3 = vendor3;
//    }

    public String toString() {
        return super.toString() + "\n" + "Purchase{" +
//                "vendor1=" + vendor1 +
                ", itemNbr='" + itemNbr + "'" +
                ", retCost='" + retCost + "'" +
                ", discount='" + discount + "'" +
                ", ourCost='" + ourCost + "'" +
                ", buyROP='" + buyROP + "'" +
                ", unit='" + buyUnit + "'" +
                ", cont='" + cont + "'" +
                ", expDate='" + expDate + "'" +
                ", delivery='" + delivery + "'" +
                ", code='" + code + "'" +
                ", usage='" + usage + "'" +
                ", eoq='" + eoq + "'" +
//                ", altVendorId2='" + vendor2 + "'" +
                ", itemNbr2='" + itemNbr2 + "'" +
//                ", altVendorId3='" + vendor3 + "'" +
                ", itemNbr3='" + itemNbr3 + "'" +
                ", count='" + count + "'" +
                ", soDate='" + lastSODate + "'" +
                ", ROPDate='" + lastROPDate + "'" +
                "}";
    }
}
