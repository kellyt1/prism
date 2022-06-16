package us.mn.state.health.builder.email

import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.dao.NotificationEmailAddressDAO
import us.mn.state.health.dao.PersonDAO
import us.mn.state.health.model.common.Group
import us.mn.state.health.model.common.NotificationEmailAddress
import us.mn.state.health.model.common.Person
import us.mn.state.health.model.materialsrequest.Request
import us.mn.state.health.model.materialsrequest.RequestLineItem
import us.mn.state.health.model.util.email.EmailBean
import us.mn.state.health.model.util.email.Utility
import us.mn.state.health.util.Environment

/**
 * Created by demita1 on 6/20/2016.
 *
 * Builds an EmailBean for the Request.
 */
class RequestEmailBuilder implements EmailBuilder, EmailBuilderBook {

    private PersonDAO personDAO
    private NotificationEmailAddressDAO notificationEmailAddressDAO
    public static final String request_message = "<br><br> A request has been submitted and requires your approval. Click on the link to bring up PRISM.  If you are not taken " +
                                                 "directly to the approval for this item then choose the following menu option --> " +
                                                 "Request Goods & Services --> Evaluate Requests, to show any outstanding requests that are waiting for your approval.";
    public static final String request_message_book = "<br><br> A request for a book has been submitted.";
    public static final String request_message_conference_meeting = "<br><br> A request for a conference/meeting has been submitted and is ready for funding adjustments and Level Two Approval.";
    public static final String link1 = "<br><br>";
    public static final String tracking = "<br><br> Tracking Number #: ";
    public static final String liDescription = "<br><br> Line Item Description: ";
    public static final String requestor = "<br><br> Requestor: ";
    public static final String status = "<br><br> Status: ";
    public static final String orgforapproval = "<br><br> Budget Code: ";

    private Request request
    private String skin

    private EmailBean emailBean

    RequestEmailBuilder(PersonDAO personDAO, NotificationEmailAddressDAO notificationEmailAddressDAO, Request request, String skin) {
        this.personDAO = personDAO
        this.notificationEmailAddressDAO = notificationEmailAddressDAO
        this.request = request
        this.skin = skin
    }

    EmailBean build() {
        emailBean = new EmailBean()
        buildFrom()
        buildTo()
        buildSubject()
        buildMessage()
        buildGroupInformation()
        emailBean
    }

    @Override
    void buildMessage(String groupCode) throws InfrastructureException {

    }

    @Override
    void buildTo(String groupCode) throws InfrastructureException {
        StringBuilder sb = new StringBuilder(Utility.createEmailAddresses(getRecipients()))
        String notificationEmailAddresses = Utility.createNotificationEmailAddresses(getNotificationEmailAddress())
        sb.append(notificationEmailAddresses)
        sb.toString()
    }

    @Override
    void buildSubject() {
        emailBean.setSubject("Materials Request Action, Tracking Number # " + request.getTrackingNumber())
    }

    @Override
    void buildMessage() throws InfrastructureException {
        StringBuilder message = new StringBuilder()
        message.append(request_message)

        message.append(tracking).append(request.getTrackingNumber())
        request.requestLineItems.each { lineItem ->
            RequestLineItem rli = (RequestLineItem) lineItem
            message.append(link1).append(buildLink(rli))
            message.append(liDescription).append(rli.getItem() ? rli.getItem().getDescription() : rli.getItemDescription())
            message.append(status).append(rli.getStatus().getName())
            message.append(orgforapproval).append(rli.getFundingSrcSummary().getOrgBudgetCodes())
            message.append("<hr>")
        }
        message.append(requestor).append(request.getRequestor().getFirstAndLastName())
        emailBean.setMessage(message.toString())
    }

    void buildGroupInformation() throws InfrastructureException {
        request.requestLineItems.each {lineItem ->
            RequestLineItem rli = (RequestLineItem) lineItem
            StringBuilder groupInformation = new StringBuilder(emailBean.groupInformaton?:"")
            rli.getRequestEvaluations().each {
                groupInformation.append("\n\n").append(it.toString())
            }
            emailBean.setGroupInformaton(groupInformation.toString())
        }
    }

    @Override
    void buildTo() throws InfrastructureException {
        StringBuilder sb = new StringBuilder(Utility.createEmailAddresses(getRecipients()))
        String notificationEmailAddresses = Utility.createNotificationEmailAddresses(getNotificationEmailAddress())
        sb.append(notificationEmailAddresses)
        emailBean.setTo(sb.toString())
    }

    @Override
    void buildFrom() throws InfrastructureException {
        emailBean.setFrom(systemEmailAddress)
    }

    @Override
    void buildCc() {
        // unused
    }

    @Override
    void buildBcc() {
        // unused
    }

    /**
     * Retrieves recipienets for the Request
     *
     * Iterates through each line item in the request:
     * If the line item is waiting for approval, and the it's a book, then
     * the book group persons are added to the set.
     * If the line item is waiting for approval, and it's not a book, then
     * the evaluators for that line item are added to the set.
     *
     * @return a set of recipienets for the Request
     */
    Set<Person> getRecipients() {
        Set<Person> evaluators = new HashSet<>()
        Set<RequestLineItem> lineItems = request.requestLineItems

        lineItems.each { lineItem ->
            if (lineItem.waitingForApproval) {
                if (lineItem.book) {
                    evaluators.addAll(getBookGroupPersons())
                }
                evaluators.addAll(getEvaluatorsForLineItem(lineItem))
            }
        }
        evaluators
    }

    List<Person> getBookGroupPersons() {
        personDAO.findPersonsByGroupCode(Group.BOOK_GROUP_CODE)
    }

    List<Person> getEvaluatorsForLineItem(RequestLineItem lineItem) {
        Utility.getEvaluators(lineItem, personDAO)
    }

    Set<NotificationEmailAddress> getNotificationEmailAddress(){
        Set<NotificationEmailAddress> emailAddresses = new HashSet<>()
        Set<RequestLineItem> lineItems = request.requestLineItems

        lineItems.each {lineItem ->
            emailAddresses.addAll(getNotificationEmailAddressesForFundingSources(lineItem))
        }
        emailAddresses
    }

     List<NotificationEmailAddress> getNotificationEmailAddressesForFundingSources(RequestLineItem rli){
        Utility.getNotificationsEmailAddresses(rli, notificationEmailAddressDAO)
    }

    private String buildLink(RequestLineItem rli) throws InfrastructureException {
        StringBuilder buffer = new StringBuilder();
        StringBuilder address = new StringBuilder();
        try {
            buffer.append("<a href='");

            // Due to the configuration of the machine,   we have had to hardcode in the path. JMP 11/23/2006
            if (Environment.isProduction()) {
                address.append("https://");
            } else address.append("https://");


            if (Environment.isProduction()) {   //is Production
                address.append(Environment.PROD_PRISM_URL);
            } else if (Environment.isTest()) {   //is Test
                address.append(Environment.NONPROD_PRISM_URL);
            } else {  // localhost??
                address.append(Environment.LOCALHOST_PRISM_URL);
            }

            address.append("/viewApprovalStatus")
                    .append("?requestLineItemId=")
                    .append(rli.getRequestLineItemId())
                    .append("&trackingNumber=")
                    .append(rli.getRequest().getTrackingNumber());

            if (skin) {
                address.append("&skin=").append(skin);
            }

            buffer.append(address)
                    .append("'>")
                    .append(address)
                    .append("</a>")
        }
        catch (Exception ignore) {
            //do nothing! Don't let email stuff crash the system. if it doesn't work, oh well...
        }
        return buffer.toString();
    }
}
