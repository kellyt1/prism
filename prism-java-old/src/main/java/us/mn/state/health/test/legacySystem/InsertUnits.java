package us.mn.state.health.test.legacySystem;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.inventory.HibernateUnitDAO;
import us.mn.state.health.dao.inventory.UnitDAO;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.persistence.HibernateUtil;
import us.mn.state.health.util.Utilities;

/**
 * This Test Case inserts the units that were found in the legacy tables
 * and that doesn't exist in the new Unit table
 */
public class InsertUnits extends TestCase {
    UnitDAO unitDAO;


    protected void setUp() {
        unitDAO = new HibernateUnitDAO();
    }

    public void testInsertInventoryUnits() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        List list = session.createQuery("select distinct i.dispenseUnit from Inventory i").list();
        Set results = new TreeSet();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            String decodedS = Utilities.decodeUnitCode(s);
            results.add(decodedS);
        }
        System.out.println("==============================================================");
        System.out.println(results.size());
        System.out.println(results);

        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            Unit existingUnit = unitDAO.findUnitByCode(s);
            if(existingUnit == null) {
                Unit newUnit = new Unit(s,s);
                unitDAO.makePersistent(newUnit);
                System.out.println(newUnit);
            }
        }
    }

    public void testInsertPurchaseUnits() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        List list = session.createQuery("select distinct p.buyUnit from Purchase p").list();
        System.out.println(list.size());
        Set results = new TreeSet();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            String decodedS = Utilities.decodeUnitCode(s);
            results.add(decodedS);
        }
        System.out.println("==============================================================");
        System.out.println(results.size());
        System.out.println(results);

        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            Unit existingUnit = unitDAO.findUnitByCode(s);
            if(existingUnit == null) {
                Unit newUnit = new Unit(s,s);
                unitDAO.makePersistent(newUnit);
                System.out.println(newUnit);
            }
        }

    }

    public void testInsertStockInvUnits() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        List list = session.createQuery("select distinct si.unit from StockInv si").list();
        System.out.println(list.size());
        Set results = new TreeSet();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            String decodedS = Utilities.decodeUnitCode(s);
            results.add(decodedS);
        }
        System.out.println("==============================================================");
        System.out.println(results.size());
        System.out.println(results);

        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            Unit existingUnit = unitDAO.findUnitByCode(s);
            if(existingUnit == null) {
                Unit newUnit = new Unit(s,s);
                unitDAO.makePersistent(newUnit);
                System.out.println(newUnit);
            }
        }
    }


    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
        unitDAO = null;
    }
}