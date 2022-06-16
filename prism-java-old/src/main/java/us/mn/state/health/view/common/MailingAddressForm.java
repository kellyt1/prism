package us.mn.state.health.view.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import java.util.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;

import us.mn.state.health.model.common.StateTerritoryProvince;

public class MailingAddressForm extends ValidatorForm {
    private Long mailingAddressId;
    private String address1;
    private String address2;
    private Boolean physicalType;
    private Boolean billToType;
    private Boolean shipToType;
    private Boolean remitToType;

    private String city;
    private String state;
    private String zip;
    private String endDate;
    private String zipPlus4;
    private String zipPlus8;
    private String internationalZip;
    private String countryCode;
    private String intlFlag;
    private Boolean intlFlagB;
    private String countryName;

     public void reset(ActionMapping mapping, HttpServletRequest request) {
        physicalType = Boolean.FALSE;
        billToType = Boolean.FALSE;
        shipToType = Boolean.FALSE;
        remitToType = Boolean.FALSE;
        address1 = "";
        address2 = "";
        city = "";
        state = "";
        zip = "";
        endDate = null;
        zipPlus4 = "";
        zipPlus8 = "";
        internationalZip = "";
        countryCode = "";
        intlFlag = "";
        intlFlagB = Boolean.FALSE;
        countryName = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }


    public void setMailingAddressId(Long mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }


    public Long getMailingAddressId() {
        return mailingAddressId;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCity() {
        return city;
    }


    public void setState(String state) {
        this.state = state;
    }


    public String getState() {
        return state;
    }


    public void setZip(String zip) {
        this.zip = zip;
    }


    public String getZip() {
        return zip;
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

    public String getShortSummary() {
        String result = address1 + ":" + (address2==null?"":address2 + ":")+city +":" + state + ":" + zip;
        if (intlFlag != null && intlFlag.equalsIgnoreCase("Y")) {
              result = address1 + ":" + (address2==null?"":address2 + ":")+city +":" + countryName + ":" + internationalZip;
        }
        return result;
    }

    public String getKey(){
        return this.toString();
    }
    public Collection getStates() {
          Collection outStates = new ArrayList();
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Alabama","AL"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Alaska","AK"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("ARMED FORCES PACIFIC","AP"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("ARMED FORCES EUROPE","AE"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("ARMED FORCES AMERICA","AA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Arizona","AZ"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Arkansas","AR"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("California","CA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Colorado","CO"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Conneticut","CT"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Deleware","DE"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Florida","FL"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Georgia","GA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Guam","GU"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Hawaii","HI"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Idaho","ID"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Illinois","IL"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Indiana","IN"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Iowa","IA"));

        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Kansas","KS"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Kentucky","KY"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Louisiana","LA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Maine","ME"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Maryland","MD"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Massachusetts","MA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Michigan","MI"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Minnesota","MN"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Mississippi","MS"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Missouri","MO"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Montana","MT"));
                                                                                                                                                        

        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Nebraska","NE"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Nevada","NV"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("New Hampshire","NH"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("New Jersey","NJ"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("New Mexico","NM"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("New York","NY"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("North Carolina","NC"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("North Dakota","ND"));


        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Ohio","OH"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Oklahoma","OK"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Oregon","OR"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Pennsylvania","PA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Puerto Rico","PR"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Rhode Island","RI"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("South Carolina","SC"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("South Dakota","SD"));

        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Tennessee","TN"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Texas","TX"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Utah","UT"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Vermont","VT"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Virginia","VA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Virgin Islands","VI"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Washington","WA"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Washington D.C","DC"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("West Virginia","WV"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Wisconsin","WI"));
        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Wyoming","WY"));

        outStates.add(StateTerritoryProvince.createStateTerritoryProvince("Outside US","XX"));


      //    outStates.add()


          return outStates;
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


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
    public Boolean getIntlFlagB() {
        if (intlFlag != null && intlFlag.equalsIgnoreCase("Y")) return true;
        else return false;
    }

    public void setIntlFlagB(Boolean intlFlagB) {
        this.intlFlagB = intlFlagB;
        if (intlFlagB) {
            intlFlag = "Y";
            state = "XX";
            zip = "99999";
        }
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}