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
import us.mn.state.health.persistence.HibernateUtil;
import us.mn.state.health.util.Utilities;
/**
 * This class is ONLY for seeing how good the decode method works with the units from those 3 tables:
 * -Inventory
 * -Purchase
 * -StockInv
 */
public class TestUnitCodes extends TestCase {
    UnitDAO unitDAO;


    protected void setUp() {
        unitDAO = new HibernateUnitDAO();
    }

    public void testInventoryUnits() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        List list = session.createQuery("select distinct i.dispenseUnit from Inventory i").list();
        System.out.println(list.size());
        Set results = new TreeSet();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            String decodedS = Utilities.decodeUnitCode(s);
            System.out.println(decodedS);
            results.add(decodedS);
        }
        System.out.println("==============================================================");
        System.out.println(results.size());
        System.out.println(results);


    }

    public void testPurchaseUnits() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        List list = session.createQuery("select distinct p.buyUnit from Purchase p").list();
        System.out.println(list.size());
        Set results = new TreeSet();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            String decodedS = Utilities.decodeUnitCode(s);
            System.out.println(decodedS);
            results.add(decodedS);
        }
        System.out.println("==============================================================");
        System.out.println(results.size());
        System.out.println(results);

    }

    public void testStockInvUnits() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        List list = session.createQuery("select distinct si.unit from StockInv si").list();
        System.out.println(list.size());
        Set results = new TreeSet();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            String decodedS = Utilities.decodeUnitCode(s);
            System.out.println(decodedS);
            results.add(decodedS);
        }
        System.out.println("==============================================================");
        System.out.println(results.size());
        System.out.println(results);
    }

    protected void tearDown() {

    }
}