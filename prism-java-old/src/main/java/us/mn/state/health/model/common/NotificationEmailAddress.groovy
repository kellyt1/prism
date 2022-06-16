package us.mn.state.health.model.common

/**
 * User: kiminn1
 * Date: 9/16/2016
 * Time: 11:04 AM
 */

class NotificationEmailAddress {

    private Long id
    private String emailAddress

    private Collection notificationEmailBudgetLinks = new HashSet()

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    String getEmailAddress() {
        return emailAddress
    }

    void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress
    }

    Set getNotificationEmailBudgetLinks() {
        return notificationEmailBudgetLinks
    }

    void setNotificationEmailBudgetLinks(Set emailBudgetLinks) {
        this.notificationEmailBudgetLinks = emailBudgetLinks
    }
}
