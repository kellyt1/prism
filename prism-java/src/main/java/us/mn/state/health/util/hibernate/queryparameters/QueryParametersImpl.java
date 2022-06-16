package us.mn.state.health.util.hibernate.queryparameters;

public class QueryParametersImpl extends QueryParameters {
    private String query;
    private String totalQuery;

    public QueryParametersImpl(String query) {
        this.query = query;
    }

    public QueryParametersImpl(String query, String totalQuery) {
        this.query = query;
        this.totalQuery = totalQuery;
    }

    public String getQuery() {
        return query;
    }

    public String getTotalQuery() {
        return totalQuery;
    }
}
