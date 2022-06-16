package us.mn.state.health.model.util.email;

import java.util.ArrayList;
import java.util.List;

public class EmailBean {
    private String from = null;
    private String to = "";
    private String bcc = "";
    private String cc = "";
    private List attachment = new ArrayList();
    private String subject = null;
    private String message = null;
    private String groupInformaton = null;//TODO fix spelling of groupInformaton ==> groupInformation


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns a comma separated sequence of addresses
     *
     * @return
     */
    public String getTo() {
        return to;
    }

    /**
     * sets "to" with a String which is a comma separated sequence of addresses
     *
     * @param to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Accepts as a parameter a comma separated sequence of addresses
     *
     * @param to
     */
    public void addTo(String to) {
        if (this.to.equals(""))
            this.to = to;
        else
            this.to = this.to + "," + to;
    }

    public String getBcc() {
        return bcc;
    }

    /**
     * Accepts as a parameter a comma separated sequence of addresses
     *
     * @param bcc
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * Accepts as a parameter a comma separated sequence of addresses
     *
     * @param bc
     */
    public void addBc(String bc) {
        if (this.bcc.equals(""))
            this.bcc = bc;
        else
            this.bcc = this.bcc + "," + bc;
    }

    /**
     * Returns a comma separated sequence of addresses
     *
     * @return
     */
    public String getCc() {
        return cc;
    }

    /**
     * Accepts as a parameter a comma separated sequence of addresses
     *
     * @param cc
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * Accepts as a parameter a comma separated sequence of addresses
     *
     * @param cc
     */
    public void addCc(String cc) {
        if (this.cc.equals(""))
            this.cc = cc;
        else
            this.cc = this.cc + "," + cc;
    }

    public List getAttachment() {
        return attachment;
    }

    /**
     * Accepts as a parameter a list of AttachmentBean objects
     *
     * @param attachment
     */
    public void setAttachment(List attachment) {
        this.attachment = attachment;
    }

    public void setAttachment(AttachmentBean attachment) {
        this.attachment.add(attachment);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGroupInformaton() {
        return groupInformaton;
    }

    public void setGroupInformaton(String groupInformaton) {
        this.groupInformaton = groupInformaton;
    }
}
