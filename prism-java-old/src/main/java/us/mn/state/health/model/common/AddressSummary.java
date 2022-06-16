package us.mn.state.health.model.common;

import java.io.Serializable;

public class AddressSummary implements Serializable {
    private String orgName;
    private String contactName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;

//    public AddressSummary(AddressBookExternalSource addBkExtSrc) {
//        if(addBkExtSrc.getExternalOrgDetail() != null) {
//            build(addBkExtSrc.getExternalOrgDetail().getPrimaryRep());
//            build(addBkExtSrc.getExternalOrgDetail().getPrimaryMailingAddress());
//        }
//        else {
//            build(addBkExtSrc.getContact());
//            build(addBkExtSrc.getContact().getPrimaryMailingAddress());
//        }
//    }

    public AddressSummary(MailingAddress mlngAddr) {
        build(mlngAddr);
    }

    private void build(MailingAddress mlngAddr) {
        this.address1 = mlngAddr.getAddress1();
        this.address2 = mlngAddr.getAddress2();
        this.city = mlngAddr.getCity();
        this.state = mlngAddr.getState();
        this.zip = mlngAddr.getZip();
    }

    private void build(Person person) {
        this.contactName = person.getFirstAndLastName();
    }

    public String getShortSummary() {
        String summary = "";
        if (contactName != null) {
            summary += contactName + ", ";
        }
        if (orgName != null) {
            summary += orgName + ", ";
        }
        summary += address1;

        return summary;
    }

    public String getLongSummary() {
        String summary = "";
        if (contactName != null) {
            summary += contactName + " \n";
        }
        if (orgName != null) {
            summary += orgName + " \n";
        }
        summary += address1 + " \n";
        summary += address2 + " \n";
        summary += city + ", ";
        summary += state + "  ";
        summary += zip;

        return summary;
    }


    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public String getOrgName() {
        return orgName;
    }

    public String getOrgNameFormatted() {
        if (orgName == null) {
            return "";
        } else {
            return orgName;
        }
    }

    public String getContactName() {
        return contactName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    public String getCity() {
        return city;
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
}