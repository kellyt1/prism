package us.mn.state.health.model.common;

import us.mn.state.health.common.exceptions.InfrastructureException

public class NotificationEmailBudgetLink implements Serializable {
    private NotificationEmailBudgetLinkId notificationEmailBudgetLinkId;
    private NotificationEmailAddress notificationEmailAddress;
    private OrgBudget orgBudget;


    // ******************* Begin Inner composite PersonGroupLinkId class ******************* //
    public static class NotificationEmailBudgetLinkId implements Serializable {
        private Long emailId;
        private Long budgetId;

        public NotificationEmailBudgetLinkId() {
        }

        Long getEmailId() {
            return emailId
        }

        Long getBudgetId() {
            return budgetId
        }

        public NotificationEmailBudgetLinkId(Long emaiId, Long budgetId) {
            this.emailId = emaiId;
            this.budgetId = budgetId;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NotificationEmailBudgetLinkId)) return false;

            final NotificationEmailBudgetLinkId personGroupLinkId = (NotificationEmailBudgetLinkId) o;

            if (!budgetId.equals(personGroupLinkId.getBudgetId())) return false;
            if (!emailId.equals(personGroupLinkId.getEmailId())) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = emailId.hashCode();
            result = 29 * result + budgetId.hashCode();
            return result;
        }
    }
    // ******************* End Inner composite PersonGroupLinkId class ******************* //

    /**
     * No-arg constructor for JavaBean tools.
     */
    public NotificationEmailBudgetLink() {
    }

    public static NotificationEmailBudgetLink createNotificationEmailBudgetLink(NotificationEmailAddress notificationEmailAddress, OrgBudget orgBudget) throws InfrastructureException {
        NotificationEmailBudgetLink link = new NotificationEmailBudgetLink();
        link.setNotificationEmailAddress(notificationEmailAddress)
        link.setOrgBudget(orgBudget)
        link.setNotificationEmailBudgetLinkId(new NotificationEmailBudgetLinkId(notificationEmailAddress.id, orgBudget.orgBudgetId))
        notificationEmailAddress.getNotificationEmailBudgetLinks().add(link)
        orgBudget.getNotificationEmailBudgetLinks().add(link)

        return link;
    }

    NotificationEmailAddress getNotificationEmailAddress() {
        return notificationEmailAddress
    }

    void setNotificationEmailAddress(NotificationEmailAddress notificationEmailAddress) {
        this.notificationEmailAddress = notificationEmailAddress
    }

    OrgBudget getOrgBudget() {
        return orgBudget
    }

    void setOrgBudget(OrgBudget orgBudget) {
        this.orgBudget = orgBudget
    }

    NotificationEmailBudgetLinkId getNotificationEmailBudgetLinkId() {
        return notificationEmailBudgetLinkId
    }

    void setNotificationEmailBudgetLinkId(NotificationEmailBudgetLinkId notificationEmailBudgetLinkId) {
        this.notificationEmailBudgetLinkId = notificationEmailBudgetLinkId
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationEmailBudgetLink)) return false;

        final NotificationEmailBudgetLink notificationEmailBudgetLink = (NotificationEmailBudgetLink) o;

        if (!notificationEmailBudgetLinkId.equals(notificationEmailBudgetLink.notificationEmailBudgetLinkId)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = notificationEmailBudgetLinkId.hashCode();
        return result;
    }
}
