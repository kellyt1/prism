package us.mn.state.health.util.hibernate.queryparameters;

import java.util.List;

public class BaseSQLQueryParameters {
    private String entityAlias;
    private Class entityClass;
    private List<String> scalarProperties;

    public BaseSQLQueryParameters(String entityAlias, Class entityClass, List<String> scalarProperties) {

        this.entityClass = entityClass;
        this.scalarProperties = scalarProperties;
        this.entityAlias = entityAlias;
    }

    public BaseSQLQueryParameters(String entityAlias, Class entityClass) {
        this.entityClass = entityClass;
        this.entityAlias = entityAlias;
    }

    public BaseSQLQueryParameters(List<String> scalarProperties) {
        this.scalarProperties = scalarProperties;
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public List<String> getScalarProperties() {
        return scalarProperties;
    }
}

