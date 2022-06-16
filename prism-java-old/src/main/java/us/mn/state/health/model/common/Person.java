package us.mn.state.health.model.common;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.HibernatePersonDAO;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

public class Person implements Serializable, Comparable {
    private Long personId;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String insertedBy;
    private Date insertionDate;
    private Date endDate;
    private String salutation;
    private String nameType;
    private Date nameStartDate;
    private String gender;
    private String terminatedBy;
    private Date terminationDate;
    private String namePrefix;
    private String changedBy;
    private Date changeDate;
    private Collection personGroupLinks = new HashSet();
    private String emailAddress;
    private String workPhone;
    private Collection personPositionLinks = new HashSet();
    private Collection personEmailAddressLinks = new HashSet();       //a collection of PersonEmailAddressLink objects
    private Collection personMailingAddresses = new HashSet();
    private Collection personPhones = new HashSet();
    private String ndsUserId;
    private String personType;
     private User user;

    public Person(Long personId,String firstName,String middlename, String lastName){
        this.personId = personId;
        this.firstName = firstName;
        this.middleName = middlename;
        this.lastName = lastName;
    }

    /**
     * No-arg constructor for JavaBean tools.
     */
    public Person() {
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setPersonGroupLinks(Collection personGroupLinks) {
        this.personGroupLinks = personGroupLinks;
    }

    public Collection getPersonGroupLinks() {
        return personGroupLinks;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Date getChangeDate() {
        return changeDate;
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

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public Date getNameStartDate() {
        return nameStartDate;
    }

    public void setNameStartDate(Date nameStartDate) {
        this.nameStartDate = nameStartDate;
    }

    public Collection getPersonPositionLinks() {
        return personPositionLinks;
    }

    public void setPersonPositionLinks(Collection personPositionLinks) {
        this.personPositionLinks = personPositionLinks;
    }


    public void setPersonEmailAddressLinks(Collection personEmailAddressLinks) {
        this.personEmailAddressLinks = personEmailAddressLinks;
    }


    public Collection getPersonEmailAddressLinks() {
        return personEmailAddressLinks;
    }

    @Transient
    public String getFirstAndLastName() {
        return new NameSummary(getFirstName(), getMiddleName(), getLastName()).firstMiddleAndLast();
    }

    @Transient
    public String getLastAndFirstName() {
        return new NameSummary(getFirstName(), getMiddleName(), getLastName()).lastFirstAndMiddle();
    }

    @Transient
    public String getEmailAddressPrimary() {
        String outString = "";
        String sep="";
        for (Object o : personEmailAddressLinks) {
            PersonEmailAddressLink peal = (PersonEmailAddressLink)o;
            EmailAddress emailAddress = peal.getEmailAddress();
            outString = sep + emailAddress.getEmailAddress();
            sep = ";";
        }

        return outString;
    }

    public void setPersonMailingAddresses(Collection personMailingAddresses) {
        this.personMailingAddresses = personMailingAddresses;
    }


    public Collection getPersonMailingAddresses() {
        return personMailingAddresses;
    }

    @Transient
    public Position getPrimaryPosition() {
        PartyResource pr = (PartyResource)CollectionUtils.getObjectFromCollectionById(personPositionLinks, 1, "rank");

        return (pr != null) ? pr.getPosition() : null;
    }

    @Transient
    public Facility getPrimaryFacility() {
        Position position = getPrimaryPosition();

        return (position != null) ? position.getPrimaryFacility() : null;
    }
    
    public void setPrimaryFacility(Facility facility, String username) {
        Position position = getPrimaryPosition();
        if(position != null) {
            CollectionUtils.removeMatchingItem(position.getFacilities(), 1, "rank");
            PositionFacility.create(position, facility, username);
        }
    }

    @Transient
    public MailingAddress getPrimaryMailingAddress() {
        PersonMailingAddress pma = (PersonMailingAddress)CollectionUtils.getObjectFromCollectionById(personMailingAddresses, 1, "rank");

        return (pma != null) ? pma.getMailingAddress() : null;
    }

    @Transient
    public Phone getWorkLandPhone() {
        PersonPhone personPhone = (PersonPhone)CollectionUtils.getObjectFromCollectionById(personPhones, Phone.LAND_PHONE, "deviceType");

        return (personPhone != null) ? personPhone.getPhone() : null;
    }

    public void setPersonPhones(Collection personPhones) {
        this.personPhones = personPhones;
    }

    public Collection getPersonPhones() {
        return personPhones;
    }

    /**
     * @param groupCode
     * @return a boolean value that tells if the person is in the provided 'groupCode'
     */
    public boolean isInGroup(String groupCode){
            HibernatePersonDAO personDAO = new HibernatePersonDAO();
            List<Person> persons;
            try {
                persons = personDAO.findPersonsByGroupCode(groupCode);
                for (Person person : persons) {
                    if (person.getEmployeeId().equalsIgnoreCase(this.getEmployeeId())) return true;
                }
                return false;
            } catch(InfrastructureException inf) {
                return false;
            }
    }

    public User getUser() {
           user = new User();
           user.setFirstName(firstName);
           user.setEmployeeId(employeeId);
           user.setGender(gender);
           user.setLastName(lastName);
           user.setMiddleName(middleName);
           user.setNdsUserId(ndsUserId);
           user.setNameType(nameType);
           user.setPersonGroupLinks(personGroupLinks);
           user.setPersonId(personId);
           user.setUsername(ndsUserId);
           user.setPersonEmailAddressLinks(personEmailAddressLinks);

           return user;
       }

    public String getNdsUserId() {
        return ndsUserId;
    }

    public void setNdsUserId(String ndsUserId) {
        this.ndsUserId = ndsUserId;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", employeeId='" + employeeId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if (!(o instanceof Person)) return false;
        final Person that = (Person)o;

        if(this.getPersonId() == null) {
            if(that.getPersonId() == null) {
                //dig deeper, using comparison by value
                if(this.getFirstAndLastName() != null && !this.getFirstAndLastName().equals(that.getFirstAndLastName())) {
                    return false;
                }
                if(this.getGender() != null && !this.getGender().equals(that.getGender())) {
                    return false;
                }
                if(this.getEmployeeId() != null && !this.getEmployeeId().equals(that.getEmployeeId())) {
                    return false;
                }
                if(this.getInsertionDate() != null && !this.getInsertionDate().equals(that.getInsertionDate())) {
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
            return this.getPersonId().equals(that.getPersonId());
        }
    }

    public int hashCode() {
        int result = 13;
        result = 29 * result + (getInsertionDate() != null ? getInsertionDate().hashCode() : 0);
        result = 29 * result + (getEmployeeId() != null ? getEmployeeId().hashCode() : 0);
        return result;
    }

    public int compareTo(Object o) {
        if(o instanceof Person) {
            return this.getPersonId().compareTo(((Person) o).getPersonId());
        }
        return 0;
    }
}
