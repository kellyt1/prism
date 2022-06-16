package us.mn.state.health.model.legacySystem.inventory;

import java.util.Date;

/**
 * This class maps the INVTRY_DBF table
 * It has a 1 to 1 relationship with the class Purchase
 */
public class Inventory {
    private String icnbr;
    private String itemDescription;
    private String dispenseUnit;
    private Date ropDate;
    private String rop;
    private String roq;
    private String location;
    private String p;
    private String space;
    private String hazardous;
    private String hold;
    private String stag;
    private String bnbr;
    private String inac;
    private String outac;
    private String cost;
    private String onhand;
    private String belowrop;
    private String onorder;
    private String so;
    private String docnbr;
    private String buyDate;
    private String ordered;

    public static final String NO="NO";
    public static final String YES="YES";

    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getDispenseUnit() {
        return dispenseUnit;
    }

    public void setDispenseUnit(String dispenseUnit) {
        this.dispenseUnit = dispenseUnit;
    }

    public Date getRopDate() {
        return ropDate;
    }

    public void setRopDate(Date ropDate) {
        this.ropDate = ropDate;
    }

    public String getRop() {
        return rop;
    }

    public void setRop(String rop) {
        this.rop = rop;
    }

    public String getRoq() {
        return roq;
    }

    public void setRoq(String roq) {
        this.roq = roq;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getHazardous() {
        return hazardous;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getStag() {
        return stag;
    }

    public void setStag(String stag) {
        this.stag = stag;
    }

    public String getBnbr() {
        return bnbr;
    }

    public void setBnbr(String bnbr) {
        this.bnbr = bnbr;
    }

    public String getInac() {
        return inac;
    }

    public void setInac(String inac) {
        this.inac = inac;
    }

    public String getOutac() {
        return outac;
    }

    public void setOutac(String outac) {
        this.outac = outac;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOnhand() {
        return onhand;
    }

    public void setOnhand(String onhand) {
        this.onhand = onhand;
    }

    public String getBelowrop() {
        return belowrop;
    }

    public void setBelowrop(String belowrop) {
        this.belowrop = belowrop;
    }

    public String getOnorder() {
        return onorder;
    }

    public void setOnorder(String onorder) {
        this.onorder = onorder;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getDocnbr() {
        return docnbr;
    }

    public void setDocnbr(String docnbr) {
        this.docnbr = docnbr;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getOrdered() {
        return ordered;
    }

    public void setOrdered(String ordered) {
        this.ordered = ordered;
    }


    public String toString() {
        return "Inventory{" +
                "icnbr='" + icnbr + "'" +
                ", itemDescription='" + itemDescription + "'" +
                ", unit='" + dispenseUnit + "'" +
                ", ropDate='" + ropDate + "'" +
                ", rop='" + rop + "'" +
                ", roq='" + roq + "'" +
                ", location='" + location + "'" +
                ", p='" + p + "'" +
                ", space='" + space + "'" +
                ", hazardous='" + hazardous + "'" +
                ", hold='" + hold + "'" +
                ", stag='" + stag + "'" +
                ", bnbr='" + bnbr + "'" +
                ", inac='" + inac + "'" +
                ", outac='" + outac + "'" +
                ", cost='" + cost + "'" +
                ", onhand='" + onhand + "'" +
                ", belowrop='" + belowrop + "'" +
                ", onorder='" + onorder + "'" +
                ", so='" + so + "'" +
                ", docnbr='" + docnbr + "'" +
                ", buyDate='" + buyDate + "'" +
                ", ordered='" + ordered + "'" +
                "}";
    }
}
