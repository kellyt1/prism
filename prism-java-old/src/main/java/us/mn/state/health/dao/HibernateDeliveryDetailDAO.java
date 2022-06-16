package us.mn.state.health.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;

public class HibernateDeliveryDetailDAO implements DeliveryDetailDAO {
    private static Log log = LogFactory.getLog(HibernateDeliveryDetailDAO.class);

    public HibernateDeliveryDetailDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch(InfrastructureException e) {
            log.error("Begin Tx failed in HibernateDeliveryDetailDAO()",e);
        }
    }

    public DeliveryDetail getDeliveryDetailById(Long deliveryDetailID) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        DeliveryDetail deliveryDetail = null;
        try {

                deliveryDetail = (DeliveryDetail) session.load(DeliveryDetail.class, deliveryDetailID);
       }
        catch(HibernateException e) {
            log.error("Error in HibernateDeliveryDetailDAO.getDeliveryDetailById()",e);
            throw new InfrastructureException(e);
        }
        return deliveryDetail;
    }
}
