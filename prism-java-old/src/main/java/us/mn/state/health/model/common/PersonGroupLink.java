package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;

public class PersonGroupLink implements Serializable {
    private PersonGroupLinkId personGroupLinkId;
    private Person person;
    private Group group;
    private Date startDate;
    private Date endDate;
    private String relationshipType;
    private String insertedBy;
    private Date insertionDate;
    private String terminatedBy;
    private Date terminationDate;


    // ******************* Begin Inner composite PersonGroupLinkId class ******************* //
    public static class PersonGroupLinkId implements Serializable {
        private Long personId;
        private Long groupId;

        public PersonGroupLinkId() {
        }
        
        public Long getPersonId() {
            return personId;
        }
        
        public Long getGroupId() {
            return groupId;
        }

        public PersonGroupLinkId(Long personId, Long groupId) {
            this.personId = personId;
            this.groupId = groupId;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PersonGroupLinkId)) return false;

            final PersonGroupLinkId personGroupLinkId = (PersonGroupLinkId) o;

            if (!groupId.equals(personGroupLinkId.getGroupId())) return false;
            if (!personId.equals(personGroupLinkId.getPersonId())) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = personId.hashCode();
            result = 29 * result + groupId.hashCode();
            return result;
        }
    }
    // ******************* End Inner composite PersonGroupLinkId class ******************* //
    
    /**
     * No-arg constructor for JavaBean tools.
     */
    public PersonGroupLink() {
    }

    public static PersonGroupLink createPersonGroupLink(Person person, Group group) throws InfrastructureException {
        PersonGroupLink link = new PersonGroupLink();
        link.setPerson(person);
        link.setGroup(group);
        link.setPersonGroupLinkId(new PersonGroupLinkId(person.getPersonId(), group.getGroupId()));
        person.getPersonGroupLinks().add(link);
        group.getPersonGroupLinks().add(link);

        return link;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setPersonGroupLinkId(PersonGroupLink.PersonGroupLinkId personGroupLinkId) {
        this.personGroupLinkId = personGroupLinkId;
    }

    public PersonGroupLink.PersonGroupLinkId getPersonGroupLinkId() {
        return personGroupLinkId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public Date getEndDate() {
        return endDate;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setTerminatedBy(String terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    public String getTerminatedBy() {
        return terminatedBy;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonGroupLink)) return false;

        final PersonGroupLink personGroupLink = (PersonGroupLink) o;

        if (endDate != null ? !endDate.equals(personGroupLink.endDate) : personGroupLink.endDate != null) return false;
        if (insertedBy != null ? !insertedBy.equals(personGroupLink.insertedBy) : personGroupLink.insertedBy != null) return false;
        if (insertionDate != null ? !insertionDate.equals(personGroupLink.insertionDate) : personGroupLink.insertionDate != null) return false;
        if (!personGroupLinkId.equals(personGroupLink.personGroupLinkId)) return false;
        if (relationshipType != null ? !relationshipType.equals(personGroupLink.relationshipType) : personGroupLink.relationshipType != null) return false;
        if (startDate != null ? !startDate.equals(personGroupLink.startDate) : personGroupLink.startDate != null) return false;
        if (terminatedBy != null ? !terminatedBy.equals(personGroupLink.terminatedBy) : personGroupLink.terminatedBy != null) return false;
        if (terminationDate != null ? !terminationDate.equals(personGroupLink.terminationDate) : personGroupLink.terminationDate != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = personGroupLinkId.hashCode();
        result = 29 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 29 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 29 * result + (relationshipType != null ? relationshipType.hashCode() : 0);
        result = 29 * result + (insertedBy != null ? insertedBy.hashCode() : 0);
        result = 29 * result + (insertionDate != null ? insertionDate.hashCode() : 0);
        result = 29 * result + (terminatedBy != null ? terminatedBy.hashCode() : 0);
        result = 29 * result + (terminationDate != null ? terminationDate.hashCode() : 0);
        return result;
    }
}
