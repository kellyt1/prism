package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.util.ProxyUtils;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.GrantedAuthority;

public class User extends Person implements Serializable, UserDetails {
    private String challengeQuestion;
    private String challengeAnswer;
    private Boolean changePasswordIndicator;
    private String password;
    private String username;
    private Boolean activeIndicator;
    private Integer loginAttempts;
    private Date userEndDate;
    private String userInsertedBy;
    private Date userInsertionDate;
    private String userChangedBy;
    private Date userChangeDate;
    private boolean accountExpired = false;
    private boolean accountLocked = false;
    private boolean credentialsExpired = false;
    private boolean isEnabled = true;


    public User() {
    }

    public User(Long personId, String firstName, String middleName, String lastName) {
        super(personId, firstName, middleName, lastName);
    }
    /*
    public String toString() {
        return super.toString() +
               "username: " + username + "\n" +
               "challengeQuestion: " + challengeQuestion + "\n" +
               "challengeAnswer: " + challengeAnswer +  "\n" +
               "changePasswordIndicator: " + changePasswordIndicator +  "\n" +
               "activeIndicator: " + activeIndicator +  "\n" +
               "endDate: " + endDate;
    }*/

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!ProxyUtils.validateClassesForEquals(User.class, this, o)) return false;
        User user = (User) o;
        if (!username.equals(user.username)) return false;
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    public String getChallengeQuestion() {
        return this.challengeQuestion;
    }

    public void setChallengeQuestion(String challengeQuestion) {
        this.challengeQuestion = challengeQuestion;
    }

    public String getChallengeAnswer() {
        return this.challengeAnswer;
    }

    public void setChallengeAnswer(String challengeAnswer) {
        this.challengeAnswer = challengeAnswer;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getActiveIndicator() {
        return this.activeIndicator;
    }

    public void setActiveIndicator(Boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    public void setChangePasswordIndicator(Boolean changePasswordIndicator) {
        this.changePasswordIndicator = changePasswordIndicator;
    }

    public Boolean getChangePasswordIndicator() {
        return changePasswordIndicator;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setUserEndDate(Date userEndDate) {
        this.userEndDate = userEndDate;
    }

    public Date getUserEndDate() {
        return userEndDate;
    }

    public GrantedAuthority[] getAuthorities() {
        return new GrantedAuthority[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserInsertedBy() {
        return userInsertedBy;
    }

    public void setUserInsertedBy(String userInsertedBy) {
        this.userInsertedBy = userInsertedBy;
    }

    public Date getUserInsertionDate() {
        return userInsertionDate;
    }

    public void setUserInsertionDate(Date userInsertionDate) {
        this.userInsertionDate = userInsertionDate;
    }

    public String getUserChangedBy() {
        return userChangedBy;
    }

    public void setUserChangedBy(String userChangedBy) {
        this.userChangedBy = userChangedBy;
    }

    public Date getUserChangeDate() {
        return userChangeDate;
    }

    public void setUserChangeDate(Date userChangeDate) {
        this.userChangeDate = userChangeDate;
    }
    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
     */
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
     */
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }


    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
     */
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

}
