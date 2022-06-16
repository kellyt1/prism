package us.mn.state.health.model.common;

import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.lang.WrapperUtils;

public class ExtOrgDetailPhone  extends ModelMember {

    Long extOrgPhoneId;

    private ExternalOrgDetail extOrgDetail;

    private Phone phone;

    private Integer rank;

    private String deviceType;

    /** Factory methods **/

    public static ExtOrgDetailPhone create(ExternalOrgDetail externalOrgDetail
                                     , Phone phone
                                     , String user
                                     , String deviceType) throws BusinessException {
        ExtOrgDetailPhone extOrgDetailPhone = new ExtOrgDetailPhone();
        extOrgDetailPhone.setExtOrgDetail(externalOrgDetail);
        extOrgDetailPhone.setPhone(phone);
//        Integer rank = WrapperUtils.getNextHighestValue(externalOrgDetail.getPhones(), "rank");
        Integer rank = null;
        if(deviceType.equals(Phone.LAND_PHONE) || deviceType.equals(Phone.CELLPHONE)){
            rank = WrapperUtils.getNextHighestValue(externalOrgDetail.getPhones(),"rank");
        }
        else {
            if(deviceType.equals(Phone.FAX)){
                rank = WrapperUtils.getNextHighestValue(externalOrgDetail.getFaxes(),"rank");
            }
            else {
                throw new BusinessException("The device type is UNKNOWN; it must be " + Phone.FAX
                        +","+ Phone.LAND_PHONE
                        + " OR" + Phone.CELLPHONE);
            }
        }
        extOrgDetailPhone.buildInsertMetaProperties(user);
        extOrgDetailPhone.setRank(rank);
        extOrgDetailPhone.setDeviceType(deviceType);
        externalOrgDetail.getPhones().add(extOrgDetailPhone);
        return extOrgDetailPhone;
    }

    public Long getExtOrgPhoneId() {
        return extOrgPhoneId;
    }

    public void setExtOrgPhoneId(Long extOrgPhoneId) {
        this.extOrgPhoneId = extOrgPhoneId;
    }

    public ExternalOrgDetail getExtOrgDetail() {
        return extOrgDetail;
    }

    public void setExtOrgDetail(ExternalOrgDetail extOrgDetail) {
        this.extOrgDetail = extOrgDetail;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
