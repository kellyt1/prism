package us.mn.state.health.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.collections.CollectionUtils;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;

/**
 * This class was built in order to be run on the server and try all the email addresses and see which one is failing
 * Put the libs in $home/lib and the classes in $home/temp
 * java -classpath "/export/home/istm/ochial1/temp:/export/home/istm/ochial1/lib/mail.jar:/export/home/istm/ochial1/lib/commons-collections-3.1.jar:/export/home/istm/ochial1/lib/activation.jar" us.mn.state.health.test.TestEmailAddresses
 */


public class TestEmailAddresses {
    public static void main(String[] args) {
        String recipients = "John.Kennedy@health.state.mn.us,Richard.Kantorowicz@health.state.mn.us,David.Branstrom@health.state.mn.us,David.Branstrom@health.state.mn.us,Joan.Kowalski@health.state.mn.us,Bill.Neujahr@health.state.mn.us,Andy.Offerdahl@health.state.mn.us,Jack.Sweet@health.state.mn.us,Khammone.Vang@health.state.mn.us,Todd.Marty@health.state.mn.us,Amy.Vallery@health.state.mn.us,Margaret.Meyer@health.state.mn.us,Ernesto.Bernal@health.state.mn.us,joseph.pugh@health.state.mn.us,Vince.Birchem@health.state.mn.us,Linda.Traster@health.state.mn.us,Joan.Osell@health.state.mn.us,Brad.Jahnke@health.state.mn.us,Robert.Hansen@health.state.mn.us,John.Mogren@health.state.mn.us,Chuck.Gibbons@health.state.mn.us,Ron.Brown@health.state.mn.us,Ron.Brown@health.state.mn.us,Lucian.Ochian@health.state.mn.us,Ashley.Scharffbillig@health.state.mn.us,Gary.Caldwell@health.state.mn.us,joe.silver@health.state.mn.us,Jerri.DesJarlait@health.state.mn.us,Ward.Bisping@health.state.mn.us,Ward.Bisping@health.state.mn.us,Dawn.Udean@health.state.mn.us,";
//        recipients = "Lucian.Ochian@health.state.mn.us";
        String[] to = recipients.split(",");

        Set to1 = new HashSet();
        CollectionUtils.addAll(to1, to);
        for (Iterator iterator = to1.iterator(); iterator.hasNext();) {
            String rec = (String) iterator.next();
                    sendMessage(rec, "TEST EMAIL - Please delete it! Sorry about the inconvenience!" );
        }



        // Setup mail server
       //props.put("mail.smtp.host", "mail.state.mn.us");

    }

    private static void sendMessage(String to, String _message){
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", EmailBusinessDelegate.SMTP_HOST_DEFAULT);
            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.setContent(_message, "text/html");
            message.setSubject("This is another test email. You can delete it too. Sorry about that!");
            Address fromAddress = new InternetAddress("prism@health.state.mn.us");
            Address lucian = new InternetAddress(to);
            message.setFrom(fromAddress);
            message.setReplyTo(new Address[] {fromAddress});
            message.addRecipient(Message.RecipientType.TO, lucian);
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Error for " + to);
            e.printStackTrace();
        }

    }
}
