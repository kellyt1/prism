package us.mn.state.health.facade.purchasing.searchorder;

import java.io.Serializable;
import java.util.List;

import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.User;

public class SearchOrdersDropDownListsDTO implements Serializable {
    private List<User> buyers;
    private List<User> requestors;
    private List<Status> statuses;
    private List<MailingAddress> mailingAddresses;


    public SearchOrdersDropDownListsDTO(List<User> buyers, List<User> requestors, List<Status> statuses, List<MailingAddress> mailingAddresses) {
        this.buyers = buyers;
        this.requestors = requestors;
        this.statuses = statuses;
        this.mailingAddresses = mailingAddresses;
    }

    public List<User> getBuyers() {
        return buyers;
    }

    public List<User> getRequestors() {
        return requestors;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public List<MailingAddress> getMailingAddresses() {
        return mailingAddresses;
    }
}
