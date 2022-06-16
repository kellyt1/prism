package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

public class PersonEmailAddressLink implements Serializable {
    private PersonEmailAddressLinkId personEmailAddressLinkId;
    private Person person;
    private EmailAddress emailAddress;
    private Date startDate;
    private Date endDate;
    private Integer rank;
    private String insertedBy;
    private Date insertionDate;
    private String terminatedBy;
    private Date terminationDate;
    private Double timeStart;
    private Double timeEnd;
    private Integer dayStart;
    private Integer dayEnd;
    private Integer monthStart;
    private Integer monthEnd;

    // ******************* Begin Inner composite PersonEmailAddressLinkId class ******************* //
    public static class PersonEmailAddressLinkId implements Serializable {
        private Long personId;
        private Long emailAddressId;

        public PersonEmailAddressLinkId() {
        }
        
        public Long getPersonId() {
            return personId;
        }
        
        public Long getEmailAddressId() {
            return emailAddressId;
        }

        public PersonEmailAddressLinkId(Long personId, Long emailAddressId) {
            this.personId = personId;
            this.emailAddressId = emailAddressId;
        }

        public boolean equals(Object o) {
            if(this == o) return true;
            if(!(o instanceof PersonEmailAddressLinkId)) return false;

            final PersonEmailAddressLinkId personEmailAddressLinkId = (PersonEmailAddressLinkId) o;

            if(!emailAddressId.equals(personEmailAddressLinkId.getEmailAddressId())) return false;
            if(!personId.equals(personEmailAddressLinkId.getPersonId())) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = personId.hashCode();
            result = 29 * result + emailAddressId.hashCode();
            return result;
        }
    }
    // ******************* End Inner composite PersonEmailAddressLinkId class ******************* //
    
    /**
     * No-arg constructor for JavaBean tools.
     */
    public PersonEmailAddressLink() {
    }

    public static PersonEmailAddressLink createPersonEmailAddressLink(Person person, EmailAddress emailAddress) {
        PersonEmailAddressLink link = new PersonEmailAddressLink();
        link.setPerson(person);
        link.setEmailAddress(emailAddress);
        link.setPersonEmailAddressLinkId(new PersonEmailAddressLinkId(person.getPersonId(), emailAddress.getContactMechanismId()));
        person.getPersonEmailAddressLinks().add(link);

        return link;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonEmailAddressLink)) return false;

        final PersonEmailAddressLink personEmailAddressLink = (PersonEmailAddressLink) o;

        if (endDate != null ? !endDate.equals(personEmailAddressLink.endDate) : personEmailAddressLink.endDate != null) return false;
        if (insertedBy != null ? !insertedBy.equals(personEmailAddressLink.insertedBy) : personEmailAddressLink.insertedBy != null) return false;
        if (insertionDate != null ? !insertionDate.equals(personEmailAddressLink.insertionDate) : personEmailAddressLink.insertionDate != null) return false;
        if (!personEmailAddressLinkId.equals(personEmailAddressLink.personEmailAddressLinkId)) return false;
        if (rank != null ? !rank.equals(personEmailAddressLink.rank) : personEmailAddressLink.rank != null) return false;
        if (startDate != null ? !startDate.equals(personEmailAddressLink.startDate) : personEmailAddressLink.startDate != null) return false;
        if (terminatedBy != null ? !terminatedBy.equals(personEmailAddressLink.terminatedBy) : personEmailAddressLink.terminatedBy != null) return false;
        if (terminationDate != null ? !terminationDate.equals(personEmailAddressLink.terminationDate) : personEmailAddressLink.terminationDate != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = personEmailAddressLinkId.hashCode();
        result = 29 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 29 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 29 * result + (rank != null ? rank.hashCode() : 0);
        result = 29 * result + (insertedBy != null ? insertedBy.hashCode() : 0);
        result = 29 * result + (insertionDate != null ? insertionDate.hashCode() : 0);
        result = 29 * result + (terminatedBy != null ? terminatedBy.hashCode() : 0);
        result = 29 * result + (terminationDate != null ? terminationDate.hashCode() : 0);
        return result;
    }


    public void setPersonEmailAddressLinkId(PersonEmailAddressLink.PersonEmailAddressLinkId personEmailAddressLinkId) {
        this.personEmailAddressLinkId = personEmailAddressLinkId;
    }


    public PersonEmailAddressLink.PersonEmailAddressLinkId getPersonEmailAddressLinkId() {
        return personEmailAddressLinkId;
    }


    public void setPerson(Person person) {
        this.person = person;
    }


    public Person getPerson() {
        return person;
    }


    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }


    public EmailAddress getEmailAddress() {
        return emailAddress;
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


    public void setRank(Integer rank) {
        this.rank = rank;
    }


    public Integer getRank() {
        return rank;
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


    public void setTimeStart(Double timeStart) {
        this.timeStart = timeStart;
    }


    public Double getTimeStart() {
        return timeStart;
    }


    public void setTimeEnd(Double timeEnd) {
        this.timeEnd = timeEnd;
    }


    public Double getTimeEnd() {
        return timeEnd;
    }


    public void setDayStart(Integer dayStart) {
        this.dayStart = dayStart;
    }


    public Integer getDayStart() {
        return dayStart;
    }


    public void setDayEnd(Integer dayEnd) {
        this.dayEnd = dayEnd;
    }


    public Integer getDayEnd() {
        return dayEnd;
    }


    public void setMonthStart(Integer monthStart) {
        this.monthStart = monthStart;
    }


    public Integer getMonthStart() {
        return monthStart;
    }


    public void setMonthEnd(Integer monthEnd) {
        this.monthEnd = monthEnd;
    }


    public Integer getMonthEnd() {
        return monthEnd;
    }
}
