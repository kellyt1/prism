package us.mn.state.health.test;

import java.util.Iterator;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.EmailAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonEmailAddressLink;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.model.util.email.Utility;
import us.mn.state.health.matmgmt.util.Constants;

public class TestEmail extends TestCase {

    private PersonDAO personDAO;
    private String environment;

    protected void setUp() {
        //personDAO = new HibernatePersonDAO();
        //here is where we select the environment
//        environment = Constants.DEVDB;
//        environment = Constants.TEST;
        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);
    }

    protected void tearDown() throws InfrastructureException {
        //HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        //HibernateUtil.closeSession();

        personDAO = null;
    }

    public TestEmail(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestEmail.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestEmail("testSendEmail"));
        suite.addTest(new TestEmail("testGetEmailAddress"));
        */
        //suite.addTest(new TestEmail("testGetEmailAddress"));
        suite.addTest(new TestEmail("testSendEmail"));

        return suite;
    }

    public void testGetEmailAddress() throws Exception {
       Person person = personDAO.getPersonById(new Long(62198), false);
       assertNotNull(person);
       System.out.println(person.getFirstAndLastName());
       Iterator iter = person.getPersonEmailAddressLinks().iterator();
       while(iter.hasNext()) {
           PersonEmailAddressLink link = (PersonEmailAddressLink)iter.next();
           EmailAddress emailAddr = link.getEmailAddress();
           System.out.println(person.getLastAndFirstName() + "'s email address: " + emailAddr.getEmailAddress());
       }
    }

    public void testSendEmail() throws Exception {
       Properties props = new Properties();
        // Setup mail server
       //props.put("mail.smtp.host", "mail.state.mn.us");
       props.put("mail.smtp.host", EmailBusinessDelegate.SMTP_HOST_DEFAULT);
       Session session = Session.getDefaultInstance(props);
       MimeMessage message = new MimeMessage(session);
       message.setContent("<font color='red'><strong>Yeah boyee!  You can write HTML in a system-generated email.  " +
                          "Click <a href='http://www.google.com'>here</a> to go to google.</strong></font>  OK I'll stop sending you emails now", "text/html");
       message.setSubject("Extremely self-important message from GW");
       Address fromAddress = new InternetAddress("prism@health.state.mn.us");
       Address lucian = new InternetAddress("Lucian.Ochian@health.state.mn.us");
       Address shawn = new InternetAddress("Shawn.Flahave@health.state.mn.us");
       message.setFrom(fromAddress);
       message.setReplyTo(new Address[] {fromAddress});
       message.addRecipient(Message.RecipientType.TO, lucian);
       message.addRecipient(Message.RecipientType.TO, shawn);
       Transport.send(message);
    }

    public void testReorderStockItemRecipients(){
        StringBuffer to = new StringBuffer();
//        to.append(Utility.createEmailAddress(stockItem.getPrimaryContact()));
//        to.append(Utility.createEmailAddress(stockItem.getSecondaryContact()));
        try {
            Group stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                                            .getGroupDAO()
                                            .findGroupByCode(Group.STOCK_CONTROLLER_CODE);
            for(Iterator iter = stkController.getPersonGroupLinks().iterator(); iter.hasNext(); ) {
                PersonGroupLink pgl = (PersonGroupLink)iter.next();
                Person person = pgl.getPerson();
                to.append(Utility.createEmailAddress(person));
                System.out.println(person.getEndDate());
            }
            System.out.println("Emails: " + to.toString());
            System.out.println("============ ");
        }
        catch(Exception e) {
            //consume excpetion here. Don't let a problem here
            //crash the system. Its not THAT important. But do log it.
            e.printStackTrace();
        }
    }
}
