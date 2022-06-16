package us.mn.state.health.view.materialsrequest.reports;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.FiscalYears;

public class PurchaseItemTransactionsForm extends ValidatorForm {
    private String dateFrom;
    private String dateTo;
    private String orgBdgtCode;
    private String[] fiscalYear;

    private String categoryId;
    private List categories = new ArrayList();
    private List fiscalyears = new ArrayList();

    private List orderedPurchaseItemTransactions = new ArrayList();


    public PurchaseItemTransactionsForm() throws InfrastructureException {
        dateTo = DateUtils.toString(new Date());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(Calendar.MONTH, -1);
        dateFrom = DateUtils.toString(new Date(gregorianCalendar.getTimeInMillis()));
        categories = (List) DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS, false);
        fiscalyears = (List) DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getFiscalYearsDAO().findAll();
        if (fiscalYear == null ) {
            FiscalYears fiscalYearById = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getFiscalYearsDAO().getFiscalYearById(new Long(0), false);
            if (fiscalYearById!=null) {
                fiscalYear = new String[1];
                fiscalYear[0] = fiscalYearById.getFiscalyear().toString();
                //System.out.println("Fiscal Year " + fiscalYear);
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

    public String getOrgBdgtCode() {
        return orgBdgtCode;
    }

    public void setOrgBdgtCode(String orgBdgtCode) {
        this.orgBdgtCode = orgBdgtCode;
    }

    public String[] getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String[] fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

//    public String getFiscalYear() {
//        return fiscalYear;
//    }
//
//    public void setFiscalYear(String fiscalYear) {
//        this.fiscalYear = fiscalYear;
//    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public List getOrderedPurchaseItemTransactions() {
        return orderedPurchaseItemTransactions;
    }

    public void setOrderedPurchaseItemTransactions(List orderedPurchaseItemTransactions) {
        this.orderedPurchaseItemTransactions = orderedPurchaseItemTransactions;
    }

    public Double getEstimatedTotalAmount(){
        double result  = 0.00d;
        for (Iterator iterator = orderedPurchaseItemTransactions.iterator(); iterator.hasNext();) {
            PurchaseItemTransaction purchaseItemTransaction = (PurchaseItemTransaction) iterator.next();
            result+=purchaseItemTransaction.getEstAmountPaid().doubleValue();
        }

        return new Double(((Math.round(result*100))+0.00d)/100.00d);
    }

    public Double getTotal(){
        double result = 0.00d;
        Set rliIds = new HashSet();
        for (Iterator iterator = orderedPurchaseItemTransactions.iterator(); iterator.hasNext();) {
            PurchaseItemTransaction purchaseItemTransaction = (PurchaseItemTransaction) iterator.next();
            if(!rliIds.contains(purchaseItemTransaction.getRequestLineItemId())){
                result+=purchaseItemTransaction.getQtyOrdered().doubleValue()*purchaseItemTransaction.getBuyUnitCost().doubleValue();
            }
            rliIds.add(purchaseItemTransaction.getRequestLineItemId());
        }
        return new Double(((Math.round(result*100))+0.00d)/100.00d);
    }

}
