package us.mn.state.health.view.common;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class PersonForm extends ValidatorForm implements Comparable {
    private String personId;
    private OrganizationForm division;
    private String employeeID;
    private String lastName;
    private String firstName;
    private Collection orgBudgetForms;
    private String namePrefix;
    private String gender;
    private String salutation;
    private String middleName;
    private String challengeQuestion;
    private String challengeAnswer;
    private Boolean forcePasswordChange;
    private String password;
    private String username;
    private Boolean activeIndicator;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {  
        personId = "";
        employeeID = "";
        lastName = "";
        firstName = "";
        namePrefix = "";
        gender = "";
        salutation = "";
        middleName = "";
        challengeQuestion = "";
        challengeAnswer = "";
        password = "";
        username = "";
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }


    public void setPersonId(String personId) {
        this.personId = personId;
    }


    public String getPersonId() {
        return personId;
    }


    public void setDivision(OrganizationForm division) {
        this.division = division;
    }


    public OrganizationForm getDivision() {
        return division;
    }


    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }


    public String getEmployeeID() {
        return employeeID;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setOrgBudgetForms(Collection orgBudgetForms) {
        this.orgBudgetForms = orgBudgetForms;
    }


    public Collection getOrgBudgetForms() {
        return orgBudgetForms;
    }


    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }


    public String getNamePrefix() {
        return namePrefix;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getGender() {
        return gender;
    }


    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }


    public String getSalutation() {
        return salutation;
    }


    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    public String getMiddleName() {
        return middleName;
    }
    
    public void setChallengeQuestion(String challengeQuestion) {
        this.challengeQuestion = challengeQuestion;
    }


    public String getChallengeQuestion() {
        return challengeQuestion;
    }


    public void setChallengeAnswer(String challengeAnswer) {
        this.challengeAnswer = challengeAnswer;
    }


    public String getChallengeAnswer() {
        return challengeAnswer;
    }


    public void setForcePasswordChange(Boolean forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }


    public Boolean getForcePasswordChange() {
        return forcePasswordChange;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }


    public void setActiveIndicator(Boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }


    public Boolean getActiveIndicator() {
        return activeIndicator;
    }
    
    public String getFirstAndLastName() {
        return firstName + " " + lastName;
    }
    
    public int compareTo(Object o) {
        if(o instanceof PersonForm) {
            return getFirstAndLastName().compareTo(((PersonForm)o).getFirstAndLastName());
        }
        return 0;
    }
}