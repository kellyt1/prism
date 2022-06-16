package us.mn.state.health.model.legacySystem.stockItemMaterialRequests;

import java.sql.Timestamp;
import java.util.Collection;
/**
 * THIS CLASS REPRESENTS THE REQUEST CLASS
 */
public class OrdId {

    public static final String ORDNBR_COLUMN = "ORDNBR";
    public static final String CLIENTID_COLUMN = "CLIENT_ID";
    public static final String BNBR_COLUMN = "BNBR";
    public static final String LIT_COLUMN = "LIT";
    public static final String DATEIN_COLUMN = "DATEIN";
    public static final String DATEPRT_COLUMN = "DATEPRT";
    public static final String SPECIAL_COLUMN = "SPECIAL";
    public static final String FILLED_COLUMN = "FILLED";
    public static final String BO_COLUMN = "BO";
    public static final String CANCEL_COLUMN = "CANCEL";
    public static final String CONTACT_COLUMN = "CONTACT";
    public static final String ROOM_COLUMN = "ROOM";
    public static final String INITIAL_COLUMN = "INITIAL";

    private String ordNbr;
    private String clientId;
    private String bnbr;
    private boolean lit;
    private Timestamp dateIn;
    private Timestamp datePrt;
    private String special;
    private boolean filled;
    private boolean backOrder;
    private boolean cancel;
    private String contact;
    private String room;
    private String initial;

    private Collection requestLineItems;
    private OrdCl client;

    public String getBnbr() {
        return bnbr;
    }

    public void setBnbr(String bnbr) {
        this.bnbr = bnbr;
    }

    public Collection getRequestLineItems() {
        return requestLineItems;
    }

    public void setRequestLineItems(Collection requestLineItems) {
        this.requestLineItems = requestLineItems;
    }

    public OrdCl getClient() {
        return client;
    }

    public void setClient(OrdCl client) {
        this.client = client;
    }

    public String getOrdNbr() {
        return ordNbr;
    }

    public void setOrdNbr(String ordNbr) {
        this.ordNbr = ordNbr;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean getLit() {
        return lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
    }

    public Timestamp getDateIn() {
        return dateIn;
    }

    public void setDateIn(Timestamp dateIn) {
        this.dateIn = dateIn;
    }

    public Timestamp getDatePrt() {
        return datePrt;
    }

    public void setDatePrt(Timestamp datePrt) {
        this.datePrt = datePrt;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isBackOrder() {
        return backOrder;
    }

    public void setBackOrder(boolean backOrder) {
        this.backOrder = backOrder;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }


    public String toString() {
        return "OrdId{" +
                "ordNbr='" + ordNbr + "'" +
                ", clientId='" + clientId + "'" +
                ", lit=" + lit +
                ", datePrt=" + datePrt +
                ", filled=" + filled +
                ", backOrder=" + backOrder +
                ", cancel=" + cancel +
                ", contact='" + contact + "'" +
                ", room='" + room + "'" +
                ", initial='" + initial + "'" +
                ", requestLineItems=" + requestLineItems +
                ", special=" + special +
                ", client=" + client + "\n" +
                "}";
    }
}
