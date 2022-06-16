package us.mn.state.health.domain.service.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.util.Email;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Group;

public class ReminderCancelationRLIApprovalToRequestorCommand extends EmailCommand {
    private RequestLineItem requestLineItem;

    protected ReminderCancelationRLIApprovalToRequestorCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email) {
        super(userRepository, groupRepository, mailService, email);
    }


    public ReminderCancelationRLIApprovalToRequestorCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email, RequestLineItem requestLineItem) {
        super(userRepository, groupRepository, mailService, email);
        this.requestLineItem = requestLineItem;
    }

    void buildSubject() {
        email.setSubject("Cancellation Notification Email");
    }

    void buildMessage() {
        StringBuilder htmlMessage = new StringBuilder().
                append("<p>The item that you requested " + requestLineItem.getRequest().getTrackingNumber()
                        + " '" + getDescription() + "' was automatically cancelled." +
                        "<br>" +
                        "Reason:   Lack of approval." +
                        "Persons having approval authority for the item:<br>");
        htmlMessage.append("<br>\n").append("<ul>\n");
        for (User user : getEvaluators()) {
            htmlMessage.append("\t<li>").append(user.getFirstAndLastName()).append("</li>\n");
        }
        htmlMessage.append("</ul>");
        htmlMessage.append("<br>" +
                "Please contact your division management if this continues to be a problem." +
                "<br>");
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
