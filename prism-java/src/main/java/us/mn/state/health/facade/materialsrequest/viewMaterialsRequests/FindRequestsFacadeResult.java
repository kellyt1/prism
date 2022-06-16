package us.mn.state.health.facade.materialsrequest.viewMaterialsRequests;

import java.util.List;

import us.mn.state.health.util.PagedQueryResultEnhanced;

public class FindRequestsFacadeResult {
    private PagedQueryResultEnhanced queryResult;
    private List users;
    private List statuses;
    private List categories;
    private int status;


    public PagedQueryResultEnhanced getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(PagedQueryResultEnhanced queryResult) {
        this.queryResult = queryResult;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List getUsers() {
        return users;
    }

    public void setUsers(List users) {
        this.users = users;
    }


    public List getStatuses() {
        return statuses;
    }

    public void setStatuses(List statuses) {
        this.statuses = statuses;
    }


    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }
}
