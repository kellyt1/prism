package us.mn.state.health.builder.purchasing;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.view.purchasing.OrdersForm;

public class OrdersFormBuilder  {

    private OrdersForm ordersForm;
    private DAOFactory daoFactory;
    
    public OrdersFormBuilder(OrdersForm ordersForm, DAOFactory daoFactory) {
        this.ordersForm = ordersForm;
        this.daoFactory = daoFactory;
    }
    
    public void buildDefaultProperties() {        
    }
    
    public void buildVendors() throws InfrastructureException {
//        Collection vendors = daoFactory.getVendorDAO().findAll(false);
        Collection vendors = daoFactory.getVendorDAO().findAll();
        ordersForm.setVendors(vendors);
    }
    
    public void buildStatuses() throws InfrastructureException {
        String[] statusCodes = {Status.ORDERED, Status.ORDERED_ON_BACK_ORDER, Status.CANCELED};
        Collection statuses = 
            daoFactory.getStatusDAO().findAllByStatusTypeAndStatusCodes(StatusType.MATERIALS_ORDER,
                                                                        statusCodes);
        ordersForm.setStatuses(statuses);
    }
    
    public void buildRequestors() throws InfrastructureException {
        Collection persons = daoFactory.getPersonDAO().findAllMDHEmployees();
        ordersForm.setRequestors(persons);
    }
    
    public void buildPurchasers() throws InfrastructureException {
        Collection persons = daoFactory.getPersonDAO().findPersonsByGroupCode(Group.BUYER_CODE);
        ordersForm.setPurchasers(persons);
    }
}