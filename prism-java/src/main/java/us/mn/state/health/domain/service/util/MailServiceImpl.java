package us.mn.state.health.domain.service.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import us.mn.state.health.util.Email;

public class MailServiceImpl implements MailService {
    static Log log = LogFactory.getLog(MailServiceImpl.class);
    private JavaMailSenderImpl mailSender;

    public void sendEmail(Email email) {
        try {
            mailSender.send(
                    new MimeMessageBuilder(
                            email.getFrom(),
                            email.getTo(),
                            email.getCc(),
                            email.getBcc(),
                            email.getSubject(),
                            email.getMessage()
                    ).applyMimeEmailTemplate());
        } catch (Exception e) {
            log.error("Error in MailServiceImpl.sendEmail(Email email): ", e);
            log.error("to=" + email.getTo());
            log.error("cc=" + email.getCc());
            log.error("bcc=" + email.getBcc());
        }
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    class MimeMessageBuilder {
        private String from;
        private String[] to;
        private String[] cc;
        private String[] bcc;
        private String subject;
        private String htmlText;


        public MimeMessageBuilder(String from, String[] to, String[] cc, String[] bcc, String subject, String mimeText) {
            this.from = from;
            this.to = to;
            this.cc = cc;
            this.bcc = bcc;
            this.subject = subject;
            this.htmlText = mimeText;
        }

        public MimeMessage applyMimeEmailTemplate() throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            if (!ArrayUtils.isEmpty(to)) {
                helper.setTo(to);
            }
            if (!ArrayUtils.isEmpty(cc)) {
                helper.setCc(cc);
            }
            if (!ArrayUtils.isEmpty(bcc)) {
                helper.setBcc(bcc);
            }
            helper.setSubject(subject);
            helper.setText(htmlText, true);
            return message;
        }
    }
}
