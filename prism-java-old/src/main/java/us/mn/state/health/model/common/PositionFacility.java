package us.mn.state.health.model.common;
import us.mn.state.health.common.lang.WrapperUtils;

public class PositionFacility extends ModelMember {

    private Long positionFacilityId;
    
    private Position position;
    
    private Facility facility;
    
    private Integer rank;
    
    private static final String REL_TYPE_POS_FACIL_ASGN = "POS_FACIL_ASGN";
    
    /* Factory Method */
    public static PositionFacility create(Position position, 
                                          Facility facility,
                                          String username) {
                                              
        PositionFacility posFclty = new PositionFacility();
        posFclty.setPosition(position);
        posFclty.setFacility(facility);
        Integer rank = WrapperUtils.getNextHighestValue(position.getFacilities(), "rank");
        posFclty.setRank(rank);
        position.getFacilities().add(posFclty);
        posFclty.setRelationshipType(REL_TYPE_POS_FACIL_ASGN);
        posFclty.buildInsertMetaProperties(username);
        
        return posFclty;
    }


    public void setPosition(Position position) {
        this.position = position;
    }


    public Position getPosition() {
        return position;
    }


    public void setFacility(Facility facility) {
        this.facility = facility;
    }


    public Facility getFacility() {
        return facility;
    }


    public void setRank(Integer rank) {
        this.rank = rank;
    }


    public Integer getRank() {
        return rank;
    }


    public void setPositionFacilityId(Long positionFacilityId) {
        this.positionFacilityId = positionFacilityId;
    }


    public Long getPositionFacilityId() {
        return positionFacilityId;
    }

}