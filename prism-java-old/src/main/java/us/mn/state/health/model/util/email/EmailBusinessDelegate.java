package us.mn.state.health.model.util.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.email.ResourceException;
import us.mn.state.health.common.exceptions.email.ServiceLocatorException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.common.User;
import us.mn.state.health.util.Environment;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class EmailBusinessDelegate {
    private static Log log = LogFactory.getLog(EmailBusinessDelegate.class);
    public static final String MAIL_SESSION_DEFAULT = "mail/MailSession";
//    public static final String SMTP_HOST_DEFAULT = "mdh-gw1.health.state.mn.us";
    public static final String SMTP_HOST_DEFAULT = "localhost";
    public static final String EMAIL_DOMAIN = "@health.state.mn.us";

    private Session session;

    public void sendEmail(EmailBean emailBean) {
        String from = emailBean.getFrom();
        String to = emailBean.getTo();
//        String bcc = emailBean.getBcc();
        String cc = emailBean.getCc();
        String bcc = emailBean.getBcc() + ",HEALTH.ISTMAPPDEV_EMAILS@ead.state.mn.us";// + EMAIL_DOMAIN;
        emailBean.setBcc(bcc);

        String subject = emailBean.getSubject();
        String msg = emailBean.getMessage();
            if (!Constants.ENVIRONMENT.contains(Environment.PROD_SERVER_NAME)) {
                String oldTo = "<br>ORIG To: " + emailBean.getTo() + "<br>ORIG Cc: " + emailBean.getCc() + "<br>ORIG Bcc: " + emailBean.getBcc() + "<br>";
                to =  buildDevelopersAddress();
                cc =  buildDevelopersAddress();
                bcc = buildDevelopersAddress() + ",HEALTH.ISTMAPPDEV_EMAILS@ead.state.mn.us";// + EMAIL_DOMAIN;
                subject = "This is a test message-Please ignore and delete it: " + emailBean.getSubject();
                msg = "<br>=======================================<br>" +
                      "This is a test message-Please ignore and delete it: " +
                      "<br>=======================================<br>" +
                      oldTo.toString() +
                      "<br>=======================================<br>" +
                      emailBean.getMessage();
            }
        List attachment = emailBean.getAttachment();


        if (from == null || from.equals("")) {
            // Change this line if you need to change the from address
            // or you can add the from address to the mail object.
            from = "prism" + EMAIL_DOMAIN;
        }
        Utility.formatNull(subject);
        Utility.formatNull(msg);

        //AttachmentReader ar = new AttachmentReader();
        try {
            // Prepare our mail message
            Message message = new MimeMessage(session);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(from));
            //parse and grab addressses
            InternetAddress destsTo[] = InternetAddress.parse(to);
            InternetAddress destsCc[] = InternetAddress.parse(cc);
            InternetAddress destsBcc[] = InternetAddress.parse(bcc);

            message.setRecipients(Message.RecipientType.TO, destsTo);
            message.setRecipients(Message.RecipientType.BCC, destsBcc);
            message.setRecipients(Message.RecipientType.CC, destsCc);

            //create text of message part
            BodyPart messageBodyPart = new MimeBodyPart();
            //messageBodyPart.setText(msg);
            messageBodyPart.setContent(msg, "text/html");

            //add text part to the email
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            //add attachment to eamil
            if (attachment != null) {
                for (int i = 0; i < attachment.size(); i++) {
                    AttachmentBean att = (AttachmentBean) attachment.get(i);
                    String fileExtension = att.getFileExtension();
                    String type = att.getContentType(fileExtension);
                    DataSource source = new ByteArrayDataSource(att.getByteBlob(), type);

                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(att.getFileTitle());
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            // Put parts in message
            message.setContent(multipart);

            // Send our mail message
            try {
                Transport.send(message);

                // Report success
                log.debug("MailItem Successfully Sent!!");
            }
            catch (SendFailedException sendFailedException) {
                log.debug("MailItem Send Failed!!");
                String emailAddresses = "There was a problem with a personal or group email address used by PRISM.\n";
                int i;
                Address[] addresses;

                //Copy invalid addresses to email body
                emailAddresses += "\nThe following email addresses were invalid in the original failed email:\n";
                if (sendFailedException.getInvalidAddresses() != null) {
                    addresses  = sendFailedException.getInvalidAddresses();
                    for (i = 0; i < addresses.length; i++) {
                        emailAddresses += addresses[i].toString() + "\n";
                    }
                }

                //Copy valid sent addresses to email body
            //    emailAddresses += "\n\nThe following email addresses are valid, and were sent in the original failed email:\n";
                emailAddresses += "\n\nThe following email addresses are valid and the original email has been sent to them.\n";
                if (sendFailedException.getValidSentAddresses() != null) {
                    addresses = sendFailedException.getValidSentAddresses();
                    for (i = 0; i < addresses.length; i++) {
                        emailAddresses += addresses[i].toString() + "\n";
                    }
                }
                //Copy valid unsent addresses to email body
            //    emailAddresses += "\n\nThe following email addresses are valid and the original email has been sent to them.\n";
                if (sendFailedException.getValidUnsentAddresses() != null) {
                    addresses = sendFailedException.getValidUnsentAddresses();
                    for (i = 0; i < addresses.length; i++) {
                       emailAddresses += addresses[i].toString() + "\n";
                    }
                }
                emailAddresses += (!(emailBean.getGroupInformaton() == null)?"\n\n" + emailBean.getGroupInformaton():"");


                emailAddresses += "\n\n\n\n***** Original Message********************";
                //Save orginal destination addresses before they are fixed and resent
                //Copy Original TO addresses to email body to be sent to developers to fix
                emailAddresses += "\nOriginal TO:\n";
                for (i = 0; i < destsTo.length; i++) {
                    emailAddresses += destsTo[i].toString() + "\n";
                }

                //Copy Original cc addresses to email body to be sent to developers to fix
                emailAddresses += "\nOriginal CC:\n";
                for (i = 0; i < destsCc.length; i++) {
                    emailAddresses += destsCc[i].toString() + "\n";
                }

                //Copy Original bcc addresses to email body to be sent to developers to fix
                emailAddresses += "\nOriginal BCC:\n";
                for (i = 0; i < destsBcc.length; i++) {
                    emailAddresses += destsBcc[i].toString() + "\n";
                }

                emailAddresses += ("\nOriginal Subject: " + subject + "\nOriginal Message:\n" + msg);

                //Fix up destination addresses by selecting only the good ones that were not sent
                //if destination or InvalidAddresses are null, then there are no bad addresses to remove
                if (destsTo != null && sendFailedException.getValidUnsentAddresses() != null) {
                    destsTo = selectGoodEmailAddresses(destsTo, sendFailedException.getValidUnsentAddresses());
                }
                if (destsTo != null && sendFailedException.getValidUnsentAddresses() != null) {
                    destsCc = selectGoodEmailAddresses(destsCc, sendFailedException.getValidUnsentAddresses());
                }
                if (destsTo != null && sendFailedException.getValidUnsentAddresses() != null) {
                    destsBcc = selectGoodEmailAddresses(destsBcc, sendFailedException.getValidUnsentAddresses());
                }

                try {
                    //resend original message to verified good addresses
                    message.setRecipients(Message.RecipientType.TO, destsTo);
                    message.setRecipients(Message.RecipientType.BCC, destsBcc);
                    message.setRecipients(Message.RecipientType.CC, destsCc);
                    if (destsTo.length > 0 || destsCc.length > 0 ||  destsBcc.length > 0) {
                        Transport.send(message);
                    }
                }
                catch (SendFailedException sendFailedException2) {
//                    emailAddresses += "\n\n\n\n***** Email Resend FAILED ******************\n No email has been sent.";
                    emailAddresses = "***** Email Resend FAILED ******************\n No email has been sent to anyone besides the Developer Group.\n\n" + emailAddresses;
                }
                finally {
                    //Now compose and send email to Developers so that bad email addresses can be fixed   
                    message.setText(emailAddresses);
                    message.setFrom(new InternetAddress("PRISM_RECIPIENT_GROUP_FAILURE"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(buildDevelopersAddress()));
                    message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(""));
                    message.setRecipients(Message.RecipientType.CC,  InternetAddress.parse(""));
                    Transport.send(message);
                }
            }
        }
        catch (Throwable t) {
            log.error("\nencountered exception: " + t + "\n" + subject + "\n" + msg);

            //throw new AppException(t);
        }
    }

    /**
     * Returns valid email addresses from the list of destination email addresses
     * <p>
     * @param destAddresses  orginal destination email addresses (to, cc or bcc)
     * @param validAddresses  valid addresses returned by sendFailedException
     * @return list of valid email addresses
     */
    private InternetAddress[] selectGoodEmailAddresses(InternetAddress[] destAddresses, Address[] validAddresses) {
        //List<InternetAddress> test = new ArrayList<InternetAddress>();
        //InternetAddress[] addresses = test.toArray(new InternetAddress[]{});
        List<InternetAddress> goodAddresses = new ArrayList<InternetAddress>();
        int i;
        int j;
        int k=0;
        Boolean isGoodAddress;

        for (i = 0; i < destAddresses.length; i++) {
            isGoodAddress = false;
            for (j = 0; j < validAddresses.length; j++) {
                if (destAddresses[i].toString().equals(validAddresses[j].toString())) {
                    isGoodAddress = true;
                    break;
                }
            }
            if (isGoodAddress) {
                goodAddresses.add(destAddresses[i]);
            }
        }
        return goodAddresses.toArray(new InternetAddress[]{});
    }


    private String buildDevelopersAddress() {
        String address = "";
        try {
            for (Object o : DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getGroupDAO().findGroupByCode(Group.PRISM_DEVELOPERS).getPersonGroupLinks()) {
                PersonGroupLink personGroupLink = (PersonGroupLink) o;
                address += buildEmailAddress(personGroupLink.getPerson()) + ",";
            }
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void sendEmails(Collection emailBeans) {
        for (Iterator i = emailBeans.iterator(); i.hasNext();) {
            sendEmail((EmailBean) i.next());
        }
    }

    public EmailBusinessDelegate() throws ResourceException {
        try {
            Session s = (Session) EmailServiceLocator.getInstance().getMailSessionWithOutJNDI(SMTP_HOST_DEFAULT);
            session = s;
        }
        catch (ServiceLocatorException ex) {
            // Translate Service Locator exception into
            // application exception
            throw new ResourceException("MailBusinessDelegate object can not be created");
        }
    }

    public EmailBusinessDelegate(String jndiName) throws ResourceException {
        try {
            Session s = (Session) EmailServiceLocator.getInstance().getMailSession(jndiName);
            session = s;
        }
        catch (ServiceLocatorException ex) {
            // Translate Service Locator exception into
            // application exception
            throw new ResourceException("MailBusinessDelegate object can not be created");
        }
    }

    String buildEmailAddress(Person person) throws InfrastructureException {
        User user = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getUserDAO().getUserById(person.getPersonId(), false);
        //return user != null ? user.getNdsUserId() + "@health.state.mn.us" : null;
        return user != null ? user.getEmailAddressPrimary()  : null;
    }
}