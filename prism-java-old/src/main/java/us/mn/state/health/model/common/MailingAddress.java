package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

public class MailingAddress implements Serializable {
    private Long mailingAddressId;
    private String address1;
    private String address2;
    private Boolean physicalType;
    private String city;
    private String state;
    private String zip;
    private String zipPlus4;
    private String zipPlus8;
    private String internationalZip;
    private String countryCode;
    private String intlFlag;
    private String countryName;
    private String insertedBy;
    private Date insertionDate;
    private Date endDate;
    private Boolean billToType;
    private Boolean shipToType;
    private Boolean remitToType;
    private Boolean mdhOwned;
    private String addressNotes;
    
    public static final String PHYSICAL_TYPE = "PHYSICAL_TYPE";
    public static final String BILL_TO_TYPE = "BILL_TO_TYPE";
    public static final String SHIP_TO_TYPE = "SHIP_TO_TYPE";
    public static final String REMIT_TO_TYPE = "REMIT_TO_TYPE";


    public String getAddressNotes() {
        return addressNotes;
    }

    public void setAddressNotes(String addressNotes) {
        this.addressNotes = addressNotes;
    }

    /**
     * Compares the fields of this MailingAddress to the object 
     * passed in (if its a MailingAddress. 
     * Note - we cannot just compare the ID's of the objects, because
     * one of them might be new, and so it wouldn't have an ID yet.
     * @return 
     * @param o
     */
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if (!(o instanceof MailingAddress)) return false;

        final MailingAddress that = (MailingAddress)o;

        if(this.getMailingAddressId() == null) {
            if(that.getMailingAddressId() == null) {
                //dig deeper, using comparison by value
                if(this.getInsertedBy() != null && !this.getInsertedBy().equals(that.getInsertedBy())) {
                    return false;
                }

                if(this.getInsertionDate() != null && !this.getInsertionDate().equals(that.getInsertionDate())) {
                    return false;
                }

                if(this.getAddress1() != null && !this.getAddress1().equals(that.getAddress1())) {
                    return false;
                }

                if(this.getAddress2() != null && !this.getAddress2().equals(that.getAddress2())) {
                    return false;
                }

                if(this.getCity() != null && !this.getCity().equals(that.getCity())) {
                    return false;
                }

                if(this.getZip() != null && !this.getZip().equals(that.getZip())) {
                    return false;
                }

                if(this.getState() != null && !this.getState().equals(that.getState())) {
                    return false;
                }

                return true;
            }
            else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        }
        else {  //we know we can't get a NullPointerException now...
            return this.getMailingAddressId().equals(that.getMailingAddressId());
        }
    }

    public int hashCode() {
        int result = 13;
        result = (getCity() != null ? getCity().hashCode() : 0);
        result = 29 * result + (getAddress1() != null ? getAddress1().hashCode() : 0);
        return result;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Long getMailingAddressId() {
        return mailingAddressId;
    }

    public void setMailingAddressId(Long mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


    public String getZipPlus4() {
        return zipPlus4;
    }

    public void setZipPlus4(String zipPlus4) {
        this.zipPlus4 = zipPlus4;
    }

    public String getZipPlus8() {
        return zipPlus8;
    }

    public void setZipPlus8(String zipPlus8) {
        this.zipPlus8 = zipPlus8;
    }

    public String getInternationalZip() {
        return internationalZip;
    }

    public void setInternationalZip(String internationalZip) {
        this.internationalZip = internationalZip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIntlFlag() {
        return intlFlag;
    }

    public void setIntlFlag(String intlFlag) {
        this.intlFlag = intlFlag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getPhysicalType() {
        return physicalType;
    }

    public void setPhysicalType(Boolean physicalType) {
        this.physicalType = physicalType;
    }

    public Boolean getBillToType() {
        return billToType;
    }

    public void setBillToType(Boolean billToType) {
        this.billToType = billToType;
    }

    public Boolean getShipToType() {
        return shipToType;
    }

    public void setShipToType(Boolean shipToType) {
        this.shipToType = shipToType;
    }

    public Boolean getRemitToType() {
        return remitToType;
    }

    public void setRemitToType(Boolean remitToType) {
        this.remitToType = remitToType;
    }

    public String getCity() {
        return city;
    }

    public String getCityAndAddress() {
              StringBuffer buffer = new StringBuffer();
              buffer.append(city).append(" - ");
              buffer.append(address1);
//              if(address2 != null){
//                  buffer.append(address2).append(", ");
//              }

              return buffer.toString();

    }

    public String getCityStateZip() {
        if(city != null && state != null && zip != null) {
            return city + ", " + state + "  " + zip;
        }
        return null;
    }

    public String getShortSummary(){
        StringBuffer buffer = new StringBuffer();
        if (address1 != null && !address1.trim().equals("")) {
            buffer.append(address1).append("\n");
        }
        if(address2 != null){
            buffer.append(address2).append("\n");
        }

        buffer.append(city).append(", ");
         if (intlFlag != null && intlFlag.equalsIgnoreCase("Y")) {
            buffer.append(countryName).append(" ");
            buffer.append(internationalZip);

         }   else {
            buffer.append(state).append(" ");
            buffer.append(zip);
         }
        return buffer.toString();
    }

    

    public String getShortSummaryInOneLine(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(address1).append(", ");
        if(address2 != null){
            buffer.append(address2).append(", ");
        }
        buffer.append(city).append(", ");
        if (intlFlag != null && intlFlag.equalsIgnoreCase("Y")) {
           buffer.append(countryName).append(" ");
           buffer.append(internationalZip);

        }   else {
           buffer.append(state).append(" ");
           buffer.append(zip);
        }

//        buffer.append(state).append(" ");
//        buffer.append(zip);
        return buffer.toString();
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }


    public String getAddress1() {
        return address1;
    }


    public void setAddress2(String address2) {
        this.address2 = address2;
    }


    public String getAddress2() {
        return address2;
    }


    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    public String getCountryName() {
        return countryName;
    }

    public Date getEndDate() {
        return endDate;
    }

  
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getMdhOwned() {
        return mdhOwned;
    }

    public void setMdhOwned(Boolean mdhOwned) {
        this.mdhOwned = mdhOwned;
    }

}
