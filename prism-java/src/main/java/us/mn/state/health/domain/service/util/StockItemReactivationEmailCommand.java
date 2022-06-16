package us.mn.state.health.domain.service.util;

import java.util.ArrayList;
import java.util.List;

import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.util.Email;

public class StockItemReactivationEmailCommand extends EmailCommand {
    private StockItem stockItem;

    protected StockItemReactivationEmailCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email) {
        super(userRepository, groupRepository, mailService, email);
    }


    public StockItemReactivationEmailCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email, StockItem stockItem) {
        super(userRepository, groupRepository, mailService, email);
        this.stockItem = stockItem;
    }

    public void buildSubject() {
        String subject = "The Hold Until Date expired for the stock item " + stockItem.getFullIcnbr() + "";
//        String subject = "The stock item " + stockItem.getFullIcnbr() + " has been reactivated";
        email.setSubject(subject);
    }

    public void buildMessage() {
        String htmlMessage = new StringBuilder().append("<p>The ON-HOLD date has been exceeded " + "for the stock item ").
                append(stockItem.getFullIcnbr()).append(".<br>\n").
                append("The stock item has been returned to the ACTIVE status.<br>\n").
                append("<br>\n").append("<b>Details</b>:</p>\n").append("<ul>\n").
                append("\t<li>Icnbr:").append(stockItem.getFullIcnbr()).append("</li>\n").
                append("\t<li>Description:").append(stockItem.getDescription()).append("</li>\n").
                append("\t<li>Dispense Unit:").append(stockItem.getDispenseUnit().getName()).append("</li>\n").
                append("\t<li>Qty-on-hand:").append(stockItem.getQtyOnHand()).append("</li>\n").
                append("\t<li>Reorder Point:").append(stockItem.getReorderPoint()).append("</li>\n").
                append("\t<li>Current Demand:").append(stockItem.getCurrentDemand()).append("</li>\n").
                append("\t<li>Primary Contact:").append(stockItem.getPrimaryContact().getFirstAndLastName()).append("</li>\n").
                append("\t<li>Secondary Contact:").append(stockItem.getSecondaryContact().getFirstAndLastName()).append("</li>\n").
                append("</ul>").toString();
        email.setMessage(htmlMessage);
    }

    public void buildTo() {
        List<String> result = new ArrayList<String>();
        if (buildEmailAddress(stockItem.getPrimaryContact()) != null) {
            result.add(buildEmailAddress(stockItem.getPrimaryContact()));
        }

        if (buildEmailAddress(stockItem.getSecondaryContact()) != null) {
            result.add(buildEmailAddress(stockItem.getSecondaryContact()));
        }
        email.setTo(result.toArray(new String[]{}));
    }

//    public void buildFrom() {
//        email.setFrom(FROM_EMAIL_ADDRESS);
//    }

    public void buildCc() {
        // Add the stock controllers here
//        String groupCode = Group.STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE;
//        String[] strings = createEmailAddressesForGroup(groupCode);
        email.setCc(new String[]{STOCK_ROOM_EMAIL_ADDRESS});
    }

}
