package us.mn.state.health.view.materialsrequest.reports;

import org.apache.struts.validator.ValidatorForm;

import java.util.*;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.FiscalYears;

/**
 * Created by IntelliJ IDEA.
 * User: kothas1
 * Date: Apr 7, 2010
 * Time: 1:51:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class FishConsumptionForm extends ValidatorForm {
    private String dateFrom;
    private String dateTo;
    private String icnbr;
    private Long[] selectedCategories;



    private List<Category> categories = new ArrayList();

    //private List orderedPurchaseItemTransactions = new ArrayList();


       public FishConsumptionForm() throws InfrastructureException {
        dateTo = DateUtils.toString(new Date());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(Calendar.MONTH,-1);
        dateFrom = DateUtils.toString(new Date(gregorianCalendar.getTimeInMillis()));
       //ategories = (List) DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS, false);

    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
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

//    public String getOrgBdgtCode() {
//        return orgBdgtCode;
//    }
//
//    public void setOrgBdgtCode(String orgBdgtCode) {
//        this.orgBdgtCode = orgBdgtCode;
//    }



//    public String getFiscalYear() {
//        return fiscalYear;
//    }
//
//    public void setFiscalYear(String fiscalYear) {
//        this.fiscalYear = fiscalYear;
//    }


    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public Long[] getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(Long[] selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

//    public List getOrderedPurchaseItemTransactions() {
//        return orderedPurchaseItemTransactions;
//    }
//
//    public void setOrderedPurchaseItemTransactions(List orderedPurchaseItemTransactions) {
//        this.orderedPurchaseItemTransactions = orderedPurchaseItemTransactions;
//    }
}
