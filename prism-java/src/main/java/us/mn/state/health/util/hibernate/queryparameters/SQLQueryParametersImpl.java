package us.mn.state.health.util.hibernate.queryparameters;

public class SQLQueryParametersImpl extends QueryParametersImpl implements SQLQueryParameters {
    private BaseSQLQueryParameters baseSQLQueryParameters;

    public SQLQueryParametersImpl(String query) {
        super(query);
    }

    public SQLQueryParametersImpl(String query, String totalQuery) {
        super(query, totalQuery);
    }

    public SQLQueryParametersImpl(String query, BaseSQLQueryParameters baseSQLQueryParameters) {
        super(query);
        this.baseSQLQueryParameters = baseSQLQueryParameters;
    }

    public SQLQueryParametersImpl(String query, String totalQuery, BaseSQLQueryParameters baseSQLQueryParameters) {
        super(query, totalQuery);
        this.baseSQLQueryParameters = baseSQLQueryParameters;
    }

    public BaseSQLQueryParameters getBaseSqlQueryParameters() {
        return baseSQLQueryParameters;
    }
}
