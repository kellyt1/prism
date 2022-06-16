package us.mn.state.health.builder.materialsrequest;

import java.util.Date;
import java.util.Collection;
import java.util.List;
import java.text.ParseException;

import us.mn.state.health.view.materialsrequest.reports.PurchaseItemTransactionsForm;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class PurchaseItemTransactionsFormBuilder {
    private PurchaseItemTransactionsForm transactionsForm;
    private DAOFactory daoFactory;

    public PurchaseItemTransactionsFormBuilder(PurchaseItemTransactionsForm transactionsForm, DAOFactory daoFactory) {
        this.transactionsForm = transactionsForm;
        this.daoFactory = daoFactory;
    }

    public void buildOrderedPurchaseItemTransactions() throws ParseException, InfrastructureException {
        String orgBdgtCode = transactionsForm.getOrgBdgtCode();
        Date startDate = DateUtils.createDate(transactionsForm.getDateFrom());
        Date endDate =  DateUtils.createDate(transactionsForm.getDateTo());
        String categoryId = transactionsForm.getCategoryId();
        
        String[] fiscalYear = transactionsForm.getFiscalYear();

        Collection orderedPurchaseItemTransactions = daoFactory.getRequestLineItemDAO()
                .findOrderedPurchaseItemTransactions(orgBdgtCode,fiscalYear, startDate, endDate, categoryId);
        orderedPurchaseItemTransactions.size();
        transactionsForm.setOrderedPurchaseItemTransactions((List) orderedPurchaseItemTransactions);

    }
}
