package us.mn.state.health.model.legacySystem.inventory;

/**
 * This class maps the vendfle_dbf table that contains vendor records
 * It has a m to 1 relationship with the class Purchase
 */
public class Vendr {
    private String idNbr;
    private String name;
    private String addr;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String fax;

    public static final String DEFAULT_VENDR_ID = "0150304-008";
    public static final String UNKNOWN_VENDR_ID = "9999999-800";

    public String getIdNbr() {
        return idNbr;
    }

    public void setIdNbr(String idNbr) {
        this.idNbr = idNbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }


    public String toString() {
        return "Vendr{" +
                "idNbr='" + idNbr + '\'' +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }
}
