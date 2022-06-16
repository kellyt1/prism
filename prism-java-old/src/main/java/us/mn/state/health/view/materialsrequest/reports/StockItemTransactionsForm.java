package us.mn.state.health.view.materialsrequest.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.FiscalYears;

public class StockItemTransactionsForm extends ValidatorForm {
    private String dateFrom ;
    private String dateTo;
    private String orgBdgtCode;
    private String[] fiscalYear;
    private String categoryId;
    private String icnbr;
    private List categories = new ArrayList();
    private List fiscalyears = new ArrayList();
    private List stockItemTransactions = new ArrayList();



    public StockItemTransactionsForm() throws InfrastructureException {
        dateTo = DateUtils.toString(new Date());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(Calendar.MONTH,-1);
        dateFrom = DateUtils.toString(new Date(gregorianCalendar.getTimeInMillis()));
        categories = (List) DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS, false);
        fiscalyears = (List) DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getFiscalYearsDAO().findAll();
        if (fiscalYear == null ) {
            FiscalYears rVal =   DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getFiscalYearsDAO().getFiscalYearById(new Long(0),false);
            if (rVal != null) {
                fiscalYear = new String[1];
                fiscalYear[0] = rVal.getFiscalyear().toString();
            }
        }
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

//    public String getFiscalYear() {
//        return fiscalYear;
//    }
//
//    public void setFiscalYear(String fiscalYear) {
//        this.fiscalYear = fiscalYear;
//    }

    public String[] getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String[] fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getOrgBdgtCode() {
        return orgBdgtCode;
    }

    public void setOrgBdgtCode(String orgBdgtCode) {
        this.orgBdgtCode = orgBdgtCode;
    }

    public List getStockItemTransactions() {
        return stockItemTransactions;
    }

    public void setStockItemTransactions(List stockItemTransactions) {
        this.stockItemTransactions = stockItemTransactions;
    }

    public Double getTotalAmountPaid(){
        double result  = 0.00d;
        for (Iterator iterator = stockItemTransactions.iterator(); iterator.hasNext();) {
            StockItemOutTransaction outTransaction =  (StockItemOutTransaction) iterator.next();
            result+=outTransaction.getAmountPaid().doubleValue();
        }
        return new Double(((Math.round(result*100))+0.00d)/100.00d);
    }

    public int getTotalRequested(){
        int i = 0;
        Set rliIds = new HashSet();
        for (Iterator iterator = stockItemTransactions.iterator(); iterator.hasNext();) {
            StockItemOutTransaction outTransaction = (StockItemOutTransaction) iterator.next();
            Long requestLineItemId = outTransaction.getRequestLineItemId();
            if (!rliIds.contains(requestLineItemId)) {
                i+=outTransaction.getQtyRequested().intValue();
            }
            rliIds.add(requestLineItemId);
        }
        return i;
    }

    public int getTotalFilled() {
        int i = 0;
        Set rliIds = new HashSet();
        for (Iterator iterator = stockItemTransactions.iterator(); iterator.hasNext();) {
            StockItemOutTransaction outTransaction = (StockItemOutTransaction) iterator.next();
            Long requestLineItemId = outTransaction.getRequestLineItemId();
            if (!rliIds.contains(requestLineItemId)) {
                i+=outTransaction.getQtyFilled().intValue();
            }
            rliIds.add(requestLineItemId);
        }
        return i;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }


    public List getFiscalyears() {
        return fiscalyears;
    }

    public void setFiscalyears(List fiscalyears) {
        this.fiscalyears = fiscalyears;
    }


}

