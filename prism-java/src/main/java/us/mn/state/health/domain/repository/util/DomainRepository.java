package us.mn.state.health.domain.repository.util;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockMode;

public interface DomainRepository {
    void makePersistent(Object entity);

    List findAll(Class clazz, boolean cacheable);

    Object get(Class entityClass, Serializable id);

    Object get(Class entityClass, Serializable id, LockMode lockMode);

    Object load(Class entityClass, Serializable id);

    Object load(Class entityClass, Serializable id, LockMode lockMode);
}
