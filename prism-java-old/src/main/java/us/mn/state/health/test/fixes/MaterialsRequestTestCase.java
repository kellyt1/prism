package us.mn.state.health.test.fixes;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.text.ParseException;

import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import junit.framework.TestCase;

public class MaterialsRequestTestCase extends TestCase {
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

    public void testRequestLineItem() throws InfrastructureException {

        RequestLineItem requestLineItem=  daoFactory.getRequestLineItemDAO().getRequestLineItemById(new Long(310557), false);
        System.out.println("");
    }

    public void viewComputerPurchases() throws InfrastructureException{
        String hlq = "select rli from RequestLineItem rli " +
                "join rli.orderLineItem oli " +
                "join oli.order o " +
                "join rli.fundingSources fs " +
                "where " +
                "rli.item is null " +
                "and rli.itemCategory.categoryCode = 'COMP' " +
                "order by rli.requestLineItemId desc ";
        Collection results = hibernateDAO.executeQuery(hlq);
        results.size();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            RequestLineItem requestLineItem = (RequestLineItem) iterator.next();
            OrderLineItem oli = requestLineItem.getOrderLineItem();
            Order order = oli.getOrder();
            String description = requestLineItem.getItemDescription();
//            String fs

        }
    }

    public void testFindOrderedPurchaseItemTransactions() throws InfrastructureException, ParseException {
        String categoryId = "%%";
        String startDat = "02/16/2006";
        String endDat = "04/16/2006";
        String orgBdgt = "%%";
        Date startDate = DateUtils.createDate(startDat);
        Date endDate = DateUtils.createDate(endDat);
        Date beginFY = DateUtils.createDate("01/01/2006");
        Date endFY = DateUtils.createDate("12/01/2006");
        hibernateDAO.addQueryParam("categoryId",categoryId);
        hibernateDAO.addQueryParam("startDate",startDate);
        hibernateDAO.addQueryParam("endDate", endDate);
        hibernateDAO.addQueryParam("orgBdgt", orgBdgt);
        hibernateDAO.addQueryParam("beginFY", beginFY);
        hibernateDAO.addQueryParam("endFY", endFY);
        Collection results = hibernateDAO.executeNamedQuery("findOrderedPurchaseItemTransactions");
        results.size();
    }

    public void testViewStockItemInTransactions() throws InfrastructureException, ParseException {
        String categoryId = "%%";
        String categoryCode = "%%";
        String icnbr = "%%";
        String startDat = "02/16/2006";
        String endDat = "04/16/2006";
        String orgBdgt = "4%";
        Date startDate = DateUtils.createDate(startDat);
        Date endDate = DateUtils.createDate(endDat);
        hibernateDAO.addQueryParam("categoryId",categoryId);
        hibernateDAO.addQueryParam("categoryCode",categoryCode);
        hibernateDAO.addQueryParam("icnbr",icnbr);
        hibernateDAO.addQueryParam("startDate",startDate);
        hibernateDAO.addQueryParam("endDate", endDate);
        hibernateDAO.addQueryParam("orgBdgt", orgBdgt);
        Collection results = hibernateDAO.executeNamedQuery("findStockItemInTransactions");
        results.size();
    }
}

