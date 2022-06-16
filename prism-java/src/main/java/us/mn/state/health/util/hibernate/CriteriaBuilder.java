package us.mn.state.health.util.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;

public interface CriteriaBuilder {
    public void addCriteria(Criteria criteria)
            throws HibernateException;
}
