package us.mn.state.health.model.util;

import us.mn.state.health.model.common.Person;

public class Notification  {
    String recipientRole
    Collection recipients = new HashSet()
    String sender
    String subject
    String message
    
    public Notification() {
    }
    
    public void send() {
    }

    public void addRecipient(Person recipient) {
        this.recipients.add(recipient);
    }
}