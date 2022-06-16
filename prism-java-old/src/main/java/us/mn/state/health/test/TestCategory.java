package us.mn.state.health.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import junit.framework.TestCase;
import org.hibernate.SQLQuery;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.CategoryDAO;
import us.mn.state.health.dao.HibernateCategoryDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.persistence.HibernateUtil;

public class TestCategory extends TestCase {
    private CategoryDAO categoryDAO;
    private long selectedCategoryId;
    private ArrayList categoriesPath;
    private String categoryCode;
    private ArrayList subCategories;
    private boolean addThisToo;

    protected void setUp() {
        categoriesPath = new ArrayList();
        categoryDAO = new HibernateCategoryDAO();
        selectedCategoryId = 47556;
        categoryCode = Category.MATERIALS_LABSUPPLIES;
        subCategories = new ArrayList();
        addThisToo = true;
    }

    public void testPathToSelectedCategory() throws InfrastructureException {
        long t1 = System.currentTimeMillis();
        Category category = categoryDAO.getCategoryById(new Long(selectedCategoryId), false);
        categoriesPath.add(0, category);
        Category parent = category.getParentCategory();
        while (parent != null) {
            categoriesPath.add(0, parent);
            parent = parent.getParentCategory();
        }
        System.out.println(System.currentTimeMillis() - t1);
        System.out.println(categoriesPath);
    }

    public void testSubcategoriesOfCategoryWithCode() throws InfrastructureException {
        long t1 = System.currentTimeMillis();
        findChildCategories("MAT", false);
//        System.out.println(subCategories.size());
        Collections.sort(subCategories);
        System.out.println(subCategories);
        System.out.println("Time:" + (System.currentTimeMillis() - t1));

    }

    public void testFindDescendantCategoriesByParentCode() throws InfrastructureException {
        System.out.println("testFindDescendantCategoriesByParentCode()");
        Collection result = categoryDAO.findDescendantCategoriesByParentCode("MAT", false);
        System.out.println(result);
    }

    public void testFindDescendantCategoriesByParentCodeSQL() throws InfrastructureException {
        System.out.println("testFindDescendantCategoriesByParentCode()");
        ArrayList subCategories = new ArrayList();
        String sqlString = addThisToo?
                "select {category.*} from CATEGORY_TBL category where category_code = :catCode\n" +
                        "UNION\n" +
                        "select {category.*} from CATEGORY_TBL category \n" +
                        "CONNECT BY parent_category_id = PRIOR category_id\n" +
                        "START WITH parent_category_id in " +
                "(select category_id from CATEGORY_TBL category where category_code = :catCode)"
                :"select {category.*} from CATEGORY_TBL category \n" +
                "CONNECT BY parent_category_id = PRIOR category_id\n" +
                "START WITH parent_category_id in " +
                "(select category_id from CATEGORY_TBL category where category_code = :catCode)";
        SQLQuery sqlQuery = HibernateUtil.getSession().createSQLQuery(sqlString);
        subCategories = (ArrayList) sqlQuery.
                addEntity("category", Category.class).
                setString("catCode",categoryCode).setCacheable(true).list();
//                setLong("catId",selectedCategoryId).setCacheable(true).list();
        Collections.sort(subCategories);
        System.out.println(subCategories);
    }

    private void findChildCategories(String categoryCode, boolean addThis) throws InfrastructureException {
        Category category = categoryDAO.findByCategoryCode(categoryCode);
        if (addThis) {
            subCategories.add(category);
        }
        Collection temp = category.getChildCategories();
        Iterator iterator = temp.iterator();
        while (iterator.hasNext()) {
            Category c = (Category) iterator.next();
            findChildCategories(c.getCategoryCode(), true);
        }

    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();
        categoryDAO = null;
    }
}
