package us.mn.state.health.domain.service.util;

import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.common.User;
import us.mn.state.health.util.Email;
import us.mn.state.health.util.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class EmailCommand {
    //todo move these constants to a place where they can be globally shared
    final String EMAIL_DOMAIN_SUFFIX = "@state.mn.us";
    final String FROM_EMAIL_ADDRESS = "prism@state.mn.us";
    final String ISTMAPPDEV_EMAILS = "HEALTH.ISTMAPPDEV_EMAILS";
    final String BCC_EMAIL_ADDRESS = ISTMAPPDEV_EMAILS + "@ead.state.mn.us"; //EMAIL_DOMAIN_SUFFIX;

    protected UserRepository userRepository;
    protected GroupRepository groupRepository;
    private MailService mailService;
    protected Email email;
    public static final String STOCK_ROOM_EMAIL_ADDRESS = "stockrm@state.mn.us";


    protected EmailCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.mailService = mailService;
        this.email = email;
    }

    abstract void buildSubject();

    abstract void buildMessage();

    abstract void buildTo();

    void buildFrom() {
        email.setFrom(FROM_EMAIL_ADDRESS);
    }

    abstract void buildCc();

/* Sub-classes that override buildBcc() should do the following
        remove the final (only added so I could check that it was not being overridden already
        and may need to be updated during this refactoring) TR.

        super.buildBcc();
        email.setBcc(addEmails(email.getBcc(), "new email address"));
           OR
        email.setBcc(addEmails(email.getBcc(), List of email address strings));   */
    final void buildBcc() {
        email.setBcc(new String[]{BCC_EMAIL_ADDRESS});
    }

    protected String buildEmailAddress(Person person) {
        User user = userRepository.getUserById(person.getPersonId());
        //return user != null ? user.getNdsUserId() + EMAIL_DOMAIN_SUFFIX : null;
        return user != null ? user.getEmailAddressPrimary() : null;

    }

    protected String[] buildEmailAddress(Collection<User> users) {
        List<String> emailAddresses = new ArrayList<String>();
        for (User user : users) {
//            emailAddresses.add(user.getNdsUserId() + EMAIL_DOMAIN_SUFFIX);
            emailAddresses.add(user.getEmailAddressPrimary());
        }
        return emailAddresses.toArray(new String[emailAddresses.size()]);
    }

    protected String[] buildEmailAddress2(List<String> emailStrings) {
        List<String> emailAddresses = new ArrayList<String>();
        for (String email : emailStrings) {
            emailAddresses.add(email);
        }
        return emailAddresses.toArray(new String[emailAddresses.size()]);
    }


    protected String[] addEmails(String[] existingEmails, List<String> emailsToAdd) {
        List<String>  list = new ArrayList(Arrays.asList(existingEmails));
        for (int i = 0; i < emailsToAdd.size(); i++) {
            list.add(emailsToAdd.get(i));
        }
        return list.toArray(new String[list.size()]);
    }

    protected String[] addEmails(String[] existingEmails, String emailToAdd) {
        List<String>  list = new ArrayList(Arrays.asList(existingEmails));
        list.add(emailToAdd);
        return list.toArray(new String[list.size()]);
    }

    public void executeSendEmail() {
        buildEmail();
        if (Environment.isNotProduction() && Environment.isTest()) {
             email.setTo(createEmailAddressesForGroup(Group.PRISM_DEVELOPERS));
             email.setCc(createEmailAddressesForGroup(Group.PRISM_DEVELOPERS));
             email.setBcc(addEmails(createEmailAddressesForGroup(Group.PRISM_DEVELOPERS),BCC_EMAIL_ADDRESS));
         }
         if (Environment.isNotProduction()) {
             email.setMessage(Environment.getHostName() + " This is a test message-Please ignore and delete it: " + email.getMessage());
             email.setSubject(Environment.getHostName() + " This is a test message-Please ignore and delete it: " + email.getSubject());
         }

        mailService.sendEmail(email);
    }

    private void buildEmail() {
        buildFrom();
        buildTo();
        buildCc();
        buildBcc();
        buildSubject();
        buildMessage();
    }

    protected String[] createEmailAddressesForGroup(String groupCode) {
        List<String> result = new ArrayList<String>();
        for (Object o : groupRepository.findGroupByCode(groupCode).getPersonGroupLinks()) {
            PersonGroupLink personGroupLink = (PersonGroupLink) o;
            result.add(buildEmailAddress(personGroupLink.getPerson()));
        }
        return result.toArray(new String[]{});
    }

}
