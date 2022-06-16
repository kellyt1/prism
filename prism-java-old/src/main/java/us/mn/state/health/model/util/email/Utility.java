package us.mn.state.health.model.util.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernatePersonDAO;
import us.mn.state.health.dao.NotificationEmailAddressDAO;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;

public class Utility {

    public static String formatNull(String sField) {
        if (sField == null) {
            return "";
        } else {
            sField.trim();
            return sField;
        }
    }

    public static boolean isEmpty(String sVal) {
        return sVal == null || ((sVal.trim().length() == 0) ? true : false);
    }

    /**
     * This method creates a email address String using a Person object
     * @param person
     * @return a String that represents the person's email address
     */
    public static String createEmailAddress(Person person) throws InfrastructureException {
        StringBuffer result = new StringBuffer();
        PersonDAO personDAO = new HibernatePersonDAO();
        if (person != null) {
            Person p = personDAO.getPersonById(person.getPersonId(), false);
            Iterator iter = p.getPersonEmailAddressLinks().iterator();
            while(iter.hasNext()) {
                PersonEmailAddressLink link = (PersonEmailAddressLink) iter.next();
                EmailAddress emailAddr = link.getEmailAddress();
                result.append(emailAddr.getEmailAddress() + ",");
            }
        }
        return result.toString();
    }

    /**
     * This method creates a list of email addresses separated by comma using a list of Person objects
     * @param persons
     * @return a String that represents the email addresses of the persons separated by a comma
     */

    public static String createEmailAddresses(Collection<Person> persons) throws InfrastructureException {
        StringBuffer result = new StringBuffer("");
        HibernatePersonDAO personDAO = new HibernatePersonDAO();
        for (Iterator i = persons.iterator(); i.hasNext();) {
            Person person = (Person) i.next();
            if(person != null){
                Person p = personDAO.getPersonById(person.getPersonId(), false);
                Iterator iter = p.getPersonEmailAddressLinks().iterator();
                while (iter.hasNext()) {
                    PersonEmailAddressLink link = (PersonEmailAddressLink) iter.next();
                    EmailAddress emailAddr = link.getEmailAddress();
                    result.append(emailAddr.getEmailAddress());
                    result.append(",");
                }
            }
        }
        return result.toString();
    }

    //TODO maybe we should move this method to PersonDAO

    /**
     * Method that returns the evaluators of a Requested Line Item
     * @param rli
     * @param personDAO
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public static List getEvaluators(RequestLineItem rli, PersonDAO personDAO) throws InfrastructureException {
        List evaluators = new ArrayList();  // A collection of person objects
        // A collection of String objects representing the  group codes of the evaluators
        Collection evaluatorGroupCodes = new ArrayList();
        Collection requestEvaluations = rli.getRequestEvaluations();
        Iterator requestEvaluationsIterator = requestEvaluations.iterator();

        while(requestEvaluationsIterator.hasNext()){
            RequestEvaluation evaluation = (RequestEvaluation) requestEvaluationsIterator.next();
            evaluatorGroupCodes.add(evaluation.getEvaluatorGroup().getGroupCode());
        }

        Iterator iter = evaluatorGroupCodes.iterator();
        while(iter.hasNext()) {
            String groupCode = (String)iter.next();
            List temp = personDAO.findPersonsByGroupCode(groupCode);
            evaluators.addAll(temp);
        }
        return evaluators;
    }

    public static String createNotificationEmailAddresses(Collection<NotificationEmailAddress> emailAddresses) throws InfrastructureException {
        StringBuilder result = new StringBuilder("");
        for (Iterator i = emailAddresses.iterator(); i.hasNext();) {
            NotificationEmailAddress emailAddress = (NotificationEmailAddress) i.next();
            if(emailAddress != null){
                result.append(emailAddress.getEmailAddress());
                if(i.hasNext()) result.append(",");
            }
        }
        return result.toString();
    }

    /**
     * Used to find and return the email addresses to be notified when particular funding strings are used.
     * @param rli request line item
     * @param notificationEmailAddressDAO access to email entities in db
     * @return list of email address to be notified when a particular funding source is used
     * @throws InfrastructureException
     */
    public static List getNotificationsEmailAddresses(RequestLineItem rli, NotificationEmailAddressDAO notificationEmailAddressDAO) throws InfrastructureException{
        List emailAddresses = new ArrayList();
        List<RequestLineItemFundingSource> fundingSources = (List<RequestLineItemFundingSource>) rli.getFundingSourcesList();

        for(RequestLineItemFundingSource fundingSource : fundingSources){
            emailAddresses.addAll(notificationEmailAddressDAO.findByOrgBudgetId(fundingSource.getOrgBudget().getOrgBudgetId()));
        }

        return emailAddresses;
    }
}