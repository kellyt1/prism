package us.mn.state.health.model.common;
import us.mn.state.health.common.lang.WrapperUtils;

public class ExtOrgDetailEmailAddress extends ModelMember {
    private Long extOrgDetailEmailAddressId;
    private ExternalOrgDetail extOrgDetail;    
    private EmailAddress emailAddress;    
    private Integer rank;
    
    public static ExtOrgDetailEmailAddress create(ExternalOrgDetail extOrgDetail,
                                                  EmailAddress emailAddress,
                                                  String username) {    
        ExtOrgDetailEmailAddress extOrgDtlEmAddr = new ExtOrgDetailEmailAddress();
        extOrgDtlEmAddr.setExtOrgDetail(extOrgDetail);
        extOrgDtlEmAddr.setEmailAddress(emailAddress);
        Integer rank = WrapperUtils.getNextHighestValue(extOrgDetail.getEmailAddresses(), "rank");
        extOrgDtlEmAddr.buildInsertMetaProperties(username);
        extOrgDtlEmAddr.setRank(rank);
        
        return extOrgDtlEmAddr;
    }


    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }


    public EmailAddress getEmailAddress() {
        return emailAddress;
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


    public void setExtOrgDetailEmailAddressId(Long extOrgDetailEmailAddressId) {
        this.extOrgDetailEmailAddressId = extOrgDetailEmailAddressId;
    }


    public Long getExtOrgDetailEmailAddressId() {
        return extOrgDetailEmailAddressId;
    }
}