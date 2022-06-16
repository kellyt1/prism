package us.mn.state.health.domain.service.util;

import org.apache.commons.lang.time.DateUtils;
import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.util.Email;

import java.util.*;

public class ReminderRLIApprovalToEvaluatorCommand extends EmailCommand {
    private RequestLineItem requestLineItem;
    private int days;

    protected ReminderRLIApprovalToEvaluatorCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email) {
        super(userRepository, groupRepository, mailService, email);
    }


    public ReminderRLIApprovalToEvaluatorCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email, RequestLineItem requestLineItem, int days) {
        super(userRepository, groupRepository, mailService, email);
        this.requestLineItem = requestLineItem;
        this.days = days;
    }

    void buildSubject() {
        email.setSubject("Cancellation Warning Email");
    }

    void buildMessage() {
        StringBuilder htmlMessage = new StringBuilder().
                append("<p>This is a reminder that an item that was requested for your approval " + requestLineItem.getRequest().getTrackingNumber()
                        + getEvalUrl(requestLineItem.getRequest().getRequestId(), getDescription()) + " has not been acted on." +
                        "If this item is not acted on by "+ DateUtils.addDays(requestLineItem.getRequest().getDateRequested(), days)+" the request will automatically be cancelled." +
                        "<br>" +
                        "Please contact the MN.iT service desk at <a href='https://fyi.web.health.state.mn.us/open/helpdesk/'>https://fyi.web.health.state.mn.us/open/helpdesk/</a>" +
                        "   If you did not receive an email requesting that you approve this item or if you are not aware of the procedure to approve an item." +
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

    static String getEvalUrl(Long requestId, String linkText) {
        return "<a href=\"https://prism.web.health.state.mn.us/viewEvaluateMaterialsRequest.do?requestId=" + requestId + "&skin=reset\">" + linkText + "</a>";
    }

    /**
     * PRIS-118
     * Business wants evaluators, who have not responded, to be added to the mailing list
     * Evaluator is added to the mailing list if evaluation status is WAITING_FOR_APPROVAL (WFA)
     * If evaluation doesn't have an evaluator, then the evaluator group is added to the mailing list
     */
    void buildTo() {
        Set<User> evaluators = new HashSet<>();
        Collection<MaterialsRequestEvaluation> evaluations = requestLineItem.getRequestEvaluations();

        for (MaterialsRequestEvaluation evaluation : evaluations) {
            if (evaluation.isWaitingForApproval()) {
                if (evaluation.getEvaluator() != null) {
                    User evaluator = evaluation.getEvaluator().getUser();
                    evaluators.add(evaluator);
                } else {
                    Group evaluatorGroup = evaluation.getEvaluatorGroup();
                    evaluators.addAll(userRepository.getUsersByGroupCode(evaluatorGroup.getGroupCode()));
                }
            }
        }

        email.setTo(buildEmailAddress(evaluators));
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
