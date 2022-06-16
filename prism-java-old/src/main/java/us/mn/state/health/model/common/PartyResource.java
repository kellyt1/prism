package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

public class PartyResource implements Serializable {
    private PartyResourceId partyResourceId = new PartyResourceId();
    private Date startDate;
    private Date endDate;
    private String insertedBy;
    private String terminatedBy;
    private Date terminationDate;
    private Position position;
    private Person person;
    private Integer rank;

    public static class PartyResourceId implements Serializable {

        private Long partyId;
        private Long resourceId;
        private Date insertionDate;
        private String relationshipType;

        public PartyResourceId() {
        }

        public PartyResourceId(Long partyId, Long resourceId, Date insertionDate, String relationshipType) {
            this.partyId = partyId;
            this.resourceId = resourceId;
            this.insertionDate = insertionDate;
            this.relationshipType = relationshipType;
        }

        public Long getPartyId() {
            return partyId;
        }

        public void setPartyId(Long partyId) {
            this.partyId = partyId;
        }

        public Long getResourceId() {
            return resourceId;
        }

        public void setResourceId(Long resourceId) {
            this.resourceId = resourceId;
        }

        public Date getInsertionDate() {
            return insertionDate;
        }

        public void setInsertionDate(Date insertionDate) {
            this.insertionDate = insertionDate;
        }

        public String getRelationshipType() {
            return relationshipType;
        }

        public void setRelationshipType(String relationshipType) {
            this.relationshipType = relationshipType;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PartyResourceId)) return false;

            final PartyResourceId partyResourceId = (PartyResourceId) o;

            if (!insertionDate.equals(partyResourceId.insertionDate)) return false;
            if (!partyId.equals(partyResourceId.partyId)) return false;
            if (!relationshipType.equals(partyResourceId.relationshipType)) return false;
            if (!resourceId.equals(partyResourceId.resourceId)) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = partyId.hashCode();
            result = 29 * result + resourceId.hashCode();
            result = 29 * result + insertionDate.hashCode();
            result = 29 * result + relationshipType.hashCode();
            return result;
        }
    }

    public PartyResourceId getPartyResourceId() {
        return partyResourceId;
    }

    public void setPartyResourceId(PartyResourceId partyResourceId) {
        this.partyResourceId = partyResourceId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getTerminatedBy() {
        return terminatedBy;
    }

    public void setTerminatedBy(String terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartyResource)) return false;

        final PartyResource partyResource = (PartyResource) o;

        if (endDate != null ? !endDate.equals(partyResource.endDate) : partyResource.endDate != null) return false;
        if (insertedBy != null ? !insertedBy.equals(partyResource.insertedBy) : partyResource.insertedBy != null)
            return false;
        if (!partyResourceId.equals(partyResource.partyResourceId)) return false;
        if (person != null ? !person.equals(partyResource.person) : partyResource.person != null) return false;
        if (position != null ? !position.equals(partyResource.position) : partyResource.position != null) return false;
        if (startDate != null ? !startDate.equals(partyResource.startDate) : partyResource.startDate != null)
            return false;
        if (terminationDate != null ? !terminationDate.equals(partyResource.terminationDate) : partyResource.terminationDate != null)
            return false;
        if (terminatedBy != null ? !terminatedBy.equals(partyResource.terminatedBy) : partyResource.terminatedBy != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = partyResourceId.hashCode();
        result = 29 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 29 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 29 * result + (insertedBy != null ? insertedBy.hashCode() : 0);
        result = 29 * result + (terminatedBy != null ? terminatedBy.hashCode() : 0);
        result = 29 * result + (terminationDate != null ? terminationDate.hashCode() : 0);
//        result = 29 * result + (position != null ? position.hashCode() : 0);
//        result = 29 * result + (person != null ? person.hashCode() : 0);
        return result;
    }


    public void setRank(Integer rank) {
        this.rank = rank;
    }


    public Integer getRank() {
        return rank;
    }
}
