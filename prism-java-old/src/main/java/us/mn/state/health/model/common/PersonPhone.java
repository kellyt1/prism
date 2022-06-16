package us.mn.state.health.model.common;

public class PersonPhone extends ModelMember {
    private Long personPhoneId;
    private Person person;
    private Phone phone;
    private String deviceType;

    public Long getPersonPhoneId() {
        return personPhoneId;
    }

    public void setPersonPhoneId(Long personPhoneId) {
        this.personPhoneId = personPhoneId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
