package us.mn.state.health.domain.repository.common.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.PriorityRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.Priority;

public class HibernatePriorityRepositoryImpl
        extends HibernateDomainRepositoryImpl
        implements PriorityRepository {

    public HibernatePriorityRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public List<Priority> findAllPriorities() {
        return super.findAll(Priority.class, true);
    }
}
