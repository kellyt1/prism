package us.mn.state.health.builder.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemLocation;
import us.mn.state.health.model.util.email.EmailBean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class is used for creation of the email notification when a requestor
 * request a new Stock Item
 * The email notifies all the buyers
 */

public class StockItemHitReorderPointEmailBuilder implements EmailBuilder {
    public static Log log = LogFactory.getLog(StockItemHitReorderPointEmailBuilder.class);
    private StockItem stockItem;
    private EmailBean emailBean;

    public static final String subject = "Stock Item hit re-order point";

    public static final String stockItemIcnbr = "<br><br> Stock Item ICNBR: ";
    public static final String itemDescriptionHeader = "<th width='20%'><u><b>Stock Item Description:</b></u></th>";
    public static final String reOrderPointHeader = "<th width='10%'><u><b>Re-Order Point:</b></u></th>";
    public static final String suggestedReOrderQuantityHeader = "<th width='10%'><u><b>Suggested Re-Order Quantity:</b></u></th>";
    public static final String quantityOnHandHeader = "<th width='10%'><u><b>Quantity On-Hand:</b></u></th>";
    public static final String currentDemandHeader = "<th width='10%'><u><b>Current Demand:</b></u></th>";
    public static final String primaryContactHeader = "<th width='10%'><u><b>Primary Contact:</b></u></th>";
    public static final String secondaryContactHeader = "<th width='10%'><u><b>Secondary Contact:</b></u></th>";
    public static final String locationsHeader = "<th width='20%'><u><b>Locations:</b></u></th>";

    public StockItemHitReorderPointEmailBuilder(StockItem stockItem, EmailBean emailBean) {
        this.stockItem = stockItem;
        this.emailBean = emailBean;
    }

    public void buildSubject() {
        emailBean.setSubject(subject + " ICNBR: " + stockItem.getFullIcnbr());
    }

    public void buildMessage() {
        StringBuffer message = new StringBuffer();
        StringBuffer locations = new StringBuffer();
        for (Iterator iterator = stockItem.getLocations().iterator(); iterator.hasNext();) {
            StockItemLocation stockItemLocation = (StockItemLocation) iterator.next();
            String location = stockItemLocation.getFacility().getFacilityCode() + "-" + stockItemLocation.getLocationCode() + "<br/>";
            locations.append(location);
        }
        message.append(stockItemIcnbr).append(stockItem.getFullIcnbr());
        message.append("<br /><br /><table><tr>");
        message.append(itemDescriptionHeader);
        message.append(quantityOnHandHeader);
        message.append(reOrderPointHeader);
        message.append(suggestedReOrderQuantityHeader);
        message.append(currentDemandHeader);
        message.append(primaryContactHeader);
        message.append(secondaryContactHeader);
        message.append(locationsHeader);
        message.append("</tr>");
        message.append("<tr>");
        message.append("<td align='left'>").append(stockItem.getDescription()).append("</td>");
        message.append("<td align='center'>").append(stockItem.getQtyOnHand()).append("</td>");
        message.append("<td align='center'>").append(stockItem.getReorderPoint()).append("</td>");
        message.append("<td align='center'>").append(stockItem.getSuggestedROQ()).append("</td>");
        message.append("<td align='center'>").append(stockItem.getCurrentDemand().intValue()).append("</td>");
        message.append("<td align='center'>").append(stockItem.getPrimaryContact().getFirstAndLastName()).append("</td>");
        message.append("<td align='center'>").append(stockItem.getSecondaryContact().getFirstAndLastName()).append("</td>");
        message.append("<td align='center'>").append(locations).append("</td>");
        message.append("</tr>");
        message.append("</table>");
        message.append("<p>Please verify the physical quantity of this item. If the actual " +
                "quantity on hand does not match the value shown in PRISM, please adjust " +
                "the value in PRISM.  If the stock item's quantity is indeed below the " +
                "Re-order point, please notify the stock item's contacts so they can decide " +
                "whether to re-order it.");
        emailBean.setMessage(message.toString());
    }

    /**
     * Send the email to everyone in the the Stock Controller group.
     */
    public void buildTo() {
        StringBuffer to = new StringBuffer();
//        to.append(Utility.createEmailAddress(stockItem.getPrimaryContact()));
//        to.append(Utility.createEmailAddress(stockItem.getSecondaryContact()));
        try {
            Group stkController;
            if (Arrays.asList(Category.MATERIALS_CHEMICALS, Category.MATERIALS_GASSES).contains(stockItem.getCategory().getCategoryCode())) {
                stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getGroupDAO().findGroupByCode(Group.STOCK_CONTROLLER_CHEMICAL_GASSES_EMAIL_NOTIFICATION_CODE);
            } else {
                stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                        .getGroupDAO()
                        .findGroupByCode(Group.STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE);
            }
            for (Iterator iter = stkController.getPersonGroupLinks().iterator(); iter.hasNext();) {
                PersonGroupLink pgl = (PersonGroupLink) iter.next();
                Person person = pgl.getPerson();
//                to.append(Utility.createEmailAddress(person));
                 to.append(person.getEmailAddressPrimary()).append(","); //.append(EmailBusinessDelegate.EMAIL_DOMAIN).append(",");
                //to.append(person.getNdsUserId()).append(EmailBusinessDelegate.EMAIL_DOMAIN).append(",");
            }
        }
        catch (Exception e) {
            //consume excpetion here. Don't let a problem here
            //crash the system. Its not THAT important. But do log it.
            log.error("Exception in buildTo(): ", e);
        }
        emailBean.setTo(to.toString());
    }

    public void buildFrom() throws InfrastructureException {
        emailBean.setFrom(systemEmailAddress);
    }

    public void buildCc() {
        if (Arrays.asList(Category.MATERIALS_GASSES).contains(stockItem.getCategory().getCategoryCode())) {
            StringBuilder builder = new StringBuilder();
//            builder.append(stockItem.getPrimaryContact().getNdsUserId()).append(EmailBusinessDelegate.EMAIL_DOMAIN).append(",");
//            builder.append(stockItem.getSecondaryContact().getNdsUserId()).append(EmailBusinessDelegate.EMAIL_DOMAIN);
//          builder.append(stockItem.getPrimaryContact().getEmailAddressPrimary()).append(","); //.append(EmailBusinessDelegate.EMAIL_DOMAIN).append(",");
//          builder.append(stockItem.getSecondaryContact().getEmailAddressPrimary()); //.append(EmailBusinessDelegate.EMAIL_DOMAIN);

//PrimaryContact and SecondaryContact are just proxies in some cases and so EmailAddressPrimary attributes are not initialized
//            DAOFactory daoFactory = new HibernateDAOFactory();
//            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
            Person primaryContact = null;
            Person secondaryContact = null;
            try {
            primaryContact = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getPersonDAO().getPersonById(stockItem.getPrimaryContact().getPersonId(),false);
            secondaryContact = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getPersonDAO().getPersonById(stockItem.getSecondaryContact().getPersonId(),false);
            } catch (InfrastructureException e) {
                log.error("Error finding primaryContact and/or secondaryContact by ID");
                e.printStackTrace();
            }
            if (primaryContact != null) {
                builder.append(primaryContact.getEmailAddressPrimary()).append(","); //.append(EmailBusinessDelegate.EMAIL_DOMAIN).append(",");
            }
            if (secondaryContact != null) {
                builder.append(secondaryContact.getEmailAddressPrimary()); //.append(EmailBusinessDelegate.EMAIL_DOMAIN).append(",");
            }
            emailBean.setCc(builder.toString());
        }
    }

    public void buildBcc() {
    }

    private String buildLink() {
        StringBuffer buffer = new StringBuffer();
        StringBuffer address = new StringBuffer();
        try {
            buffer.append("<a href='");
            address.append("http://");
            address.append(InetAddress.getLocalHost().getHostName());
            address.append("/viewReorderStockItems.do");
            buffer.append(address);
            buffer.append("'>");
            buffer.append(address);
            buffer.append("</a>");
        }
        catch (UnknownHostException e) {
            //do nothing! We do not want to let the email stuff crash the system.
            //if it doesn't work, oh well...
            //throw new InfrastructureException(e);
        }
        return buffer.toString();
    }
}
