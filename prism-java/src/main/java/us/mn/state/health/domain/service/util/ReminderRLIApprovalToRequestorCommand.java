package us.mn.state.health.domain.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.util.Email;
import org.apache.commons.lang.time.DateUtils;

public class ReminderRLIApprovalToRequestorCommand extends EmailCommand {
    private RequestLineItem requestLineItem;
    private int days;

    protected ReminderRLIApprovalToRequestorCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email) {
        super(userRepository, groupRepository, mailService, email);
    }

    public ReminderRLIApprovalToRequestorCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email, RequestLineItem requestLineItem, int days) {
        super(userRepository, groupRepository, mailService, email);
        this.requestLineItem = requestLineItem;
        this.days = days;
    }

    void buildSubject() {
        email.setSubject("Cancellation Warning Email");
    }

    void buildMessage() {

        StringBuilder htmlMessage = new StringBuilder().
                append("<p>An Item you requested in " + requestLineItem.getRequest().getTrackingNumber()
                        + " '" + getDescription() + "' has not been acted on by an approver." +
                        "If this item is not acted on by "+ DateUtils.addDays(requestLineItem.getRequest().getDateRequested(), days)+" your request will automatically be cancelled." +
                        "<br>" +
                        "Once an item is cancelled you will have to re-enter the entire request for it to be processed." +
                        "<br>" +
                        "Please contact one of the designated approver(s) by the cancellation date to prevent this from happening." +
                        "<br>" +
                        "Persons having approval authority for the item:<br>");
        htmlMessage.append("<br>\n").append("<ul>\n");
        for (User user : getEvaluators()) {
            htmlMessage.append("\t<li>").append(user.getFirstAndLastName()).append("</li>\n");
        }
        htmlMessage.append("</ul>");
        email.setMessage(htmlMessage.toString());

    }

    void buildTo() {
        List<String> result = new ArrayList<String>();
        result.add(buildEmailAddress(requestLineItem.getRequest().getRequestor()));
        email.setTo(result.toArray(new String[]{}));
    }

    void buildCc() {
    }

    private String getDescription() {
        return requestLineItem.getItem() == null ? requestLineItem.getItemDescription() : requestLineItem.getItem().getDescription();
    }

    public List<User> getEvaluators() {
        List<User> evaluators = new ArrayList<User>();
        Collection evaluations = requestLineItem.getRequestEvaluations();
        for (Iterator iterator = evaluations.iterator(); iterator.hasNext();) {
            MaterialsRequestEvaluation materialsRequestEvaluation = (MaterialsRequestEvaluation) iterator.next();
            Group evaluatorGroup = materialsRequestEvaluation.getEvaluatorGroup();
            evaluators.addAll(userRepository.getUsersByGroupCode(evaluatorGroup.getGroupCode()));
        }
        return evaluators;
    }
}
