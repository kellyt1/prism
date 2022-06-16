package us.mn.state.health.domain.repository.common.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.common.CategoryRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.common.Category;

public class HibernateCategoryRepositoryImpl extends HibernateDomainRepositoryImpl implements CategoryRepository {

    public HibernateCategoryRepositoryImpl(HibernateTemplate template) {
        super(template);
    }

    public List findAll(final boolean onlyRootCategories) {
        return (List) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria crit = null;
                List categories = new ArrayList();
                if (onlyRootCategories) {
                    crit = session.createCriteria(Category.class).addOrder(Order.asc("categoryCode")).addOrder(Order.asc("name"));
                    crit.setCacheRegion("findAllCategories");
                    crit.setCacheable(true);
                    categories = crit.add(Expression.isNull("parentCategory")).list();
                } else {
                    crit = session.createCriteria(Category.class).addOrder(Order.asc("categoryCode")).addOrder(Order.asc("name"));
                    crit.setCacheRegion("findAllCategories");
                    crit.setCacheable(true);
                    categories = crit.list();
                }
                return categories;
            }
        });
    }

    public Collection findChildCategoriesByParentCode(String parentCode) {
        return null;
    }

    public Collection findDescendantCategoriesByParentCode(String parentCode, boolean addThisToo) {
        return null;
    }

    public Integer getNumberOfItemsInCategory(Category category) {
        return null;
    }

    public Category getCategoryById(Long categoryId) {
        Object cat = super.get(Category.class, categoryId);
        return cat != null ? (Category) cat : null;
    }

    public Category findByCategoryCode(String categoryCode) {
        return null;
    }

    public void makePersistent(Category category) {
        super.makePersistent(category);
    }

    public Category loadCategoryById(long categoryId) {
        return (Category) super.load(Category.class, categoryId);
    }
}
