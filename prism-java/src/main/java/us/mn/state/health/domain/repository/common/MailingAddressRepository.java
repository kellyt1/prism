package us.mn.state.health.domain.repository.common;

import java.util.List;

import us.mn.state.health.model.common.MailingAddress;

public interface MailingAddressRepository {
    List<MailingAddress> findShippingAddresses();
}
