package us.mn.state.health.model.materialsrequest;

import org.hibernate.search.annotations.*;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Priority;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.util.BusinessRulesEngine;
import us.mn.state.health.model.util.search.EntityIndex;
import us.mn.state.health.model.util.search.RequestIndex;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;

@Entity
@Indexed(index = "requestIndex")
public class Request implements Serializable {
    private Long requestId;
    @ContainedIn
    @OneToMany(mappedBy = "request")
    private Collection requestLineItems = new LinkedHashSet();
    private Person requestor;
    private Date dateRequested;
    private Date needByDate;
    private String additionalInstructions;
    private Priority priority;
    private String trackingNumber;
    private DeliveryDetail deliveryDetail;
    private DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
    private Long helpDeskticketId;

    private int version;

    public void assignTrackingNumber() throws InfrastructureException {
        String nextTrackingNumber = daoFactory.getRequestDAO().findNextTrackingNumber();
        this.setTrackingNumber(nextTrackingNumber);
    }

    public void executeBusinessRules() throws InfrastructureException {
        executeBusinessRules(false);
    }
    public void executeBusinessRules(Boolean bCheck) throws InfrastructureException {
        BusinessRulesEngine bre = new BusinessRulesEngine();
        bre.applyMaterialsRequestRules(this,bCheck);
    }

    public void save() throws InfrastructureException {
        daoFactory.getRequestDAO().makePersistent(this);
    }

    public void addRequestLineItem(RequestLineItem lineItem) throws InfrastructureException {
        this.getRequestLineItems().add(lineItem);
        lineItem.setRequest(this);
    }

    public void removeRequestLineItem(RequestLineItem lineItem) throws InfrastructureException {
        if (this.getRequestLineItems().contains(lineItem)) {
            getRequestLineItems().remove(lineItem);
            lineItem.setRequest(null);
            lineItem.delete();
        }
    }

    public EntityIndex getEntityIndex() throws InfrastructureException {
        return new RequestIndex();
    }

    public Collection getRequestLineItems() {
        return requestLineItems;
    }

    public void setRequestLineItems(Collection requestLineItems) {
        this.requestLineItems = requestLineItems;
    }

    public Person getRequestor() {
        return requestor;
    }

    public void setRequestor(Person requestor) {
        this.requestor = requestor;
    }

    @Id
    @DocumentId
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public void setNeedByDate(Date needByDate) {
        this.needByDate = needByDate;
    }

    public Date getNeedByDate() {
        return needByDate;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", requestLineItems=" + requestLineItems +
                ", requestor=" + requestor +
                ", dateRequested=" + dateRequested +
                ", needByDate=" + needByDate +
                ", additionalInstructions='" + additionalInstructions + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", deliveryDetail=" + deliveryDetail +
                '}';
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public String getDeliverToInfoAsString() {
        return (getDeliveryDetail() != null) ? getDeliveryDetail().getLongSummary() : "";
    }

    public Long getHelpDeskticketId() {
        return helpDeskticketId;
    }

    public void setHelpDeskticketId(Long helpDeskticketId) {
        this.helpDeskticketId = helpDeskticketId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Request)) return false;

        final Request that = (Request) o;

        if (this.getRequestId() == null) {
            if (that.getRequestId() == null) {
                //dig deeper, using comparison by value
                if (this.getRequestor() != null && !this.getRequestor().equals(that.getRequestor())) {
                    return false;
                }
                if (this.getDateRequested() != null && !this.getDateRequested().equals(that.getDateRequested())) {
                    return false;
                }
                return true;
            } else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        } else {  //we know we can't get a NullPointerException now...
            return this.getRequestId().equals(that.getRequestId());
        }
    }

    public int hashCode() {
        int result = 14;
        result = 29 * result + (getDateRequested() != null ? getDateRequested().hashCode() : 0);
        result = 29 * result + (getRequestor() != null ? getRequestor().hashCode() : 0);
        return result;
    }


    private String getRequestorName() {
        return getRequestor().getFirstAndLastName();
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getConcatenatedContent() {
        return getTrackingNumber() + " " + getRequestorName() + " " + getVersion();
    }

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String getRequestorId() {
        return getRequestor().getPersonId().toString();
    }


    public Boolean getAllLineItemsActedOn() {
        Boolean b = Boolean.TRUE;
        for (Iterator iterator = requestLineItems.iterator(); iterator.hasNext(); ) {
            RequestLineItem rli = (RequestLineItem)iterator.next();
            if (rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
                b = Boolean.FALSE;
                break;
            }
        }
        return b;
    }

}
