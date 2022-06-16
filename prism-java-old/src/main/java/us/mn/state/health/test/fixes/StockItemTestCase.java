package us.mn.state.health.test.fixes;

import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.text.ParseException;

import junit.framework.TestCase;
import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.StockItemHitReorderPointEmailBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItemLocation;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.persistence.HibernateUtil;

public class StockItemTestCase extends TestCase {
    private String user;
    private HibernateDAO hibernateDAO;
    private DAOFactory daoFactory;
    private String environment;

    protected void setUp() {
//        environment = Constants.DEVDB;
//        environment = Constants.TEST;
        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);

        user = "ochial1";
        hibernateDAO = new HibernateDAO();
        daoFactory = new HibernateDAOFactory();

    }
    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }

    public void testRequestLineItemsUnderROP() throws InfrastructureException {
        String hql = "select s from StockItem s where s.qtyOnHand < s.reorderPoint and s.isOnOrder = 0 " +
                "and s.category.categoryCode in ('366','365','368','399','141','140','375','381') order by s.category.categoryCode asc, s.icnbr asc";
        Collection stockItems = hibernateDAO.executeQuery(hql);
        int i = 0;
        System.out.println("ICNBR\tQTYonHand\tRQP");
        for (Iterator iterator = stockItems.iterator(); iterator.hasNext();) {
            StockItem stockItem = (StockItem) iterator.next();
            if (!stockItem.getDiscontinued().booleanValue()) {
                System.out.println(stockItem.getFullIcnbr() + "\t" + stockItem.getQtyOnHand() + "\t" + stockItem.getReorderPoint());
                i++;
                EmailBean emailBean = new EmailBean();
                EmailBuilder emailBuilder = new StockItemHitReorderPointEmailBuilder(stockItem, emailBean);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                System.out.println(emailBean.getTo());
                System.out.print("");
            }

        }

        System.out.println(i + " stock items");
    }

    public void testGetExternalOrgDetailsThatOrderedStockItems() throws InfrastructureException {
        String hql = "select distinct rli.request.deliveryDetail.organization.orgName from RequestLineItem rli" +
                "join rli.item item" +
                "where item.itemId in ('212112','212114')" +
                " order by rli.request.deliveryDetail.organization.orgName asc";
        Collection extOrgs = hibernateDAO.executeQuery(hql);
        for (Iterator iterator = extOrgs.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            System.out.println(name);

        }
    }

    public void testDeleteUsersFromGroup() throws InfrastructureException, InterruptedException {
        Group stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                                            .getGroupDAO()
                                            .findGroupByCode(Group.STOCK_CONTROLLER_CODE);
            for(Iterator iter = stkController.getPersonGroupLinks().iterator(); iter.hasNext(); ) {
                PersonGroupLink pgl = (PersonGroupLink)iter.next();
                Person person = pgl.getPerson();
                if(person.getLastName().equals("Coon") && person.getFirstName().equals("David")){
                    person.getPersonGroupLinks().remove(pgl);
                    hibernateDAO.makeTransient(pgl);
                    stkController.getPersonGroupLinks().remove(pgl);
//                    hibernateDAO.makePersistent(person);
//                    hibernateDAO.makePersistent(stkController);
                    hibernateDAO.makeTransient(pgl);
                }
            }
    }

    public void testStockItemOutTransactions() throws InfrastructureException{
        String hql = "select distinct rli,item, r.trackingNumber, rli.quantity, rli.quantityFilled," +
                " r.dateRequested" +
                " from RequestLineItem rli " +
                "join rli.item item " +
                "join rli.request r " +
                "join rli.fundingSources fs " +
                "where rli.fundingSources.size >1 and" +
                "(select count(si) from StockItem si where si.itemId = item.itemId)>0 " +
                "and " +
                "fs.orgBudget.orgBudgetCode like '42%' " +
                "and " +
                "((rli.status.statusCode = 'FLD' and rli.quantityFilled>0 ) or " +
                " (rli.status.statusCode = 'WFD' and rli.quantityFilled>0))";
        hql = "select new us.mn.state.health.view.materialsrequest.reports.StockItemOutTransaction(item,rli.request.trackingNumber)"+
                " from RequestLineItem rli " +
                "join rli.item item " +
                "join rli.request r " +
                "join rli.fundingSources fs " +
                "where rli.fundingSources.size >1 and" +
                "(select count(si) from StockItem si where si.itemId = item.itemId)>0 " +
                "and " +
                "fs.orgBudget.orgBudgetCode like '42%' " +
                "and " +
                "((rli.status.statusCode = 'FLD' and rli.quantityFilled>0 ) or " +
                " (rli.status.statusCode = 'WFD' and rli.quantityFilled>0))";
        Collection results = hibernateDAO.executeQuery(hql);
        System.out.println("");
    }

    public void testStockItemOutTransactions1() throws InfrastructureException, ParseException {
        Date fy = DateUtils.createDate("06/30/2006",DateUtils.DEFAULT_DATE_FORMAT);
        Date startDate = DateUtils.createDate("03/1/2006",DateUtils.DEFAULT_DATE_FORMAT);
        Date endDate = DateUtils.createDate("06/30/2006",DateUtils.DEFAULT_DATE_FORMAT);
        String orgBdgt="4%";
        HibernateDAO hibernateDAO = new HibernateDAO();
        hibernateDAO.addQueryParam("fy",fy);
        hibernateDAO.addQueryParam("startDate",startDate);
        hibernateDAO.addQueryParam("endDate",endDate);
        hibernateDAO.addQueryParam("orgBdgt",orgBdgt);
        Collection results = hibernateDAO.executeNamedQuery("findStockItemOutTransactions");
        System.out.println("");

    }

    /**
     * Display icnbr, description, vendor name, QtyOnHand, Annual Usage, Dispense Unit, DispenseUnitCost, Locations
     * @throws InfrastructureException
     */

    public void testDisplayStockItemsInCategory() throws InfrastructureException {
        String hql = "from StockItem s where s.category.categoryCode='141' and s.potentialIndicator = 'N' order by s.icnbr asc";
        Collection stockItems = hibernateDAO.executeQuery(hql);
        System.out.println("ICNBR\tDESCRIPTION\tVendor Name\tQtyOnHand\tAnnual Usage\tDispense Unit\tDispense Unit Cost\tLocations");
        for (Iterator iterator = stockItems.iterator(); iterator.hasNext();) {
            StockItem stockItem = (StockItem) iterator.next();
            String icnbr = stockItem.getFullIcnbr();
            String description = stockItem.getDescription();

            String[] desc = description.split("\\s");
            String vendors = "";
            for (Iterator iterator1 = stockItem.getItemVendors().iterator(); iterator1.hasNext();) {
                ItemVendor iv =  (ItemVendor) iterator1.next();
                vendors+=iv.getVendor().getExternalOrgDetail().getOrgName();
                if(iv.getPrimaryVendor().booleanValue()){
                    vendors+="(primary)";
                }
                if (iterator.hasNext()) {
                    vendors+=";";
                }
            }
            Integer qtyOnHand = stockItem.getQtyOnHand();
            Integer annualUsage = stockItem.getAnnualUsage();
            Double dispenseUnitCost = stockItem.getDispenseUnitCost();
            String dispenseUnit = stockItem.getDispenseUnit().getName();
            String locations = "";
            for (Iterator iterator1 = stockItem.getLocations().iterator(); iterator1.hasNext();) {
                StockItemLocation stockItemLocation = (StockItemLocation) iterator1.next();
                locations+=stockItemLocation.getFacility().getFacilityName()+"-"+stockItemLocation.getLocationCode();
                if(iterator1.hasNext()){
                    locations+=";";
                }
            }

            description= "";
            for (int i = 0; i < desc.length; i++) {
                description+= desc[i]+" ";
            }

            System.out.println(icnbr+"\t"+description+"\t"+vendors+"\t"+qtyOnHand+"\t"+annualUsage+"\t"+dispenseUnit+"\t"+dispenseUnitCost+"\t"+locations);
        }
        System.out.println("");
    }

    /**
     * 11/30/2006
     * Display icnbr,  QtyOnHand, ROP, Dispense Unit, Locations
     * @throws InfrastructureException
     */

    public void testDisplayStockItemsBelowROP() throws InfrastructureException {
        String hql = "from StockItem si where si.qtyOnHand < si.reorderPoint and si.status.statusCode = 'ACT' and si.isOnOrder = 0 order by si.category.categoryCode asc, si.icnbr asc";
        Collection stockItems = hibernateDAO.executeQuery(hql);
        System.out.println("ICNBR\tDESCRIPTION\tVendor Name\tQtyOnHand\tAnnual Usage\tDispense Unit\tDispense Unit Cost\tLocations");
        int k = 1;
        for (Iterator iterator = stockItems.iterator(); iterator.hasNext();) {
            StockItem stockItem = (StockItem) iterator.next();
            String icnbr = stockItem.getFullIcnbr();
            String description = stockItem.getDescription();

            String[] desc = description.split("\\s");

            Integer qtyOnHand = stockItem.getQtyOnHand();
            String dispenseUnit = stockItem.getDispenseUnit().getName();
            String locations = "";
            for (Iterator iterator1 = stockItem.getLocations().iterator(); iterator1.hasNext();) {
                StockItemLocation stockItemLocation = (StockItemLocation) iterator1.next();
                locations+=stockItemLocation.getFacility().getFacilityName()+"-"+stockItemLocation.getLocationCode();
                if(iterator1.hasNext()){
                    locations+=";";
                }
            }

            description= "";
            for (int i = 0; i < desc.length; i++) {
                description+= desc[i]+" ";
            }

            System.out.println(k+"\t"+icnbr+"\t"+qtyOnHand+"\t"+stockItem.getReorderPoint()+"\t"+dispenseUnit+"\t"+locations);
            k++;
        }
        System.out.println("");
    }

    /**
     * Display icnbr, description, QtyOnHand, Dispense Unit, DispenseUnitCost, OnDemand
     * @throws InfrastructureException
     */

    public void testDisplayStockItemsInCategory1() throws InfrastructureException {
        String hql = "from StockItem s where s.category.categoryCode='399' order by s.icnbr";
        Collection stockItems = hibernateDAO.executeQuery(hql);
        System.out.println("ICNBR\tDESCRIPTION\tQtyOnHand\tDispense Unit\tDispense Unit Cost\tCurrent Demand");
        for (Iterator iterator = stockItems.iterator(); iterator.hasNext();) {
            StockItem stockItem = (StockItem) iterator.next();
            String icnbr = stockItem.getFullIcnbr();
            String description = stockItem.getDescription();
            Integer qtyOnHand = stockItem.getQtyOnHand();
            Double dispenseUnitCost = stockItem.getDispenseUnitCost();
            String dispenseUnit = stockItem.getDispenseUnit().getName();
            Integer currentDemand = stockItem.getCurrentDemand();

            System.out.println(icnbr+"\t"+description+"\t"+qtyOnHand+"\t"+dispenseUnit+"\t"+dispenseUnitCost+"\t"+currentDemand);
        }
        System.out.println("");
    }

    /**
     * Display the stock items that have the same ICNBR
     */
    public void testDisplayDuplicateStockItems() throws InfrastructureException {
        String hql = "from StockItem si where si.potentialIndicator = 'N'";
        Collection stockItems = new HibernateDAO().executeQuery(hql);
        Map cart = new HashMap();
        for (Iterator iterator = stockItems.iterator(); iterator.hasNext();) {
            StockItem stockItem = (StockItem) iterator.next();
            String fullIcnbr = stockItem.getFullIcnbr();
            if(!cart.containsKey(fullIcnbr)){
                cart.put(fullIcnbr,new Integer(1));
            }
            else {
                cart.put(fullIcnbr, new Integer(((Integer) cart.get(fullIcnbr)).intValue()+1));
            }
            HibernateUtil.getSession().evict(stockItem);
        }

        Set keys = cart.keySet();

        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            int count = ((Integer)cart.get(key)).intValue();
            if(count>1){
                System.out.println(key + " :!: " + count);
            }
        }

    }

    public static void main(String[] args) throws InfrastructureException, InterruptedException {
        StockItemTestCase stockItemTestCase = new StockItemTestCase();
        stockItemTestCase.setUp();
//        stockItemTestCase.testRequestLineItemsUnderROP();
        stockItemTestCase.testDeleteUsersFromGroup();
//        stockItemTestCase.testGetExternalOrgDetailsThatOrderedStockItems();
        stockItemTestCase.tearDown();
    }
}
