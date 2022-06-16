package us.mn.state.health.facade.materialsrequest.notifications;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO;
import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.domain.repository.common.StatusRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.materialsrequest.RequestRepository;
import us.mn.state.health.domain.service.util.*;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.configuration.ConfigurationItem;
import us.mn.state.health.util.Email;

import java.util.*;

public class NotifyEvaluatorsFacadeImpl implements NotifyEvaluatorsFacade {
    private RequestRepository requestRepository;
    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private MailService mailService;
    private StatusRepository statusRepository;

    private Log log = LogFactory.getLog(NotifyEvaluatorsFacadeImpl.class);

    public void notifyEvaluatorsTheReminder() {
        log.info("Entering notifyEvaluatorsTheReminder");
        ConfigurationItem configurationItem = null;
        try {
            configurationItem = new HibernateConfigurationItemDAO("try").getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.DAYS_TO_WARNING);
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }
        int days_to_warning = new Integer(configurationItem.getValue());
        try {
            configurationItem = new HibernateConfigurationItemDAO("try").getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.DAYS_TO_CANCELATION);
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }
        int days_to_cancelation = new Integer(configurationItem.getValue());
        List<RequestLineItem> rlis = new ArrayList<RequestLineItem>();
        rlis = requestRepository.findRequestLineItemsWaitingForLevelOneApprovalSubmittedBeforeDate(new Date(System.currentTimeMillis() - 1000l * 3600 * 24 * days_to_warning));
        for (RequestLineItem rli : rlis) {
            EmailCommand emailCommandToEvaluator = new ReminderRLIApprovalToEvaluatorCommand(userRepository, groupRepository, mailService, new Email(), rli,days_to_cancelation);
            EmailCommand emailCommandToRequestor = new ReminderRLIApprovalToRequestorCommand(userRepository, groupRepository, mailService, new Email(), rli,days_to_cancelation);
            emailCommandToEvaluator.executeSendEmail();
            emailCommandToRequestor.executeSendEmail();
        }
        log.info("Leaving notifyEvaluatorsTheReminder");
    }

    public void notifyEvaluatorsTheCancelation() {
        log.info("Entering notifyEvaluatorsTheCancelation");
        ConfigurationItem configurationItem = null;
        try {
            configurationItem = new HibernateConfigurationItemDAO("try").getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.DAYS_TO_CANCELATION);
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }
        //set the status to rli and to each evaluation
        int days_to_cancelation = new Integer(configurationItem.getValue());
        List<RequestLineItem> rlis = new ArrayList<RequestLineItem>();
        rlis = requestRepository.findRequestLineItemsWaitingForLevelOneApprovalSubmittedBeforeDate(new Date(System.currentTimeMillis() - 1000l * 3600 * 24 * days_to_cancelation));
        for (RequestLineItem rli : rlis) {
            Status status = statusRepository.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.CANCELED);
            rli.setStatus(status);
            Collection evaluations = rli.getRequestEvaluations();
            for (Iterator iterator = evaluations.iterator(); iterator.hasNext();) {
                MaterialsRequestEvaluation evaluation = (MaterialsRequestEvaluation) iterator.next();
                evaluation.setEvaluationDecision(status);

            }
            requestRepository.makePersistent(rli);
        }

        try {
            for (RequestLineItem rli : rlis) {
                //do we have to change more than the RLI status?
                EmailCommand emailCommandToRequestor = new ReminderCancelationRLIApprovalToRequestorCommand(userRepository, groupRepository, mailService, new Email(), rli);
                emailCommandToRequestor.executeSendEmail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Leaving notifyEvaluatorsTheCancelation");
    }


    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setRequestRepository(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }


    public void setStatusRepository(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }
}
