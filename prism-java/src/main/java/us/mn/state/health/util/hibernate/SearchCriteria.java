package us.mn.state.health.util.hibernate;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import us.mn.state.health.util.Pagination;

public abstract class SearchCriteria {
    private Pagination pagination = Pagination.DEFAULT_PAGINATION;
    private String sortMethod = DESC;
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String[] SORT_METHODS = {ASC, DESC};


    public Pagination getPagination() {
        return pagination;
    }

    public void setPageNo(int pageNo) {
        pagination = new Pagination(pageNo, pagination.getPageSize());
    }

    public int getPageNo() {
        return pagination.getPageNumber();
    }

    public void setPageSize(int pageSize) {
        pagination = new Pagination(pagination.getPageNumber(), pageSize);
    }

    protected boolean isSpecified(String s) {
        return StringUtils.isNotBlank(s);
    }

    protected boolean isSpecified(Date date) {
        return !(date == null);
    }

    public String getSortMethod() {
        return sortMethod;
    }

    public void setSortMethod(String sortMethod) {
        this.sortMethod = sortMethod;
    }
}
