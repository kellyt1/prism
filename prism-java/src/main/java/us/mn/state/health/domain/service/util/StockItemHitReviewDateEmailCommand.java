package us.mn.state.health.domain.service.util;

import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.util.Email;

public class StockItemHitReviewDateEmailCommand extends EmailCommand {
    private StockItem stockItem;

    protected StockItemHitReviewDateEmailCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email) {
        super(userRepository, groupRepository, mailService, email);
    }

    public StockItemHitReviewDateEmailCommand(UserRepository userRepository, GroupRepository groupRepository, MailService mailService, Email email, StockItem stockItem) {
        super(userRepository, groupRepository, mailService, email);
        this.stockItem = stockItem;
    }

    void buildSubject() {
        String subject = "Please review the stock item " + stockItem.getFullIcnbr() + "";
        email.setSubject(subject);
    }

    void buildMessage() {
        String htmlMessage = new StringBuilder().append("<p>Today is the Review Date for the stock item ").
                append(stockItem.getFullIcnbr()).append(".<br>\n").
                append("<br>\n").append("<b>Details</b>:</p>\n").append("<ul>\n").
                append("\t<li>Icnbr: ").append(stockItem.getFullIcnbr()).append("</li>\n").
                append("\t<li>Description: ").append(stockItem.getDescription()).append("</li>\n").
                append("\t<li>Dispense Unit: ").append(stockItem.getDispenseUnit().getName()).append("</li>\n").
                append("\t<li>Qty-on-hand: ").append(stockItem.getQtyOnHand()).append("</li>\n").
                append("\t<li>Reorder Point: ").append(stockItem.getReorderPoint()).append("</li>\n").
                append("\t<li>Current Demand: ").append(stockItem.getCurrentDemand()).append("</li>\n").
                append("\t<li>Primary Contact: ").append(stockItem.getPrimaryContact().getFirstAndLastName()).append("</li>\n").
                append("\t<li>Secondary Contact: ").append(stockItem.getSecondaryContact().getFirstAndLastName()).append("</li>\n").
                append("\t<li>Review Date: ").append(DateUtils.toString(stockItem.getReviewDate())).append("</li>\n").
                append("</ul>").toString();
        email.setMessage(htmlMessage);
    }

    void buildTo() {
        email.setTo(buildEmailAddress(userRepository.getUsersByGroupCode(Group.STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE)));
    }

    void buildCc() {
    }

}
