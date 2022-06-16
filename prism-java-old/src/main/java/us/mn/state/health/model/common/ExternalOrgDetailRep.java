package us.mn.state.health.model.common;

import us.mn.state.health.common.lang.WrapperUtils;

public class ExternalOrgDetailRep extends ModelMember {
    private Long externalOrgDetailRepId;
    private ExternalOrgDetail extOrgDetail;
    private Person rep;    
    private Integer rank;
    
    /** Factory Methods */
    public static ExternalOrgDetailRep create(ExternalOrgDetail extOrgDetail, Person person) {
        ExternalOrgDetailRep extOrgRep = new ExternalOrgDetailRep();
        extOrgRep.setExtOrgDetail(extOrgDetail);
        extOrgRep.setRep(person);
        Integer rank = WrapperUtils.getNextHighestValue(extOrgDetail.getReps(), "rank");
        extOrgRep.setRank(rank);
        extOrgDetail.getReps().add(extOrgRep);
        
        return extOrgRep;
    }


    public void setRep(Person rep) {
        this.rep = rep;
    }


    public Person getRep() {
        return rep;
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


    public void setExternalOrgDetailRepId(Long externalOrgDetailRepId) {
        this.externalOrgDetailRepId = externalOrgDetailRepId;
    }


    public Long getExternalOrgDetailRepId() {
        return externalOrgDetailRepId;
    }
}