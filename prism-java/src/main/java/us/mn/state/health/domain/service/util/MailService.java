package us.mn.state.health.domain.service.util;

import us.mn.state.health.util.Email;

public interface MailService {
    void sendEmail(Email email);
}
