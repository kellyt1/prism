package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.validator.ValidatorForm;

public class SearchCatalogForm extends ValidatorForm {
    private String query;
    private String ckShowInactive="ACT";
    private Collection results = new ArrayList(); // collection of Item objects

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Collection getResults() {
        return results;
    }

    public String getCkShowInactive() {
        return ckShowInactive;
    }

    public void setCkShowInactive(String ckShowInactive) {
        this.ckShowInactive = ckShowInactive;
    }

    public void setResults(Collection results) {
        this.results = results;
    }
}
