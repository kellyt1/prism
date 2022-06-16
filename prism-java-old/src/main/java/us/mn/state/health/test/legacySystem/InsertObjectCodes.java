package us.mn.state.health.test.legacySystem;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.ObjectCodeDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.persistence.HibernateUtil;

/**
 * This testCase is for converting the old ObjectCode's of the Items from the old system to the new one
 */

public class InsertObjectCodes extends TestCase {
    DAOFactory daoFactory;
    String user;

    protected void setUp() {
        user = "ochianl1";
        daoFactory = new HibernateDAOFactory();
    }

    public void convertObjectCodes() throws InfrastructureException {
        ObjectCodeDAO objectCodeDAO = daoFactory.getObjectCodeDAO();
        Collection codes = getUniqueObjectCodes();
        for (Iterator iterator = codes.iterator(); iterator.hasNext();) {
            String code = (String) iterator.next();
            ObjectCode objectCode = null;
            objectCode = objectCodeDAO.findByCode(code);
            if (objectCode == null) {
                objectCode = new ObjectCode();
                objectCode.setObjectCode(code);
                objectCode.setInsertedBy(user);
                objectCode.setInsertionDate(new Date());
            }
            else {
                Category category = daoFactory.getCategoryDAO().findByCategoryCode(code);

                if(category!=null && !category.getName().equals(objectCode.getName())){
                    objectCode.setName(category.getName());
                }

            }
            if (objectCode != null) {
                objectCodeDAO.makePersistent(objectCode);
                System.out.println(objectCode);
            }
        }

    }

    private Collection getUniqueObjectCodes() throws InfrastructureException {
        Collection codes = HibernateUtil.getSession()
                .createQuery("select distinct p.code from Purchase p where p.code is not null").list();
        return codes;
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }
}