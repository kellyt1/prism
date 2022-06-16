package us.mn.state.health.facade.purchasing.searchorder;

import java.util.List;

import org.hibernate.Hibernate;
import us.mn.state.health.domain.repository.common.MailingAddressRepository;
import us.mn.state.health.domain.repository.common.StatusRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.purchasing.OrderRepository;
import us.mn.state.health.domain.repository.purchasing.OrderSearchCriteria;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.PagedQueryResultEnhanced;

public class SearchOrdersFacadeImpl implements SearchOrdersFacade {
    private StatusRepository statusRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private MailingAddressRepository mailingAddressRepository;


    public SearchOrdersFacadeImpl(StatusRepository statusRepository, UserRepository userRepository, OrderRepository orderRepository, MailingAddressRepository mailingAddressRepository) {
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.mailingAddressRepository = mailingAddressRepository;
    }

    public SearchOrdersDropDownListsDTO getDropDownLists() {

        return new SearchOrdersDropDownListsDTO(
                userRepository.getUsersByGroupCode(Group.BUYER_CODE),
                userRepository.findAllUsers(),
                statusRepository.findAllByStatusTypeCode(StatusType.MATERIALS_ORDER),
                mailingAddressRepository.findShippingAddresses()
        );
    }

    public PagedQueryResultEnhanced searchOrders(OrderSearchCriteria criteria) throws NotificationException {
        PagedQueryResultEnhanced queryResultEnhanced = orderRepository.findOrders(criteria);
        List<Order> orders = queryResultEnhanced.getPageList();
        for (Order order : orders) {
            Hibernate.initialize(order.getPurchaser());
            Hibernate.initialize(order.getVendor());
            Hibernate.initialize(order.getVendor().getExternalOrgDetail());
            Hibernate.initialize(order.getShipToAddress());
        }
        return queryResultEnhanced;
    }
}
