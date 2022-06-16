package us.mn.state.health.model.common;

import us.mn.state.health.common.lang.WrapperUtils;

public class ExtOrgDetailMailingAddress extends ModelMember {
    private Long extOrgDetailMailingAddressId;
    private ExternalOrgDetail extOrgDetail;    
    private MailingAddress mailingAddress;    
    private Integer rank;
    
    /** Factory Methods **/    
    public static ExtOrgDetailMailingAddress create(ExternalOrgDetail extOrgDetail,
                                                    MailingAddress mailingAddress,
                                                    String username) {        
        ExtOrgDetailMailingAddress extOrgDtlMlngAddr = new ExtOrgDetailMailingAddress();
        extOrgDtlMlngAddr.setExtOrgDetail(extOrgDetail);
        extOrgDtlMlngAddr.setMailingAddress(mailingAddress);
        Integer rank = WrapperUtils.getNextHighestValue(extOrgDetail.getMailingAddresses(), "rank");
        extOrgDtlMlngAddr.buildInsertMetaProperties(username);
        extOrgDtlMlngAddr.setRank(rank);
        extOrgDetail.getMailingAddresses().add(extOrgDtlMlngAddr);
        return extOrgDtlMlngAddr;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if (!(o instanceof ExtOrgDetailMailingAddress)) return false;

        final ExtOrgDetailMailingAddress that = (ExtOrgDetailMailingAddress)o;

        if(this.getExtOrgDetailMailingAddressId() == null) {
            if(that.getExtOrgDetailMailingAddressId() == null) {
                //dig deeper, using comparison by value
                if(this.getInsertedBy() != null && !this.getInsertedBy().equals(that.getInsertedBy())) {
                    return false;
                }

                if(this.getInsertionDate() != null && !this.getInsertionDate().equals(that.getInsertionDate())) {
                    return false;
                }

                if(this.getExtOrgDetail() != null && !this.getExtOrgDetail().equals(that.getExtOrgDetail())) {
                    return false;
                }

                if(this.getMailingAddress() != null && !this.getMailingAddress().equals(that.getMailingAddress())) {
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
            return this.getExtOrgDetailMailingAddressId().equals(that.getExtOrgDetailMailingAddressId());
        }
    }

    public int hashCode() {
        int result = 13;
        result = (getExtOrgDetail() != null ? getExtOrgDetail().hashCode() : 0);
        result = 29 * result + (getInsertionDate() != null ? getInsertionDate().hashCode() : 0);
        return result;
    }

    public void setMailingAddress(MailingAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }


    public MailingAddress getMailingAddress() {
        return mailingAddress;
    }


    public void setExtOrgDetail(ExternalOrgDetail extOrgDetail) {
        this.extOrgDetail = extOrgDetail;
    }


    public ExternalOrgDetail getExtOrgDetail() {
        return extOrgDetail;
    }


    public void setRank(Integer rank) {
        this.rank = rank;
    }


    public Integer getRank() {
        return rank;
    }


    public void setExtOrgDetailMailingAddressId(Long extOrgDetailMailingAddressId) {
        this.extOrgDetailMailingAddressId = extOrgDetailMailingAddressId;
    }


    public Long getExtOrgDetailMailingAddressId() {
        return extOrgDetailMailingAddressId;
    }


    public String toString() {
        return "ExtOrgDetailMailingAddress{" +
                "rank=" + rank +
                ", extOrgDetailMailingAddressId=" + extOrgDetailMailingAddressId +
                "}";
    }
}