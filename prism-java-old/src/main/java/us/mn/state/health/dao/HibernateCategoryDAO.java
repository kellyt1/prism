package us.mn.state.health.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateCategoryDAO implements CategoryDAO {

    public HibernateCategoryDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }

    /**
     * Finds Child Categories By Parent Code in persistence store
     *
     * @param parentCode
     * @return Collection of categories
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *          todo blah
     */
    public Collection findChildCategoriesByParentCode(String parentCode) throws InfrastructureException {
        try {
            Session session = HibernateUtil.getSession();
            Query query = session.getNamedQuery("findChildCategoriesByParentCode");
            query.setString("categoryCode", parentCode);
            query.setCacheable(true);
            Collection c = query.list();
            return c;
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * Finds all the descendant categories by Parent Code in persistence store
     *
     * @param parentCode
     * @param addThisToo
     * @return
     * @throws InfrastructureException
     *
     * Todd R. 2008-05-20
     *    I believe there is an error on the lines "START WITH parent_category_id in " +
     *    I think the parent_category_id should actually be category_id in order for the Oracle Hierarchical query
     *    using START WITH and CONNECT BY to work as designed.  When implemented as intended, the query returns the
     *    root parent also which would perform the functionality which was implemented using the addThisToo boolean and
     *    the related query.  Making this change in the code below would require additional modifications in order to
     *    retain the same functionality, such as:
     *     : "select {category.*} from CATEGORY_TBL category \n" +
     *          "where category_id in (select category_id from CATEGORY_TBL category \n" +
     *          "CONNECT BY parent_category_id = PRIOR category_id \n" +
     *          "START WITH category_id in " +
     *          "(select category_id from CATEGORY_TBL category where category_code = :catCode)) " +
     *          "and category_code != :catCode";
     *
     */
    public Collection findDescendantCategoriesByParentCode(String parentCode, boolean addThisToo) throws InfrastructureException {
        ArrayList subCategories = new ArrayList();
//        findChildCategories(parentCode, addThisToo, subCategories);
        String sqlString = addThisToo ?
                "select {category.*} from CATEGORY_TBL category where category_code = :catCode\n" +
                        "UNION\n" +
                        "select {category.*} from CATEGORY_TBL category \n" +
                        "CONNECT BY parent_category_id = PRIOR category_id\n" +
                        "START WITH parent_category_id in " +
                        "(select category_id from CATEGORY_TBL category where category_code = :catCode)"
                : "select {category.*} from CATEGORY_TBL category \n" +
                "CONNECT BY parent_category_id = PRIOR category_id\n" +
                "START WITH parent_category_id in " +
                "(select category_id from CATEGORY_TBL category where category_code = :catCode)";
        SQLQuery sqlQuery = HibernateUtil.getSession().createSQLQuery(sqlString);
        subCategories = (ArrayList) sqlQuery.
                addEntity("category", Category.class).
                setString("catCode", parentCode).setCacheable(true).list();

        Collections.sort(subCategories);
        return subCategories;
    }


    /**
     * Returns all allowable categories used for a particular purpose, such as all those used when making a REUEST or
     * all categories used for STOCK/Warehouse items.
     *
     * @param usedFor  -- currently either "REQUEST" or "STOCK" defined as constants in Category.java
     * @throws InfrastructureException
     */
    public Collection findCategoriesByUsedFor(String usedFor) throws InfrastructureException {
        ArrayList subCategories = new ArrayList();
        ArrayList finalCategories = new ArrayList();
        String sqlString = "select {category.*} from CATEGORY_TBL category where used_for like :usedFor";

        SQLQuery sqlQuery = HibernateUtil.getSession().createSQLQuery(sqlString);
        subCategories = (ArrayList) sqlQuery.
                addEntity("category", Category.class).
                setString("usedFor", "%" + usedFor + "%").setCacheable(true).list(); //surrounds with wildcards so that
                                                                                     //categories with used_for with a value
                                                                                     //such as "BOTH REQUEST, STOCK" will be returned
                                                                                     //for both REQUEST  & STOCK searches.

        // PRIS-181
        // This does it for "add Non-Catalog Item to cart option",   browseCatalog.jsp ??  23 Category originally ??
        Iterator iterator = subCategories.iterator();
        while (iterator.hasNext()) {
            Category c = (Category) iterator.next();
            String temp = c.getCategoryCode().toString();
            if ((temp.equalsIgnoreCase("COMP")) || (temp.equalsIgnoreCase("SOFTWR")))
                ;
            else
                finalCategories.add(c);
        }

        //Collections.sort(subCategories);
        //return subCategories;
        Collections.sort(finalCategories);
        return finalCategories;
    }

    // Change for PRIS-181 effects PRIS-159; need to be rewritten just coming from constructEditPurchasingRequestLineItemForm()
    /**
     * Returns all allowable categories used for a particular purpose, such as all those used when making a REUEST or
     * all categories used for STOCK/Warehouse items.
     *
     * @param usedFor  -- currently either "REQUEST" or "STOCK" defined as constants in Category.java
     * @throws InfrastructureException
     */
    public Collection findCategoriesByUsedForEdit(String usedFor) throws InfrastructureException {
        ArrayList subCategories = new ArrayList();
        String sqlString = "select {category.*} from CATEGORY_TBL category where used_for like :usedFor";

        SQLQuery sqlQuery = HibernateUtil.getSession().createSQLQuery(sqlString);
        subCategories = (ArrayList) sqlQuery.
                addEntity("category", Category.class).
                setString("usedFor", "%" + usedFor + "%").setCacheable(true).list(); //surrounds with wildcards so that
        //categories with used_for with a value
        //such as "BOTH REQUEST, STOCK" will be returned
        //for both REQUEST  & STOCK searches.

        Collections.sort(subCategories);
        return subCategories;
    }


    /**
     * Helper method for recursive navigation(top-down) in the Categories tree
     *
     * @param categoryCode
     * @param addThis
     * @param result
     * @throws InfrastructureException
     */

    private void findChildCategories(String categoryCode, boolean addThis, Collection result) throws InfrastructureException {
        Category category = findByCategoryCode(categoryCode);
        if (addThis) {
            result.add(category);
        }
        Collection temp = category.getChildCategories();
        Iterator iterator = temp.iterator();
        while (iterator.hasNext()) {
            Category c = (Category) iterator.next();
            findChildCategories(c.getCategoryCode(), true, result);
        }
    }


    public Category findByCategoryCode(String categoryCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        try {
            Collection c = session.getNamedQuery("findByCategoryCode")
                    .setString("categoryCode", categoryCode)
                    .setCacheable(true)
                    .list();
            if (c == null || c.size() == 0) {
                return null;
            } else {
                return (Category) c.iterator().next();
            }

        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }


    public Category findByCategoryName(String categoryName) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        try {
            Collection c = session.getNamedQuery("findByCategoryName")
                    .setString("categoryName", categoryName.toUpperCase())
                    .setCacheable(true)
                    .list();
            if (c == null || c.size() == 0) {
                return null;
            } else {
                return (Category) c.iterator().next();
            }

        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public Integer getNumberOfItemsInCategory(Category category) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        try {
            Object count = session.getNamedQuery("countItemsInCategory")
                    .setLong("categoryId", category.getCategoryId().longValue())
                    .uniqueResult();
            return new Integer(count.toString());
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public Category getCategoryById(Long categoryId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Category cat = null;
        try {
            if (lock) {
                cat = (Category) session.load(Category.class, categoryId, LockMode.UPGRADE);
            } else {
                cat = (Category) session.load(Category.class, categoryId);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return cat;
    }

    public Collection findAll(boolean onlyRootCategories) throws InfrastructureException {
        Collection categories;
        try {
            Criteria crit = null;
            if (onlyRootCategories) {
                crit = HibernateUtil.getSession().createCriteria(Category.class);
                crit.setCacheRegion("findAllCategories");
                crit.setCacheable(true);
                categories = crit.add(Expression.isNull("parentCategory")).addOrder(Order.asc("name")).addOrder(Order.asc("categoryCode")).list();
            } else {
                crit = HibernateUtil.getSession().createCriteria(Category.class);
                crit.setCacheRegion("findAllCategories");
                crit.setCacheable(true);
                categories = crit.addOrder(Order.asc("name")).addOrder(Order.asc("categoryCode")).list();
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return categories;
    }

    public Collection findByExample(Category exampleCategory) throws InfrastructureException {
        Collection categories;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(Category.class);
            crit.setCacheRegion("findCategoryByExample");
            crit.setCacheable(true);
            categories = crit.add(Example.create(exampleCategory)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return categories;
    }

    public void makePersistent(Category category) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(category);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Category category) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(category);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

}
