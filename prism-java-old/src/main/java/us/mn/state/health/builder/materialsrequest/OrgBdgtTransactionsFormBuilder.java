package us.mn.state.health.builder.materialsrequest;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;
import java.util.ArrayList;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.view.materialsrequest.reports.StockItemTransactionsForm;

public class OrgBdgtTransactionsFormBuilder {
    private StockItemTransactionsForm  stockItemTransactionsForm;
    private DAOFactory daoFactory;

    public OrgBdgtTransactionsFormBuilder(StockItemTransactionsForm stockItemTransactionsForm, DAOFactory daoFactory) {
        this.stockItemTransactionsForm = stockItemTransactionsForm;
        this.daoFactory = daoFactory;
    }

    public void buildStockItemsOutWithFS() throws InfrastructureException, ParseException {
        String orgBdgtCode = stockItemTransactionsForm.getOrgBdgtCode();
        String[] fiscalYear = stockItemTransactionsForm.getFiscalYear();
        Date startDate = DateUtils.createDate(stockItemTransactionsForm.getDateFrom());
        Date endDate =  DateUtils.createDate(stockItemTransactionsForm.getDateTo());
        String categoryId = stockItemTransactionsForm.getCategoryId();
        String icnbr = stockItemTransactionsForm.getIcnbr();

        Collection stockItemTransactions = daoFactory.getRequestLineItemDAO().findStockItemOutTransactionsWithFS(orgBdgtCode, fiscalYear, startDate, endDate, categoryId, icnbr);

        stockItemTransactionsForm.setStockItemTransactions((List) stockItemTransactions);
    }

    public void buildStockItemsOutWithoutFS() throws InfrastructureException, ParseException {
        String orgBdgtCode = stockItemTransactionsForm.getOrgBdgtCode();
        String fiscalYear[] = stockItemTransactionsForm.getFiscalYear();
        Date startDate = DateUtils.createDate(stockItemTransactionsForm.getDateFrom());
        Date endDate =  DateUtils.createDate(stockItemTransactionsForm.getDateTo());
        String categoryId = stockItemTransactionsForm.getCategoryId();
        String icnbr = stockItemTransactionsForm.getIcnbr();

        Collection stockItemTransactionsWithoutFS = daoFactory.getRequestLineItemDAO()
                .findStockItemOutTransactionsWithoutFS(startDate, endDate, categoryId, icnbr);


        stockItemTransactionsForm.setStockItemTransactions((List) stockItemTransactionsWithoutFS);
    }

    public void buildStockItemsIn() throws InfrastructureException, ParseException{
        String orgBdgtCode = stockItemTransactionsForm.getOrgBdgtCode();
        Date startDate = DateUtils.createDate(stockItemTransactionsForm.getDateFrom());
        Date endDate =  DateUtils.createDate(stockItemTransactionsForm.getDateTo());
        String categoryId = stockItemTransactionsForm.getCategoryId();
        String icnbr = stockItemTransactionsForm.getIcnbr();
        Collection stockItemTransactions = daoFactory.getRequestLineItemDAO()
                .findStockItemInTransactions(orgBdgtCode, startDate, endDate, categoryId, icnbr);
        stockItemTransactionsForm.setStockItemTransactions((List) stockItemTransactions);
    }

}
