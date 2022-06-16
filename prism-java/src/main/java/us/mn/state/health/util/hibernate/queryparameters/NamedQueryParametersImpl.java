package us.mn.state.health.util.hibernate.queryparameters;

public class NamedQueryParametersImpl extends QueryParameters {
    private String queryName;

    public NamedQueryParametersImpl(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryName() {
        return queryName;
    }
}
