package us.mn.state.health.domain.repository.inventory.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.inventory.StockQtyChangeReasonRefRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.inventory.StockQtyChangeReasonRef;

public class StockQtyChangeReasonRefRepositoryImpl extends HibernateDomainRepositoryImpl implements StockQtyChangeReasonRefRepository {

    public StockQtyChangeReasonRefRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public void makePersistent(Object entity) {
        super.makePersistent(entity);
    }

    public List<StockQtyChangeReasonRef> findAll() {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createQuery("from StockQtyChangeReasonRef reason order by reason.reason asc").setCacheable(true).list();
            }
        });
    }

    public StockQtyChangeReasonRef getById(Long reasonId) {
        Object o = super.get(StockQtyChangeReasonRef.class, reasonId);
        return o != null ? (StockQtyChangeReasonRef) o : null;
    }
}
