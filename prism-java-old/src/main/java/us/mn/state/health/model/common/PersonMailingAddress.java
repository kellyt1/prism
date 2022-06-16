package us.mn.state.health.model.common;

import us.mn.state.health.common.lang.WrapperUtils;

public class PersonMailingAddress extends ModelMember {    
    private Long personMailingAddressId;
    private Person person;    
    private MailingAddress mailingAddress;
    private Integer rank;
    
    public static PersonMailingAddress create(Person person, 
                                              MailingAddress mailingAddress,
                                              String username) {        
        PersonMailingAddress persMlngAddr = new PersonMailingAddress();
        persMlngAddr.setPerson(person);
        persMlngAddr.setMailingAddress(mailingAddress);
        Integer rank = WrapperUtils.getNextHighestValue(person.getPersonMailingAddresses(), "rank");
        persMlngAddr.setRank(rank);
        persMlngAddr.buildInsertMetaProperties(username);
        person.getPersonMailingAddresses().add(persMlngAddr);
        
        return persMlngAddr;
    }


    public void setPersonMailingAddressId(Long personMailingAddressId) {
        this.personMailingAddressId = personMailingAddressId;
    }


    public Long getPersonMailingAddressId() {
        return personMailingAddressId;
    }


    public void setPerson(Person person) {
        this.person = person;
    }


    public Person getPerson() {
        return person;
    }


    public void setMailingAddress(MailingAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }


    public MailingAddress getMailingAddress() {
        return mailingAddress;
    }


    public void setRank(Integer rank) {
        this.rank = rank;
    }


    public Integer getRank() {
        return rank;
    }    
}