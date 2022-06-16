package us.mn.state.health.model.legacySystem.common;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.common.lang.StringUtils;

/**
 * Class that maps the ASAP_ORG_TBL
 */
public class ExternalOrgASAP {
    private ExternalOrgASAPId externalOrgASAPId;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String telephone;
    private String attention;
    private String type;

    public static class ExternalOrgASAPId implements Serializable {
        private String name;
        private Date lastOderDate;

        public ExternalOrgASAPId() {
        }

        public ExternalOrgASAPId(Date lastOderDate, String name) {
            this.lastOderDate = lastOderDate;
            this.name = name;
        }

        public Date getLastOderDate() {
            return lastOderDate;
        }

        public void setLastOderDate(Date lastOderDate) {
            this.lastOderDate = lastOderDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final ExternalOrgASAPId that = (ExternalOrgASAPId) o;

            if (!lastOderDate.equals(that.lastOderDate)) return false;
            if (!name.equals(that.name)) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = lastOderDate.hashCode();
            result = 29 * result + name.hashCode();
            return result;
        }


        public String toString() {
            return "name='" + name + '\'' +
                    ", lastOderDate=" + lastOderDate;

        }
    }

    public ExternalOrgASAPId getExternalOrgASAPId() {
        return externalOrgASAPId;
    }

    public void setExternalOrgASAPId(ExternalOrgASAPId externalOrgASAPId) {
        this.externalOrgASAPId = externalOrgASAPId;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ExternalOrgASAP that = (ExternalOrgASAP) o;

        if (!StringUtils.trim(address1).equals(StringUtils.trim(that.address1))) return false;
        if (!StringUtils.nullOrBlank(address2)
                ? !StringUtils.trim(address2).equals(StringUtils.trim(that.address2))
                : !StringUtils.nullOrBlank(that.address2))
            return false;
        if (!StringUtils.trim(city).equals(StringUtils.trim(that.city))) return false;
        if (!StringUtils.trim(externalOrgASAPId.getName()).equals(StringUtils.trim(that.externalOrgASAPId.getName()))) return false;
        if (!StringUtils.trim(state).equals(StringUtils.trim(that.state))) return false;
//        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;
        String z1 = zipCode.substring(0, 5);
        String z2 = that.zipCode.substring(0, 5);
        if (zipCode != null ? !z1.equals(z2) : that.zipCode != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = externalOrgASAPId.getName().hashCode();
        result = 29 * result + StringUtils.trim(address1).hashCode();
        result = 29 * result + (!StringUtils.nullOrBlank(address2) ? StringUtils.trim(address2).hashCode() : 0);
        result = 29 * result + StringUtils.trim(city).hashCode();
        result = 29 * result + StringUtils.trim(state).hashCode();
        result = 29 * result + (zipCode != null ? zipCode.substring(0, 5).hashCode() : 0);
        return result;
    }


    public String toString() {
        return "ExternalOrgASAP{" +
                 externalOrgASAPId +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
