package us.mn.state.health.util.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.util.Notification;
import us.mn.state.health.util.NotificationException;

public abstract class NotificationAwareAction extends MappingDispatchAction {
    protected void printErrors(NotificationException e, HttpServletRequest request) {
        for (Notification.Error error : e.getNotification().getErrors()) {
            ActionMessages errors1 = getErrors(request);
            errors1.add(error.getProperty(), new ActionMessage("errors.custom", error.getMessage()));
            saveErrors(request, errors1);
        }
    }
}
