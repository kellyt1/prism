package us.mn.state.health.model.materialsrequest;


import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;

import javax.persistence.Transient;

/**
 * This class represents the delivery information specified for a Request.
 * Even though ExternalOrgDetail and Facility both have an association with MailingAddress,
 * this class serves as an encapsulation.  It simplifies things because the relationship those entities
 * have with MailingAddress is one-to-many.
 * the mailingAddress property should never be null, regardless of the existense of either organization
 * or mailingAddress.
 *
 * @author Shawn Flahave
 */
public class DeliveryDetail implements Serializable {
    private Long deliveryDetailId;
    private ExternalOrgDetail organization;
    private String extOrgContactName;
    private Person recipient;
    private MailingAddress mailingAddress;
    private Facility facility;

    @Transient
    public String getLongSummary() {
        StringBuilder buffer = new StringBuilder();
        if (organization) {
            buffer.append(organization.getOrgName()).append("\n");
        } else {
            if (recipient?.getEmployeeId()) {
                buffer.append("MDH").append("\n");
            }
        }
        String recipientName = getRecipientName();
        if (recipientName) {
            buffer.append(recipientName).append("\n");
        }

        if (facility) {
            buffer.append(facility.getFacilityName()).append("\n");
        }
        if (mailingAddress) {
            buffer.append(mailingAddress.getShortSummary());
        }
        return buffer.toString();
    }

    public void setDeliveryDetailId(Long deliveryDetailId) {
        this.deliveryDetailId = deliveryDetailId;
    }

    public Long getDeliveryDetailId() {
        return deliveryDetailId;
    }

    public void setOrganization(ExternalOrgDetail organization) {
        this.organization = organization;
    }

    public ExternalOrgDetail getOrganization() {
        return organization;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public Person getRecipient() {
        return recipient;
    }

    /**
     * Just a helper method.
     *
     * @return the recipient name
     */
    @Transient
    public String getRecipientName() {
        if (recipient) {
            return recipient.getFirstAndLastName();
        } else if(extOrgContactName) {
            return extOrgContactName;
        } else {
            return "Inventory";
        }
    }

    public void setMailingAddress(MailingAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public MailingAddress getMailingAddress() {
        return mailingAddress;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setExtOrgContactName(String extOrgContactName) {
        this.extOrgContactName = extOrgContactName;
    }

    public String getExtOrgContactName() {
        return extOrgContactName;
    }

    public String toString() {
        return "DeliveryDetail{" +
                "deliveryDetailId=" + deliveryDetailId +
                ", organization=" + organization +
                ", extOrgContactName='" + extOrgContactName + '\'' +
                ", recipient=" + recipient +
                ", facility=" + facility +
                '}';
    }
}
