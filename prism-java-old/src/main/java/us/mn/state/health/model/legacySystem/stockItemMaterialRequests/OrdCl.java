package us.mn.state.health.model.legacySystem.stockItemMaterialRequests;

import java.sql.Timestamp;

/**
 * THIS CLASS REPRESENTS THE CONTACT DATA CLASS
 */
public class OrdCl {
    public static final String LASTORDER_COLUM = "LASTORD";
    public static final String NAME_COLUMN = "NAME";
    public static final String ADDRESS1_COLUMN = "ADDR1";
    public static final String ADDRESS2_COLUMN = "ADDR2";
    public static final String CITY_COLUM = "CITY";
    public static final String STATE_COLUMN = "ST";
    public static final String ZIP_COLUMN = "ZIP";
    public static final String TELEPHONE_COLUMN = "TELE";
    public static final String ATTN_COLUMN = "ATTN";
    public static final String TYPE_COLUMN = "TYPE";

    private Timestamp lastOrder;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String telephone;
    private String attn;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(Timestamp lastOrder) {
        this.lastOrder = lastOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }


    public String toString() {
        return "Contact {" +
                "lastOrder=" + lastOrder.toString().substring(0,lastOrder.toString().indexOf(" ")) +
                ", name='" + name + "'" +
                ", address1='" + address1 + "'" +
                ", address2='" + address2 + "'" +
                ", city='" + city + "'" +
                ", state='" + state + "'" +
                ", zip='" + zip + "'" +
                ", telephone='" + telephone + "'" +
                ", attn='" + attn + "'" +
                ", type='" + type + "'" +
                "}";
    }
}
